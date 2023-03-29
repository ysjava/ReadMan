package com.android.learn.ipc

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.learn.R

class IpcTest2Activity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ipc_test2)
        Log.d("uqgkweqwe","id2:${UserManager.id}")
        startActivity(Intent(this,IpcTest3Activity::class.java))
    }
}