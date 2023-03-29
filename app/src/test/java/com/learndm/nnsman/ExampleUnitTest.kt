package com.learndm.nnsman

import android.util.Log
import androidx.constraintlayout.utils.widget.MockView
import com.learndm.nnsman.novelreader.data.BookRepository
import com.learndm.nnsman.novelreader.data.db.NovelReaderDatabase
import com.learndm.nnsman.novelreader.data.model.Book
import com.learndm.nnsman.novelreader.data.network.NovelReaderNetwork
import com.learndm.nnsman.utils.InjectorUtil
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.junit.Test

import org.junit.Assert.*
import java.math.BigInteger
import kotlin.concurrent.thread

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {


    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun addition_isCorrect() {
        runBlocking {
            launch(Dispatchers.Unconfined) {
                flowOf(1, 2, 3, 4, 5, 6, 7, 8)
                    .map {
                        println("map1 ${currentCoroutineContext()[CoroutineDispatcher]}")
                        it
                    }
                    .flowOn(Dispatchers.IO)
                    .map {
                        println("map2 ${currentCoroutineContext()[CoroutineDispatcher]}")
                        it
                    }
                    .flowOn(Dispatchers.Default)
                    .collect {
                        println("${currentCoroutineContext()[CoroutineDispatcher]} $it")
                    }
            }


        }

    }


}
