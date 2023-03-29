package com.learndm.nnsman.novelreader.ui.home.ranking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.learndm.nnsman.databinding.FragmentVp2RankingBinding
import com.learndm.nnsman.utils.InjectorUtil
import kotlinx.coroutines.launch

class VP2RankingFragment : Fragment() {

    companion object {
        const val ARGUMENTS_KEY_TYPE = "type"
    }

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            InjectorUtil.getVP2RankingFactory(this)
        )[VP2RankingViewModel::class.java]
    }
    private lateinit var binding: FragmentVp2RankingBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVp2RankingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = VP2Adapter(requireContext())
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        arguments?.getInt(ARGUMENTS_KEY_TYPE)?.let {
            adapter.addLoadStateListener {
                when (it.refresh) {
                    is LoadState.NotLoading -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.recyclerView.visibility = View.VISIBLE
                    }
                    is LoadState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.recyclerView.visibility = View.INVISIBLE
                    }
                    is LoadState.Error -> {
                        val state = it.refresh as LoadState.Error
                        binding.progressBar.visibility = View.INVISIBLE
                        Toast.makeText(requireContext(), "Load Error: ${state.error.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            lifecycleScope.launch {
                viewModel.getBooksByType(it).collect {
                    adapter.submitData(it)
                }
            }
        }
    }
}