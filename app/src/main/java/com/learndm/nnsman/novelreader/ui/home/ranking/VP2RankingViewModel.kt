package com.learndm.nnsman.novelreader.ui.home.ranking

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.learndm.nnsman.novelreader.data.BookRepository

class VP2RankingViewModel(
    private val bookRepository: BookRepository,
    private val stateHandle: SavedStateHandle
) : ViewModel() {
    fun getBooksByType(type: Int) = bookRepository.getBooksByType(type).cachedIn(viewModelScope)
}