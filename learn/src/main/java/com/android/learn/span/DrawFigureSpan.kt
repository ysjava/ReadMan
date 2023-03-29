package com.android.learn.span

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.style.ReplacementSpan
import android.util.Log

class DrawFigureSpan : ReplacementSpan() {
    private val figurePaint: Paint = Paint()
    private var figureTextPaint: Paint = Paint()
    private val figureWidth = 78
    private val figureTextSize = 36
    init {
        figurePaint.style = Paint.Style.STROKE
        figurePaint.color = Color.RED
        figurePaint.strokeWidth = 3f

        figureTextPaint.style = Paint.Style.FILL
        figureTextPaint.color = Color.RED
        figureTextPaint.textSize = figureTextSize.toFloat()

    }

    override fun getSize(
        paint: Paint,
        text: CharSequence?,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int {
        return figureWidth
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence?,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        val left = x + 10
        val right = left + figureWidth
        val z1 = left + figureWidth / 2
        val w = figureTextPaint.measureText("999")
        val h = -figureTextPaint.fontMetricsInt.ascent
        val rect = Rect(left.toInt(), top + 20, right.toInt(), bottom - 20)

        val tx = z1 - w / 2f
        val ty = rect.centerY() + h / 2f - figurePaint.strokeWidth

        canvas.drawRect(rect, figurePaint)
        canvas.drawText("999", tx, ty, figureTextPaint)
    }
}