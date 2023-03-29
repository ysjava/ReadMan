package com.learndm.nnsman.novelreader.data.parse

import androidx.annotation.IntRange
import com.learndm.nnsman.novelreader.data.model.*
import com.learndm.nnsman.novelreader.data.network.NovelReaderNetwork
import org.jsoup.Jsoup

abstract class BookStoreHtmlParse(val network: NovelReaderNetwork) {

    abstract suspend fun searchBookByKeyword(keyword: String, page: Int):ResponseSearchBookByKeyword
    abstract suspend fun getChapterTitleList(bookUrl: String):ArrayList<ChapterTitle>
    abstract suspend fun getChaptersInfo(bookUrl: String, chapterNumbers: IntArray):ArrayList<Chapter>
    abstract suspend fun getBooksByType(type: Int, page: Int):ResponseGetBooksByType
    abstract suspend fun getBookInfoByUrl(bookUrl: String): Book?
}