package com.learndm.nnsman.novelreader.ui.read

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.learndm.nnsman.databinding.ItemBookTitleBinding
import com.learndm.nnsman.novelreader.data.model.ChapterTitle

class TitleAdapter(private val context: Context, private val dataList: ArrayList<ChapterTitle> = arrayListOf()) :
    RecyclerView.Adapter<TitleViewHolder>() {
    private var selectedIndex = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitleViewHolder =
        TitleViewHolder(
            context,
            ItemBookTitleBinding.inflate(LayoutInflater.from(context), parent, false)
        )

    override fun onBindViewHolder(holder: TitleViewHolder, position: Int) {
        val title = dataList[position]

        holder.bind(position == selectedIndex, title.title)
        holder.setClickListener {
            itemClickListener?.invoke(position, title.title)
            selectedItem(position)
        }
    }

    override fun getItemCount(): Int = dataList.size

    fun updateAll(dataList: ArrayList<ChapterTitle>){
        this.dataList.clear()
        this.dataList.addAll(dataList)
        notifyDataSetChanged()
    }

    fun selectedItem(position: Int){
        val oldSelectedIndex = selectedIndex
        selectedIndex = -1
        notifyItemChanged(oldSelectedIndex)
        selectedIndex = position
        notifyItemChanged(position)
    }

    var itemClickListener: ((position: Int, title: String) -> Unit)? = null
}