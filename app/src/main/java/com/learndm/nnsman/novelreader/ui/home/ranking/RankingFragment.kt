package com.learndm.nnsman.novelreader.ui.home.ranking

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.learndm.nnsman.databinding.FragmentRankingBinding
import com.learndm.nnsman.novelreader.data.parse.QWYDParse
import com.learndm.nnsman.novelreader.ui.read.ReadActivity

class RankingFragment : Fragment() {
    private lateinit var binding: FragmentRankingBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRankingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            it.getString()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewPager.adapter = RankingAdapter(this)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = getTabTextByPosition(position)
        }.attach()

    }

    private fun getTabTextByPosition(position: Int): String {
        return QWYDParse.typeMap[position + 1]
            ?: throw IllegalArgumentException("position is out of bounds, range is 1-6,current position is ${position + 1} ")

    }

}