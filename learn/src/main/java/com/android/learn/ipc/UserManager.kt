package com.android.learn.ipc

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlin.math.log

class UserManager {
    companion object{
        var id = 1
        @RequiresApi(Build.VERSION_CODES.P)
        get() {
            Log.d("uqgkweqwe","progress name: ${Application.getProcessName()}")
            return field
        }
    }
}