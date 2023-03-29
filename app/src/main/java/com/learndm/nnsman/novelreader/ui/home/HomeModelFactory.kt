package com.learndm.nnsman.novelreader.ui.home

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import com.learndm.nnsman.novelreader.data.BookRepository

class HomeModelFactory(owner: SavedStateRegistryOwner, private val bookRepository: BookRepository) :
    AbstractSavedStateViewModelFactory(owner, null) {
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(bookRepository,handle) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}