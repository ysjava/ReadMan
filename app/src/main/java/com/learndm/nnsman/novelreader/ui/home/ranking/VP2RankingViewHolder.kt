package com.learndm.nnsman.novelreader.ui.home.ranking

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.learndm.nnsman.databinding.ItemBookBinding
import com.learndm.nnsman.novelreader.data.model.Book

class VP2RankingViewHolder(private val context: Context, private val binding: ItemBookBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(book: Book) {
        binding.apply {
            Glide.with(context).load(book.coverUrl).into(ivBookCover)
            tvAuthor.text = book.author
            tvDesc.text = book.desc
            tvBookName.text = book.name
        }
    }

    fun setClickListener(listener: View.OnClickListener) {
        binding.root.setOnClickListener(listener)
    }
}