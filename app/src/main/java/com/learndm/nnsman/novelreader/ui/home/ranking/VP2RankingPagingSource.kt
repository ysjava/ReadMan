package com.learndm.nnsman.novelreader.ui.home.ranking

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.learndm.nnsman.novelreader.data.model.Book
import com.learndm.nnsman.novelreader.data.parse.BookStoreHtmlParse

class VP2RankingPagingSource(
    private val bookStoreHtmlParse: BookStoreHtmlParse,
    private val type: Int
) : PagingSource<Int, Book>() {

    companion object{
        const val PAGE_SIZE = 12
    }

    override fun getRefreshKey(state: PagingState<Int, Book>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Book> {
        return try {
            val key = params.key ?: 1
            val data = bookStoreHtmlParse.getBooksByType(type, key)
            val preKey = if (key > 1) key - 1 else null
            val nextKey =
                if (data.books.isNotEmpty() && data.books.size == PAGE_SIZE) key + 1 else null
            return LoadResult.Page(data.books, preKey, nextKey)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}