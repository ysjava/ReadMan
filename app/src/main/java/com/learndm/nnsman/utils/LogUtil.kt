package com.learndm.nnsman.utils

import android.content.Context
import android.util.Log
import android.widget.Toast

private const val UTIL_TAG = "UTIL_TAG"
fun logDebug(msg: String, tag: String? = UTIL_TAG) {
    Log.d(tag, msg)
}

fun toast(context: Context,str:String){
    Toast.makeText(context,str,Toast.LENGTH_SHORT).show()
}