package com.learndm.nnsman.novelreader.data.network.api


import com.learndm.nnsman.novelreader.data.model.Book
import retrofit2.Call
import retrofit2.http.*


interface BookService {
    @POST("book/add")
    fun addBook(@Body book: Book): Call<Book>

    @DELETE("book/delete/{id}")
    fun deleteBook(@Path("id") id: String): Call<Boolean>

    @GET
    fun searchBookByKeyword(@Url url: String): Call<String>

    @GET
    fun getBooksByType(@Url url: String): Call<String>

    @GET
    fun getChaptersInfo(@Url url: String): Call<String>

    @GET
    fun getBookInfo(@Url url: String): Call<String>
    @GET
    fun getChapterTitleList(@Url url: String): Call<String>

}