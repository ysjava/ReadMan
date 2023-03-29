package com.learndm.nnsman.novelreader.ui.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.learndm.nnsman.novelreader.data.BookRepository

class SearchBookViewModel(
    private val bookRepository: BookRepository,
    private val state: SavedStateHandle
) : ViewModel() {
    fun searchBookByKeyword(keyword: String) =
        bookRepository.searchBookByKeyword(keyword).cachedIn(viewModelScope)

}