package com.learndm.nnsman.utils

import android.content.Context
import android.content.res.Resources
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.util.*

fun ViewModel.launch(block: suspend () -> Unit, error: ((Exception) -> Unit)? = null) {
    this.viewModelScope.launch {
        try {
            block()
        } catch (e: Exception) {
            if (error != null) {
                error(e)
            } else {
                //unified processing
            }
        }
    }
}

fun delayTask(delay: Long = 3000, block: () -> Unit) {
    Timer().schedule(object : TimerTask() {
        override fun run() {
            block()
        }
    }, delay)
}

@ColorInt
fun Context.getColor(@ColorRes colorId: Int, theme: Resources.Theme? = null): Int {
    return resources.getColor(colorId, theme)
}
