package com.learndm.nnsman.novelreader.ui.test

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import com.learndm.nnsman.R
import com.learndm.nnsman.databinding.DialogTestBinding
import com.learndm.nnsman.utils.*
import com.lxj.xpopup.XPopup
import org.w3c.dom.Text
import kotlin.math.max

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        ImmersionBar.with(this).statusBarDarkFont(true).init()

        val button = findViewById<Button>(R.id.button)
        val sun = findViewById<View>(R.id.sun)

//        val blay =
//        dialog.addContentView(blay,dialogLayoutParams)


        button.setOnClickListener {
            sun.background.setTint(getColor(R.color.color9pp))
        }
    }

    class Adapter():RecyclerView.Adapter<Adapter.ViewHolder>(){

        class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
            val title = itemView.findViewById<TextView>(R.id.tv_title)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_book_title,parent,false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.title.text = "当前位置: $position"
        }

        override fun getItemCount(): Int {
            return 30
        }
    }
}