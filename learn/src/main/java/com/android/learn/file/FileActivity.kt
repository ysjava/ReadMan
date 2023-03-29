package com.android.learn.file

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.android.learn.R
import java.io.*

class FileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file)

        Log.d("FileActivity", "内部存储")
        //filesDir: /data/user/0/com.android.learn/files
        Log.d("FileActivity", "filesDir: $filesDir")
        //cacheDir: /data/user/0/com.android.learn/cache
        Log.d("FileActivity", "cacheDir: $cacheDir")
        //codeCacheDir: /data/user/0/com.android.learn/code_cache
        Log.d("FileActivity", "codeCacheDir: $codeCacheDir")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //dataDir: /data/user/0/com.android.learn
            Log.d("FileActivity", "dataDir: $dataDir")
        }

        Log.d("FileActivity", "外部存储")
        //ExternalStorageDirectory: /storage/emulated/0
        Log.d(
            "FileActivity",
            "ExternalStorageDirectory: ${Environment.getExternalStorageDirectory()}"
        )
        //ExternalStoragePublicDirectory: /storage/emulated/0/Music
        Log.d(
            "FileActivity",
            "ExternalStoragePublicDirectory: ${
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).path
            }"
        )
        //ExternalStorageState: mounted
        Log.d("FileActivity", "ExternalStorageState: ${Environment.getExternalStorageState()}")

        Log.d("FileActivity", "外部存储私有目录")
        //externalCacheDir: /storage/emulated/0/Android/data/com.android.learn/cache
        Log.d("FileActivity", "externalCacheDir: $externalCacheDir")
        //externalCacheDirs: [Ljava.io.File;@984c7bf
        Log.d("FileActivity", "externalCacheDirs: $externalCacheDirs")

        //写"2022减肥成功"进一个文件中
        val str = "2022减肥成功"

        val ba = str.toByteArray()
        var fos: FileOutputStream? = null
        var oos: ObjectOutputStream? = null
        val file = File("$filesDir/2022target.txt")
        val file2 = File("$filesDir/YangSong2022target")

        try {
            fos = FileOutputStream(file)
            fos.write(ba, 0, ba.size)

            oos = ObjectOutputStream(FileOutputStream(file2))
            oos.writeObject(YangSong("杨崧", 1, 24, 69.99f, 90f))
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            fos?.close()
            oos?.close()
        }

        val name = findViewById<TextView>(R.id.name)
        val sex = findViewById<TextView>(R.id.sex)
        val age = findViewById<TextView>(R.id.age)
        val weight = findViewById<TextView>(R.id.weight)
        val appearance = findViewById<TextView>(R.id.appearance)

        findViewById<Button>(R.id.button).setOnClickListener {
            var ois: ObjectInputStream? = null
            var yangSong: YangSong? = null
            try {
                ois = ObjectInputStream(file2.inputStream())
                yangSong = ois.readObject() as YangSong
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                ois?.close()
            }

            yangSong?.let {
                name.text = String.format(getString(R.string.txt1), it.name)
                sex.text = String.format(getString(R.string.txt2), if (it.sex == 1) "男" else "女")
                age.text = String.format(getString(R.string.txt3), it.age)
                weight.text = String.format(getString(R.string.txt4), it.weight.toString())
                appearance.text = String.format(getString(R.string.txt5), it.appearance.toString())
            }
        }
    }

    data class YangSong(
        val name: String,
        val sex: Int,
        val age: Int,
        val weight: Float,
        val appearance: Float
    ) : Serializable
}