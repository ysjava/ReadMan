package com.learndm.nnsman.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes

fun ImageView.setTint(@ColorRes color:Int){
    drawable.setTint(resources.getColor(color,null))
}
