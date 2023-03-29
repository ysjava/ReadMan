package com.android.learn.ipc

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log

val MSG_FORM_CLIENT = 1
val MSG_FORM_SERVER = 0

class MyService : Service() {

    class MessengerHandler : Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            when(msg.what){
                MSG_FORM_CLIENT -> {
                    Log.d("xasedqweq",msg.data.getString("client")?:"错误key")
                    val clientMessenger = msg.replyTo
                    val message = Message.obtain()
                    message.what = MSG_FORM_SERVER
                    val data = Bundle()
                    data.putString("server","hello client, i got your message!")
                    message.data = data
                    clientMessenger.send(message)
                }
                else -> super.handleMessage(msg)
            }
        }
    }

    val messenger = Messenger(MessengerHandler())

    override fun onBind(intent: Intent?): IBinder {
        return messenger.binder
    }
}