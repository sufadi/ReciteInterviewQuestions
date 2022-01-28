package com.sufadi.mylibrary.handle

import java.lang.Thread.currentThread

/**
 * 手写一个handler
 * https://www.jianshu.com/p/91aa90886347
 */
class RunDemo {

    companion object {
        var handler: MyHandler? = null

        @JvmStatic
        fun main(args: Array<String>) {
            println("RunDemo main")

            Thread(Runnable {
                MyLoop.prepare()
                handler = object : MyHandler() {
                    override fun handleMessage(message: MyMessage) {
                        super.handleMessage(message)
                        println("RunDemo 消息接受：" + currentThread().name + "线程接收到：" + message.obj
                        )
                    }
                }
                MyLoop.loop()
            }).start()
            //睡0.5s，保证上面的线程中looper初始化好了
            //睡0.5s，保证上面的线程中looper初始化好了
            Thread.sleep(500)
            Thread(Runnable {
                val message = MyMessage()
                message.obj = currentThread().name + " 旺财 "
                println("RunDemo 消息发送：${message.obj}")
                handler?.sendMessage(message)
            }).start()
            Thread(Runnable {
                val message = MyMessage()
                message.obj = currentThread().name + " 黑胖 "
                println("RunDemo 消息发送：${message.obj}")
                handler?.sendMessage(message)
            }).start()
        }
    }

}