package com.learndm.nnsman.novelreader.ui.read

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.learndm.nnsman.novelreader.data.BookRepository
import com.learndm.nnsman.novelreader.ui.search.SearchBookViewModel

class ReadFactory(owner: SavedStateRegistryOwner, private val bookRepository: BookRepository): AbstractSavedStateViewModelFactory(owner, null) {
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if (modelClass.isAssignableFrom(ReadViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReadViewModel(bookRepository,handle) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}