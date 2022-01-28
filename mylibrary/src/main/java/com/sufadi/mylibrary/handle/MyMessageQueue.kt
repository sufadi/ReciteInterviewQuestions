package com.sufadi.mylibrary.handle

import java.lang.Exception
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock

class MyMessageQueue {
    var messageArray= arrayOfNulls<MyMessage>(30)
    var putIndex = 0

    // 队列中的消息数
    var count = 0
    var takeIndex = 0

    // 锁
    var lock:ReentrantLock? = null

    // 唤醒，沉睡某个线程操作
    var getCondition: Condition? =null
    var addCondition: Condition? =null

    init {
        lock = ReentrantLock()
        getCondition = lock?.newCondition()
        addCondition = lock?.newCondition()
    }


    /**
     * 添加消息队列
     */
    fun enqueueMessage(msg: MyMessage) {
        println("    MyMessageQueue 添加消息队列 enqueueMessage ${msg.obj}")
        try {
            lock?.lock()

            // 检查队列是否满了
            while (count >= messageArray.size) {
                println("    MyMessageQueue 队列满了，写锁阻塞")
                // 阻塞
                addCondition?.await()
            }
            messageArray[putIndex] = msg
            // 处理越界， index 数组容量时，替换第一个
            if (putIndex >= messageArray.size) {
                putIndex = 0
            } else {
                putIndex ++
            }
            count ++

            // 通知消费者消费
            getCondition?.signalAll()

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            lock?.unlock()
        }
    }

    fun next(): MyMessage? {
        var msg: MyMessage? = null
        try {
            lock?.lock()
            // 检查队列是否空了
            while (count <= 0) {
                println("    MyMessageQueue 队列空了，读锁阻塞")
                // 读锁阻塞
                getCondition?.await()
            }

            msg = messageArray[takeIndex] // 可能空
            // 消息被处理侯，置为空
            messageArray[takeIndex] = null

            // 处理数据越界
            if (takeIndex >= messageArray.size) {
                takeIndex = 0
            } else {
                takeIndex ++
            }
            count --

            // 通知生产者生产
            addCondition?.signalAll()

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            lock?.unlock()
        }
        return msg
    }
}