package com.learndm.nnsman.utils

import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import com.learndm.nnsman.novelreader.data.BookRepository
import com.learndm.nnsman.novelreader.data.db.NovelReaderDatabase
import com.learndm.nnsman.novelreader.data.network.NovelReaderNetwork
import com.learndm.nnsman.novelreader.data.parse.BookStoreHtmlParse
import com.learndm.nnsman.novelreader.data.parse.QWYDParse
import com.learndm.nnsman.novelreader.ui.home.HomeModelFactory
import com.learndm.nnsman.novelreader.ui.home.ranking.VP2RankingFactory
import com.learndm.nnsman.novelreader.ui.read.ReadFactory
import com.learndm.nnsman.novelreader.ui.search.SearchBookFactory

object InjectorUtil {
    private val provideBookRepository =
        BookRepository.getInstance(
            NovelReaderDatabase.getInstance().bookDao(),
            NovelReaderNetwork.getInstance(),
            getBootHtmlParse()
        )

    private fun getBootHtmlParse(): BookStoreHtmlParse = QWYDParse(NovelReaderNetwork.getInstance())


    fun getHomeModelFactory(owner: SavedStateRegistryOwner): ViewModelProvider.Factory {
        return HomeModelFactory(owner, provideBookRepository)
    }

    fun getSearchBookFactory(owner: SavedStateRegistryOwner):ViewModelProvider.Factory{
        return SearchBookFactory(owner, provideBookRepository)
    }

    fun getVP2RankingFactory(owner: SavedStateRegistryOwner):ViewModelProvider.Factory{
        return VP2RankingFactory(owner, provideBookRepository)
    }

    fun getReadFactory(owner: SavedStateRegistryOwner):ViewModelProvider.Factory{
        return ReadFactory(owner, provideBookRepository)
    }
}