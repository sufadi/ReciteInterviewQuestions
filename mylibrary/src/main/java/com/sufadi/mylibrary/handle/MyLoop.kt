package com.sufadi.mylibrary.handle

import java.lang.RuntimeException

/**
 * 要实现生产者／消费者模型，首先的有锁，这里使用ReentrantLock
 * 主要考虑的重写入，它可以根据设定的变量来唤醒不同类型的锁，也就是说当我们队列有数据时，我们需要唤醒read锁；当队列有空间时，我们需要唤醒写锁。
 */
object MyLoop {
    var mQueue: MyMessageQueue? = null
    var threadLocal = ThreadLocal<MyLoop>()

    init {
        mQueue = MyMessageQueue()
    }

    /**
     * 为当前现成初始化一个Looper副本对象
     */
    fun prepare() {
        if (threadLocal.get() != null) {
            throw RuntimeException("    Only one Looper may be create pre thread")
        }

        threadLocal.set(MyLoop)
        println("    MyLoop MyLoop 初始化 prepare")
    }


    /**
     * 获取当前线程的looper副本对象
     */
    fun myLooper(): MyLoop? {
        return threadLocal.get()
    }

    /**
     * 轮询消息
     */
    fun loop() {
        // 获取当前线程的Looper对象
        val myLooper = myLooper()
        var msg: MyMessage? = null

        while (true) {
            msg = myLooper?.mQueue?.next()
            if (msg?.target == null) {
                println("    MyLoop 空消息")
                continue
            }

            println("    MyLoop Loop 轮询到消息了，接下来发送消息")
            msg?.target?.dispatchMessage(msg)
        }
    }
}