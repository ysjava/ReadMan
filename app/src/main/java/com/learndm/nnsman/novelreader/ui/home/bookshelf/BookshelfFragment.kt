package com.learndm.nnsman.novelreader.ui.home.bookshelf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.learndm.nnsman.databinding.FragmentBookshelfBinding

class BookshelfFragment : Fragment() {
    private lateinit var binding: FragmentBookshelfBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookshelfBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.button.setOnClickListener {
            val action = BookshelfFragmentDirections.actionNavigationBookshelfToSearchActivity()
            findNavController().navigate(action)
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
    }
}