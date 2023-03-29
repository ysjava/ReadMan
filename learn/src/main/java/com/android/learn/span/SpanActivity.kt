package com.android.learn.span

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ImageSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.android.learn.R

class SpanActivity:AppCompatActivity(R.layout.activity_span) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val textview = findViewById<TextView>(R.id.textview)

        val spannableString = SpannableString("我要搞大钱，周周做新\n郎测周\n做新郎测试周\n做新郎测试周周做新郎测周做新郎测试周做新郎测试")
        //val span = RelativeSizeColorSpan(2f,Color.RED)
        val span = DrawFigureSpan()
        val drawable = ResourcesCompat.getDrawable(resources,R.drawable.h,null)!!
        drawable.setBounds(0,0,textview.lineHeight * 2,textview.lineHeight * 2)
//        val span = ImageSpan(drawable)
        spannableString.setSpan(span,spannableString.length-1,spannableString.length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(object : ClickableSpan(){
            override fun onClick(widget: View) {
                Log.d("qliwbrqf","点击了！")
            }

        },spannableString.length-1,spannableString.length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textview.text = spannableString
        textview.movementMethod = LinkMovementMethod.getInstance()
    }
}