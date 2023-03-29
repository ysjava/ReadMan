package com.learndm.nnsman.novelreader.ui.test

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.math.BigInteger

fun fibonacci(): Flow<BigInteger> = flow {
    var x = BigInteger.ZERO
    var y = BigInteger.ONE
    while (true) {
        emit(x)
        x = y.also {
            y += x
        }
    }

}

fun main() {
    val a = 5
    val b = 10f
    println("result = ${a/b}")
}

fun f1() = flow<Int> { emit(10) }
fun f2() = flow<Int> { emit(20) }
