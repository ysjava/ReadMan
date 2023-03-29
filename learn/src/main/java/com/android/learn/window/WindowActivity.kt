package com.android.learn.window

import android.graphics.PixelFormat
import android.os.Bundle
import android.view.Gravity
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.android.learn.R
import android.view.WindowManager.LayoutParams
class WindowActivity:AppCompatActivity(R.layout.activity_window) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val btn = Button(this)
        btn.text = "TEST"
        val layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
        LayoutParams.WRAP_CONTENT,99,0,PixelFormat.TRANSPARENT)
        layoutParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE or LayoutParams.FLAG_NOT_TOUCH_MODAL
        layoutParams.gravity = Gravity.TOP or Gravity.START
        layoutParams.apply {
            x = 100
            y = 300
        }

        windowManager.addView(btn,layoutParams)

        btn.setOnTouchListener { v, event ->
            val rawX = event.rawX
            val rawY = event.rawY
            when(event.action){
                MotionEvent.ACTION_MOVE -> {
                    layoutParams.x = rawX.toInt()
                    layoutParams.y = rawY.toInt()
                    windowManager.updateViewLayout(btn,layoutParams)
                }
            }
            false
        }
    }
}