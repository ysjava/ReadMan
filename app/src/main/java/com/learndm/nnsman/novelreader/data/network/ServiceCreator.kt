package com.learndm.nnsman.novelreader.data.network

import com.learndm.nnsman.novelreader.data.network.api.BookService
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object ServiceCreator {
    private const val BASE_URL = "https://www.quanwenyuedu.io"
    private const val UserAgent =
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.117 Safari/537.36"
    private val builder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(OkHttpClient.Builder().addInterceptor {
            val oldRequest = it.request()
            val rb = oldRequest.newBuilder()
                .removeHeader("User-Agent")
                .header("User-Agent", UserAgent)
//                .header("Content-Type","application/x-www-form-urlencoded; charset=UTF-8")
//                .header("Accept-Encoding", "gzip, deflate")
//                .header("Connection", "keep-alive")
//                .header("Accept", "*/*")
            val newRequest = rb.build()
            it.proceed(newRequest)
        }.build())
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())

    fun <T> create(serviceClass: Class<T>) = builder.build().create(serviceClass)
}