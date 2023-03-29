package com.learndm.nnsman.novelreader.ui.read

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.learndm.nnsman.databinding.ItemBookTitleBinding

class TitleViewHolder(private val context: Context, private val binding: ItemBookTitleBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(isSelected: Boolean, title: String) {
        binding.apply {
            tvTitle.text = title
            tvTitle.setTextColor(if (isSelected) Color.RED else Color.BLACK)
        }
    }

    fun setClickListener(listener: View.OnClickListener) {
        binding.root.setOnClickListener(listener)
    }
}