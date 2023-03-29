package com.learndm.nnsman.novelreader.data.model

data class ResponseSearchBookByKeyword(
    val keyword: String = "",
    val currentPage: Int = 1,
    val totalPage: Int = 1,
    val books: ArrayList<Book> = ArrayList()
)

data class ChapterTitle(val title: String = "", val chapterUrl: String = "")

data class ResponseGetBooksByType(
    val type: Int = 1,
    val currentPage: Int = 1,
    val totalPage: Int = 1,
    val books: ArrayList<Book> = ArrayList()
)