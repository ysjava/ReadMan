package com.learndm.nnsman.novelreader.ui.home

import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.gyf.immersionbar.ImmersionBar
import com.learndm.nnsman.R
import com.learndm.nnsman.databinding.ActivityHomeBinding
import com.learndm.nnsman.novelreader.ui.search.SearchAdapter
import com.learndm.nnsman.utils.InjectorUtil

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val viewModel by viewModels<HomeViewModel>(
        factoryProducer = { InjectorUtil.getHomeModelFactory(this) }
    )
    private lateinit var adapter: SearchAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(ev)
    }
}