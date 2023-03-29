package com.learndm.nnsman.novelreader.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.learndm.nnsman.novelreader.data.db.BookDao
import com.learndm.nnsman.novelreader.data.model.Book
import com.learndm.nnsman.novelreader.data.network.NovelReaderNetwork
import com.learndm.nnsman.novelreader.data.parse.BookStoreHtmlParse
import com.learndm.nnsman.novelreader.ui.home.ranking.VP2RankingPagingSource
import com.learndm.nnsman.novelreader.ui.search.SearchBookPagingSource
import com.learndm.nnsman.novelreader.ui.search.SearchBookPagingSource.Companion.PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.zip

class BookRepository private constructor(
    private val bookDao: BookDao,
    private val network: NovelReaderNetwork,
    private val bookStoreHtmlParse: BookStoreHtmlParse
) {

    fun searchBookByKeyword(keyword: String): Flow<PagingData<Book>> = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
        pagingSourceFactory = { SearchBookPagingSource(bookStoreHtmlParse, keyword) }
    ).flow

    fun getBooksByType(type: Int): Flow<PagingData<Book>> = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
        pagingSourceFactory = { VP2RankingPagingSource(bookStoreHtmlParse, type) }
    ).flow

    fun getBookInfo(bookUrl: String) = flow {
        emit(bookStoreHtmlParse.getBookInfoByUrl(bookUrl))
    }

    fun getChapters(bookUrl: String, chapterNumber: IntArray) = flow {
        emit(bookStoreHtmlParse.getChaptersInfo(bookUrl, chapterNumber))
    }

    fun getChapterTitles(bookUrl: String) = flow {
        emit(bookStoreHtmlParse.getChapterTitleList(bookUrl))
    }

    fun getDetailBookInfo(bookUrl: String, chapterNumber: IntArray) =
        getBookInfo(bookUrl)
            .zip(getChapters(bookUrl, chapterNumber)) { book, chapters ->
                book?.chapter = chapters
                book
            }.zip(getChapterTitles(bookUrl)) { book, titles ->
                book?.chapterTitles = titles
                book?.chapterNumber = titles.size
                book
            }

    companion object {
        private var instance: BookRepository? = null

        fun getInstance(
            bookDao: BookDao,
            network: NovelReaderNetwork,
            htmlParse: BookStoreHtmlParse
        ): BookRepository {
            if (instance == null) {
                instance = BookRepository(bookDao, network, htmlParse)
            }
            return instance!!
        }
    }
}