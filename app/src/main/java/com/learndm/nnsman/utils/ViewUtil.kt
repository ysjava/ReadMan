package com.learndm.nnsman.utils

import android.view.View
import kotlin.math.min

fun measureSize(defaultSize: Int, spec: Int): Int {
    var result = defaultSize
    val model = View.MeasureSpec.getMode(spec)
    val size = View.MeasureSpec.getSize(spec)
    if (model == View.MeasureSpec.EXACTLY) {
        result = size
    } else if (model == View.MeasureSpec.AT_MOST) {
        result = min(defaultSize, size)
    }

    return result
}