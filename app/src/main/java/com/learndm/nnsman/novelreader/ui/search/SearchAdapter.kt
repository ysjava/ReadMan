package com.learndm.nnsman.novelreader.ui.search

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.learndm.nnsman.databinding.ItemBookBinding
import com.learndm.nnsman.novelreader.data.model.Book
import com.learndm.nnsman.novelreader.ui.read.ReadActivity

class SearchAdapter(private val context: Context) :
    PagingDataAdapter<Book, SearchViewHolder>(SEARCH_DIFF_CALLBACK) {

    companion object {
        private val SEARCH_DIFF_CALLBACK = object : DiffUtil.ItemCallback<Book>() {
            override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean =
                oldItem.url == newItem.url

            override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean =
                oldItem.name == newItem.name && oldItem.author == newItem.author && oldItem.coverUrl == newItem.coverUrl && oldItem.desc == newItem.desc
        }
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val book = getItem(position)
        if (book != null) {
            holder.bind(book)

            holder.setClickListener {
                val intent = Intent(context,ReadActivity::class.java).apply {
                    putExtra("bookurl",book.url)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder =
        SearchViewHolder(
            context, ItemBookBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
}