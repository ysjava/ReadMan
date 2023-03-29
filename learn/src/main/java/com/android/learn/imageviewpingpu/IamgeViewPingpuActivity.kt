package com.android.learn.imageviewpingpu

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.ImageView
import com.android.learn.R
import com.android.learn.logd

class IamgeViewPingpuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iamge_view_pingpu)


//        val imageview = findViewById<ImageView>(R.id.imageview)
        val scrollingImageView = findViewById<ScrollingImageView>(R.id.scrollImageView)
        scrollingImageView.startScroll(R.drawable.down64)
        var lastX = -1f
        var lastY = -1f

//        imageview.setOnTouchListener { v, event ->
//            val x = event.x
//            val y = event.y
//
//            when (event.action) {
//                MotionEvent.ACTION_DOWN -> {
//
//                }
//                MotionEvent.ACTION_MOVE -> {
//                    val dy = y - lastY
//                    if (dy > 0) {
//                        //手指下滑
//                        logd("手指下滑111","akyvgfq")
//                        v.scrollBy(0, -dy.toInt())
//                        imageview.setImageResource(R.drawable.down64)
//                    } else {
//                        //手指上滑
//                        logd("手指上滑222","akyvgfq")
//                        imageview.setImageResource(R.drawable.up64)
//                        v.scrollBy(0, -dy.toInt())
//                    }
//                }
//            }
//            lastY = y
//            true
//        }
    }
}