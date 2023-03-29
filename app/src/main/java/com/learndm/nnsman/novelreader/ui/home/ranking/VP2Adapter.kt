package com.learndm.nnsman.novelreader.ui.home.ranking

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.learndm.nnsman.databinding.ItemBookBinding
import com.learndm.nnsman.novelreader.data.model.Book

class VP2Adapter(private val context: Context) :
    PagingDataAdapter<Book, VP2RankingViewHolder>(SEARCH_DIFF_CALLBACK) {

    companion object {
        private val SEARCH_DIFF_CALLBACK = object : DiffUtil.ItemCallback<Book>() {
            override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean =
                oldItem.url == newItem.url

            override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean =
                oldItem.name == newItem.name && oldItem.author == newItem.author && oldItem.coverUrl == newItem.coverUrl && oldItem.desc == newItem.desc
        }
    }

    override fun onBindViewHolder(holder: VP2RankingViewHolder, position: Int) {
        val book = getItem(position)
        if (book != null) {
            holder.bind(book)

            holder.setClickListener {
                val action = RankingFragmentDirections.actionNavigationRankingToReadActivity(book.url)
                holder.itemView.findNavController().navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VP2RankingViewHolder =
        VP2RankingViewHolder(
            context, ItemBookBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
}