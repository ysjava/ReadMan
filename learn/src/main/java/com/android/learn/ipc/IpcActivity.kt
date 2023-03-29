package com.android.learn.ipc

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.android.learn.IBookManager
import com.android.learn.IBookUpdateListener
import com.android.learn.R
import com.android.learn.logd
import java.io.File
import java.lang.Process
import java.nio.file.FileSystem
import java.util.concurrent.CopyOnWriteArrayList




class IpcActivity : AppCompatActivity() {

    companion object {
        const val PKG = "com.learn.ipctestserver"
        const val CLS = "com.learn.ipctestserver.test.IPCMessengerServer"
        val list = mutableListOf<TextView?>()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_ipc)
        textView = findViewById(R.id.textView)
        val button1 = findViewById<Button>(R.id.button1)
        val button2 = findViewById<Button>(R.id.button2)


        //messenger start
//        val intent = Intent()
//        //通过ComponentName来启动和Intent(x,x::class.java)效果一样,并且还可用于不同应用
//        intent.component = ComponentName(PKG, CLS)
//        findViewById<Button>(R.id.button2).setOnClickListener {
//            val bindService = bindService(intent, connection, BIND_AUTO_CREATE)
//            Log.d("asdqwtwq","绑定服务:$bindService")
//        }
        list.add(textView)
        //messenger end

        //aidl start

        button1.setOnClickListener {
//            val service = BookManagerService()
//            val bm = IBookManager.Stub.asInterface(service.binder)
//            logd("list1 : ${bm.bookList}")
//            bm.addBook(Book("test",999))
//            logd("list2 : ${bm.bookList}")

            logd("aa ${iBookManager?.bookList}")
        }
        button2.setOnClickListener {
            val intent = Intent(this, BookManagerService::class.java)
            bindService(intent,bookManagerServiceConnection, BIND_AUTO_CREATE)
        }
        //aidl end
    }

    private var textView: TextView? = null

    class MessengerHandler : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_FORM_SERVER -> {
                    list[0]?.text = msg.data.getString("server", "错误key")
                    Log.d("asdqwtwq", msg.data.getString("server", "错误key"))
                }
                else -> super.handleMessage(msg)
            }
        }
    }
    val replyMessenger = Messenger(MessengerHandler())
    val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder) {
            val messenger = Messenger(service)
            val message = Message.obtain()
            message.what = MSG_FORM_CLIENT
            val data = Bundle()
            data.putString("client", "hello i am client!")
            message.data = data
            message.replyTo = replyMessenger
            try {
                messenger.send(message)
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }

    }

    private var iBookManager: IBookManager? = null
    val bookManagerServiceConnection = object : ServiceConnection{
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val bookManger = IBookManager.Stub.asInterface(service)
            iBookManager = bookManger
            try {
//                var list = bookManger.bookList
//                logd("list : $list")
//                bookManger.addBook(Book("kotlin",600))
//                list = bookManger.bookList
//                logd("list : $list")

                bookManger.registerListener(listenerBinder)
            }catch (e: RemoteException){
                e.printStackTrace()
            }
        }

        override fun onServiceDisconnected(name: ComponentName) {

        }
    }

    private val listenerBinder = object : IBookUpdateListener.Stub(){
        override fun onBookUpdate(book: Book) {
            logd("onBookUpdate $book")
        }
    }

    override fun onDestroy() {

        iBookManager?.let {
            if (it.asBinder().isBinderAlive){
                try {
                    logd("unregister listener $listenerBinder")
                    it.unregisterListener(listenerBinder)
                }catch (e: RemoteException){
                    e.printStackTrace()
                }
            }
        }


        unbindService(bookManagerServiceConnection)
        super.onDestroy()
    }
}