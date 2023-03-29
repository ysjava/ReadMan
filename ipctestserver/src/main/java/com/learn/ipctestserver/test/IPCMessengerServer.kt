package com.learn.ipctestserver.test

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log

class IPCMessengerServer : Service() {

    class ServerHandler : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                1 -> {
                    val data = msg.data.getString("client", "invalid key")
                    val clientMessenger = msg.replyTo
                    Log.d("IPCMessengerServer", data)

                    val replyMessage = Message.obtain()
                    replyMessage.what = 0
                    val bundle = Bundle()
                    bundle.putString("server", "hi client, i got your message!")
                    replyMessage.data = bundle
                    try {
                        clientMessenger.send(replyMessage)
                    } catch (e: RemoteException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    private val messenger = Messenger(ServerHandler())
    override fun onBind(intent: Intent?): IBinder? {
        return messenger.binder
    }
}