package com.learndm.nnsman.novelreader.data.network

import com.learndm.nnsman.novelreader.data.model.*
import com.learndm.nnsman.novelreader.data.network.api.BookService
import com.learndm.nnsman.novelreader.data.network.api.ChapterService
import com.learndm.nnsman.novelreader.data.network.api.CommentService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class NovelReaderNetwork private constructor() {
    private val bookService = ServiceCreator.create(BookService::class.java)
    private val chapterService = ServiceCreator.create(ChapterService::class.java)
    private val commentService = ServiceCreator.create(CommentService::class.java)

    suspend fun addBook(book: Book): Book = bookService.addBook(book).await()
    suspend fun deleteBook(id: String): Boolean = bookService.deleteBook(id).await()

    suspend fun subscribeChapter(chapterId: String): Subscription =
        chapterService.subscribeChapter(chapterId).await()

    suspend fun findComments(chapterId: String, startIndex: Int, endIndex: Int): List<Comment> =
        commentService.findComments(chapterId, startIndex, endIndex).await()


    suspend fun searchBookByKeyword(url: String): String =
        bookService.searchBookByKeyword(url).await()

    suspend fun getBooksByType(url: String): String = bookService.getBooksByType(url).await()
    suspend fun getChaptersInfo(url: String): String = bookService.getChaptersInfo(url).await()
    suspend fun getChapterTitleList(url: String): String = bookService.getChapterTitleList(url).await()
    suspend fun getBookInfo(url: String): String = bookService.getBookInfo(url).await()


    private suspend fun <T> Call<T>.await() = suspendCoroutine<T> {

        enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val body = response.body()
                if (body != null)
                    it.resume(body)
                else
                    it.resumeWithException(RuntimeException("response body is null"))
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                it.resumeWithException(t)
            }

        })
    }

    companion object {
        private var instance: NovelReaderNetwork? = null
        fun getInstance(): NovelReaderNetwork {
            if (instance == null) {
                instance = NovelReaderNetwork()
            }
            return instance!!
        }
    }
}