package com.sufadi.mylibrary.handle

open class MyHandler() {
    var mLooper: MyLoop?= null
    var mQueue: MyMessageQueue? = null

    init {
        // 获取当前线程的 looper
        mLooper = MyLoop.myLooper()
        // 获取当前线程的消息队列
        mQueue = mLooper?.mQueue
    }

    /**
     * 发送消息
     */
    fun sendMessage(msg: MyMessage) {
        msg.target = this
        mQueue?.enqueueMessage(msg)
    }

    /**
     * 处理消息
     */
    open fun handleMessage(mgs: MyMessage) {

    }

    /**
     * 分发消息
     */
    fun dispatchMessage(msg: MyMessage) {
        handleMessage(msg)
    }
}