package com.learndm.nnsman.novelreader.ui.read

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.learndm.nnsman.databinding.ItemReadSettingBgBinding
import com.learndm.nnsman.novelreader.ui.flyer.ReadTheme

class ReadBgAdapter(private val context: Context, private val dataList: List<ReadTheme>) :
    RecyclerView.Adapter<ViewHolder>() {
    private var selectedIndex = 5
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemReadSettingBgBinding.inflate(LayoutInflater.from(context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val theme = dataList[position]

        holder.bind(position == selectedIndex, theme)
        holder.setClickListener {
            if (position == selectedIndex) return@setClickListener
            selectedIndex = holder.absoluteAdapterPosition
            notifyDataSetChanged()
            itemClickListener?.invoke(theme)
        }
    }

    override fun getItemCount(): Int = dataList.size

    var itemClickListener: ((theme: ReadTheme) -> Unit)? = null
}