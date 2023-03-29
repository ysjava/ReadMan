package com.learndm.nnsman.utils

import android.content.Context
import android.os.Build
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import com.learndm.nnsman.App

fun sp2px(sp: Int, context: Context): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        sp.toFloat(),
        context.resources.displayMetrics
    )
}

fun dp2px(dp: Int, context: Context): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        context.resources.displayMetrics
    )
}

fun View.dp2px(dp: Int): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        context.resources.displayMetrics
    )
}

fun View.sp2px(sp: Int): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        sp.toFloat(),
        context.resources.displayMetrics
    )
}

fun screenWidth(context: Context = App.context): Int {
    val dm = context.resources.displayMetrics
    return dm.widthPixels
}

fun screenHeight(context: Context = App.context): Int {
    val dm = context.resources.displayMetrics
    return dm.heightPixels
}

fun statusBarHeight(context: Context = App.context): Int {
    var height = 0
    val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        height = context.resources.getDimensionPixelSize(resourceId)
    }
    return height
}

/**
 * @param real: 计算真实显示区域,是否包括状态栏等系统装饰区域
 * */
fun screenHeight(context: Context = App.context, real: Boolean): Int {
    val height: Int = if (!real) {
        val dm = context.resources.displayMetrics
        dm.heightPixels
    } else {
        val vm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
            vm.currentWindowMetrics.bounds.height()
        } else {
            val outMetrics = DisplayMetrics()
            vm.defaultDisplay.getRealMetrics(outMetrics)
            outMetrics.heightPixels
        }
    }

    return height
}

/**
 * 获取系统亮度
 * @return 0-255
 * */
fun getSystemBrightness(context: Context): Int {
    var systemBrightness = 0
    try {
        systemBrightness =
            Settings.System.getInt(context.contentResolver, Settings.System.SCREEN_BRIGHTNESS)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return systemBrightness
}

/**
 * 获取系统最大亮度
 * @return 0-255
 * */
fun getSystemMaxBrightness(context: Context): Int {
    return context.resources.getIdentifier(
        "config_screenBrightnessSettingMaximum",
        "integer",
        "android"
    )
}