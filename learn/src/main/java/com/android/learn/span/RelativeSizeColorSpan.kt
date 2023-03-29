package com.android.learn.span

import android.text.TextPaint
import android.text.style.RelativeSizeSpan


class RelativeSizeColorSpan(spanSize: Float, private val color: Int) :
    RelativeSizeSpan(spanSize) {
    override fun updateDrawState(textPaint: TextPaint) {
        super.updateDrawState(textPaint)
        textPaint.color = color
    }
}