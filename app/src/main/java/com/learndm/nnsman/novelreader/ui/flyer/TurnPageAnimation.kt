package com.learndm.nnsman.novelreader.ui.flyer

import android.graphics.Canvas
import android.view.MotionEvent

interface TurnPageAnimation {
    val readerView: ReaderView
    val listener: TurnPageListener

    fun draw(canvas: Canvas)
    fun onTouchEvent(event: MotionEvent): Boolean
    fun computeScroll()
}