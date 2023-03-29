package com.learndm.nnsman.novelreader.ui.flyer

import android.graphics.Canvas
import android.view.MotionEvent

abstract class TurnPageAnimation{
    abstract val readerView: ReaderView
    abstract val listener:TurnPageListener
    abstract fun draw(canvas: Canvas)

    abstract fun onTouchEvent(event: MotionEvent):Boolean
    abstract fun computeScroll()
}