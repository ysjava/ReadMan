package com.learndm.nnsman.novelreader.ui.home.ranking

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.learndm.nnsman.novelreader.data.BookRepository
import com.learndm.nnsman.novelreader.ui.home.HomeViewModel

class VP2RankingFactory(owner: SavedStateRegistryOwner, private val bookRepository: BookRepository): AbstractSavedStateViewModelFactory(owner, null) {
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if (modelClass.isAssignableFrom(VP2RankingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VP2RankingViewModel(bookRepository,handle) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}