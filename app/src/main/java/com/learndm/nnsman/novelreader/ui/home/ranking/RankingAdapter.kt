package com.learndm.nnsman.novelreader.ui.home.ranking

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.learndm.nnsman.novelreader.data.parse.QWYDParse

class RankingAdapter(fragment: RankingFragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = QWYDParse.typeMap.size

    override fun createFragment(position: Int): Fragment {
        val fragment = VP2RankingFragment()
        fragment.arguments = Bundle().apply {
            putInt(VP2RankingFragment.ARGUMENTS_KEY_TYPE, position + 1)
        }
        return fragment
    }
}