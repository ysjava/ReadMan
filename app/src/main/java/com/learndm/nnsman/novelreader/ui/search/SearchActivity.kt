package com.learndm.nnsman.novelreader.ui.search

import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.gyf.immersionbar.ImmersionBar
import com.learndm.nnsman.databinding.ActivitySearchBookBinding
import com.learndm.nnsman.utils.InjectorUtil
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            InjectorUtil.getSearchBookFactory(this)
        )[SearchBookViewModel::class.java]
    }

    private lateinit var adapter: SearchAdapter
    private lateinit var binding: ActivitySearchBookBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySearchBookBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        val adapter = SearchAdapter(this)
        this.adapter = adapter
        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(this)

        binding.editSearchBook.setOnEditorActionListener { v, actionId, event ->
            val keyword = v.text.toString().trim()
            if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED && keyword.isNotEmpty() && event.action == KeyEvent.ACTION_UP) {
                executeSearch(keyword)
            }
            true
        }
    }

    private fun executeSearch(keyword:String){
        lifecycleScope.launch {
            viewModel.searchBookByKeyword(keyword).collect{
                adapter.submitData(it)
            }
        }
    }
}