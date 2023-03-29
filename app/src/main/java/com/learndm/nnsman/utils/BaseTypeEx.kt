package com.learndm.nnsman.utils

import androidx.annotation.ColorInt

fun Boolean.pt(ifTrue: () -> Unit, ifFalse: (() -> Unit)? = null) {
    if (this) ifTrue()
    else ifFalse?.invoke()
}


