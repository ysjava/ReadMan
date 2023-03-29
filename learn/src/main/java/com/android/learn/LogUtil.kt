package com.android.learn

import android.util.Log

fun logd(str: String,tag: String = "LogUtil"){
    Log.d(tag,"${Thread.currentThread().name}: $str")
}