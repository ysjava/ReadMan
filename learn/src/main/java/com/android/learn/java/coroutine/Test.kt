package com.android.learn.java.coroutine

import android.util.Log
import com.android.learn.logd
import kotlinx.coroutines.*
import okhttp3.*
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread
import kotlin.coroutines.*

/*
*
*
*
* 协程是一种设计模式,使用它来简化异步操作,在android中就像线程池,Handler等线程的操作框架.
* 协程挂起就是执行到suspend函数后 就是去开启一个线程去执行,执行完毕后再自动回到线程中继续执行,通过withContext方法.
*
* */

val ceHandler = CoroutineExceptionHandler { _, e ->
    log("error msg :${e.message}")
}

fun main() {
    runBlocking {
        launch {  }
        withContext(Dispatchers.IO) {
            val s = test3()
            log("test1 hha")
            log("test2 $s")
        }

//        val parentScope = test2()
//
//        delay(3000)
//        parentScope.coroutineContext[Job]?.cancel()
//        log("cancel")
//        delay(2000)
    }
    log("end")
}

fun log(str: String) {
    println("${Thread.currentThread()}:$str")
}

suspend fun test3() {

    var result = "bad"
    val clent = OkHttpClient()
    val request = Request.Builder().get().url("https://www.baidu.com").build()

    val call = clent.newCall(request)
    call.enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            //it.resumeWithException(e)
            log("result = $e")
        }

        override fun onResponse(call: Call, response: Response) {
            //it.resume(response.body?.string() ?: "er")
            log("result = ${response.body?.string()}")

        }

    })
    call.cancel()
log("re end")
    //while (true){}
}


fun test1(): CoroutineScope {

    val parentScope = CoroutineScope(Dispatchers.Default)
    parentScope.launch {

        val scope =
            CoroutineScope(Dispatchers.Default + SupervisorJob(coroutineContext[Job]) + ceHandler)

        scope.launch {
            throw NullPointerException("haha")
        }

        scope.launch {
            while (true) {
                delay(1000)
                log("33")
            }
        }
    }
    parentScope.launch {
        while (true) {
            delay(1000)
            log("22")
        }
    }
    return parentScope
}

fun test2(): CoroutineScope {
    val scope = CoroutineScope(Dispatchers.Default)

    scope.launch(SupervisorJob(scope.coroutineContext[Job]) + ceHandler) {
        //val childScope = CoroutineScope(coroutineContext)
        launch(SupervisorJob(coroutineContext[Job])) {
            throw NullPointerException("haha")
//            throw CancellationException("c e")
        }
        launch {
            while (true) {
                delay(1000)
                log("66")
            }
        }
    }

    scope.launch {
        while (true) {
            delay(1000)
            log("55")
        }
    }
    return scope
}