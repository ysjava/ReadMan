package com.learndm.nnsman

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
//        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
//        assertEquals("com.learndm.nnsman", appContext.packageName)
        val sequence = sequenceOf(1, 2, 3, 4)
        val result: Sequence<Int> = sequence
            .map { i ->
                println("Map $i")
                i * 2
            }
            .filter { i ->
                println("Filter $i")
                i % 3  == 0
            }

        println(result.last()) // 👈 只取集合的第一个元素
    }
}