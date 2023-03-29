package com.android.learn.ipc

import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.os.RemoteCallbackList
import android.os.RemoteException
import com.android.learn.IBookManager
import com.android.learn.IBookUpdateListener
import com.android.learn.logd
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicBoolean

class BookManagerService : Service() {
    private val books: CopyOnWriteArrayList<Book> = CopyOnWriteArrayList<Book>()
    private val listenerDataList: RemoteCallbackList<IBookUpdateListener> =
        RemoteCallbackList<IBookUpdateListener>()
    private val isDestroy = AtomicBoolean(false)
    val binder = object : IBookManager.Stub() {
        override fun getBookList(): MutableList<Book> {
            Thread.sleep(10000)
            return books
        }

        override fun addBook(book: Book) {
            bookList.add(book)
        }

        override fun registerListener(listener: IBookUpdateListener?) {
            listenerDataList.register(listener)
        }

        override fun unregisterListener(listener: IBookUpdateListener?) {
            listenerDataList.unregister(listener)
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        packageManager
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        books.add(Book("android", 100))
        books.add(Book("java", 300))


        Thread(ServiceWorker()).start()
    }

    override fun onDestroy() {
        isDestroy.set(true)
        super.onDestroy()
    }

    inner class ServiceWorker : Runnable {
        //每过5s进行一次添加书籍并通知观察者
        override fun run() {
            while (!isDestroy.get()) {
                try {
                    Thread.sleep(5000)
                    //添加
                    val newBook = Book("new book ${(1..10000).random()}", (1..10000).random())
                    books.add(newBook)
                    //通知更新
                    val n = listenerDataList.beginBroadcast()
                    try {
                        for (i in 0 until n) {
                            val listener = listenerDataList.getBroadcastItem(i)
                            listener.onBookUpdate(newBook)
                        }
                    } catch (e: RemoteException) {
                        e.printStackTrace()
                    } finally {
                        listenerDataList.finishBroadcast()
                    }

                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }
        }
    }
}