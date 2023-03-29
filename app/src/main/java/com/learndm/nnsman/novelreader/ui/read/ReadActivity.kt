package com.learndm.nnsman.novelreader.ui.read

import android.animation.ObjectAnimator
import android.graphics.PointF
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import com.learndm.nnsman.R
import com.learndm.nnsman.databinding.ActivityReadBinding
import com.learndm.nnsman.databinding.CellReadBottomToolBarBinding
import com.learndm.nnsman.databinding.CellReadSettingBinding
import com.learndm.nnsman.novelreader.data.model.ChapterTitle
import com.learndm.nnsman.novelreader.ui.flyer.*
import com.learndm.nnsman.utils.*
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.enums.PopupAnimation
import com.lxj.xpopup.interfaces.SimpleCallback

class ReadActivity : AppCompatActivity() {
    private val args: ReadActivityArgs by navArgs()
    private lateinit var appbar: AppBarLayout
    private lateinit var bottomBar: CellReadBottomToolBarBinding
    private lateinit var settingBar: CellReadSettingBinding
    private lateinit var readerView: ReaderView
    private lateinit var binding: ActivityReadBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var recyclerView: RecyclerView
    private val viewModel by viewModels<ReadViewModel>(
        factoryProducer = {
            InjectorUtil.getReadFactory(this)
        })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_BAR).init()
        initData()
        initView()
        observe()
    }

    private fun initData() {
        //初始化阅读主题色
        viewModel.initReadTheme()

        val bookUrl = intent.getStringExtra("bookurl") ?: args.bookurl
        viewModel.getDetailBookInfo(bookUrl)
    }

    private fun initView() {
        readerView = binding.readrView
        appbar = binding.appbar
        bottomBar = binding.readBottomToolBar
        settingBar = binding.readSettingBar
        drawerLayout = binding.drawerLayout
        recyclerView = binding.recyclerView

        val setBgAdapter = ReadBgAdapter(this, viewModel.readThemes)
        setBgAdapter.itemClickListener = {
            switchReadTheme(it)
        }
        settingBar.listBg.adapter = setBgAdapter

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        binding.layDrawLeft.setPadding(0, statusBarHeight(this), 0, 0)
        appbar.setPadding(0, statusBarHeight(this), 0, 0)

        val adapter = TitleAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter.itemClickListener = { position, _ ->
            readerView.skipChapter(position + 1)
            hideMenuBar()
            drawerLayout.closeDrawer(GravityCompat.START)

            bottomBar.seekBar.progress = position + 1
        }

        readerView.setReadTheme(viewModel.readThemes[5])
        switchReadTheme(viewModel.readThemes[5])

        readerView.dataSource = object : DataSource {
            override fun getChapterData(chapterNumbers: IntArray) {
                viewModel.getChapters(chapterNumbers)
            }
        }
        readerView.chapterChangeListener = object : OnChapterChangeListener {
            override fun change(chapterNumber: Int) {
                bottomBar.seekBar.progress = chapterNumber
                (recyclerView.adapter as TitleAdapter).selectedItem(chapterNumber - 1)
            }

        }
        readerView.touchListener = object : OnReaderViewTouchListener {
            override fun onCenterClick(readerView: ReaderView) {
                if (showingSettingBar)
                    hideSettingBar()
                else
                    toggleMenuBar()
            }

            override fun canMove(): Boolean {
                return viewModel.isFullScreen
            }

            override fun resumeMovement() {
                hideMenuBar()
                hideSettingBar()
            }
        }

        settingBar.apply {
            tvFontSizeMinus.setOnClickListener {
                val curFontSize = tvFontNumber.text.toString().toInt()
                if (curFontSize - 1 < 12) return@setOnClickListener
                readerView.setContentSize(curFontSize - 1)
                "${curFontSize - 1}".also { tvFontNumber.text = it }
            }
            tvFontSizeAdd.setOnClickListener {
                val curFontSize = tvFontNumber.text.toString().toInt()
                if (curFontSize + 1 > 33) return@setOnClickListener
                readerView.setContentSize(curFontSize + 1)
                "${curFontSize + 1}".also { tvFontNumber.text = it }
            }

            //小米系统获取的系统亮度区间是0-4095, 测试机是小米,所以直接设置了,这儿应该对不同系统做处理
            seekBar.max = 4095
            seekBar.progress = getSystemBrightness(this@ReadActivity)
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if(!fromUser) return

                    val wl:WindowManager.LayoutParams = window.attributes
                    wl.screenBrightness = progress / 4095f
                    window.attributes = wl
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {

                }

            })
        }

        bottomBar.apply {
            tvDateModel.setOnClickListener {
                //夜间日间模式切换
            }
            tvCatalogue.setOnClickListener {
                //目录
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
            tvChapterComment.setOnClickListener {
                //章评
            }
            tvSetting.setOnClickListener {
                //设置
                showSettingBar()
            }
            tvLastChapter.setOnClickListener {
                //上一章
                readerView.skipLastChapter()
            }
            tvNextChapter.setOnClickListener {
                //下一章
                readerView.skipNextChapter()
            }

            var prog = 0
            val dialog = XPopup.Builder(this@ReadActivity)
                .atPoint(PointF(screenWidth() / 2f, screenHeight() * 3f / 4f))
                .hasShadowBg(false)
                .isClickThrough(true)
                .isTouchThrough(true)
                .isLightStatusBar(true)
                .dismissOnTouchOutside(false)
                .isCenterHorizontal(true)
                .offsetY(100)
                .popupAnimation(PopupAnimation.ScaleAlphaFromCenter)
                .setPopupCallback(object : SimpleCallback() {
                    override fun beforeShow(popupView: BasePopupView) {
                        loadDialogData(popupView, prog)
                    }
                })
                .asCustom(CustomAttachPopup(this@ReadActivity))

            var isTouchSeekBar = false

            bottomBar.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (!isTouchSeekBar) return
                    prog = progress
                    loadDialogData(dialog, progress)
                    dialog.show()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                    isTouchSeekBar = true
                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    readerView.skipChapter(seekBar.progress)
                    (recyclerView.adapter as TitleAdapter).selectedItem(seekBar.progress - 1)
                    recyclerView.scrollToPosition(seekBar.progress - 1)
                    dialog.delayDismiss(3000)
                    isTouchSeekBar = false
                }
            })
        }
    }

    private fun switchReadTheme(theme: ReadTheme) {
        val toolbarBgColor = getColor(theme.toolBarBgColor)
        val toolbarContentColor = getColor(theme.toolBarContentColor)
        //
        appbar.background.setTint(toolbarBgColor)

        bottomBar.apply {
            root.background.setTint(toolbarBgColor)
            tvLastChapter.setTextColor(toolbarContentColor)
            tvNextChapter.setTextColor(toolbarContentColor)
            seekBar.progressDrawable.setTint(toolbarContentColor)
            tvCatalogue.setTextColor(toolbarContentColor)
            tvCatalogue.compoundDrawables.forEach {
                it?.setTint(toolbarContentColor)
            }
            tvChapterComment.setTextColor(toolbarContentColor)
            tvChapterComment.compoundDrawables.forEach {
                it?.setTint(toolbarContentColor)
            }
            tvDateModel.setTextColor(toolbarContentColor)
            tvDateModel.compoundDrawables.forEach {
                it?.setTint(toolbarContentColor)
            }
            tvSetting.setTextColor(toolbarContentColor)
            tvSetting.compoundDrawables.forEach {
                it?.setTint(toolbarContentColor)
            }
        }

        settingBar.apply {
            root.background.setTint(toolbarBgColor)
            tvLuminance.setTextColor(toolbarContentColor)
            tvFontSize.setTextColor(toolbarContentColor)
            tvTurn.setTextColor(toolbarContentColor)
            tvReadBg.setTextColor(toolbarContentColor)
            tvFontNumber.setTextColor(toolbarContentColor)
            seekBar.progressDrawable.setTint(toolbarContentColor)
            ivSun1.drawable.setTint(toolbarContentColor)
            ivSun2.drawable.setTint(toolbarContentColor)

            rbAnimationNot.setTextColor(toolbarContentColor)
            rbAnimationOver.setTextColor(toolbarContentColor)
            rbAnimationScroll.setTextColor(toolbarContentColor)
            rbAnimationSimulation.setTextColor(toolbarContentColor)

            tvFontSizeMinus.setTextColor(toolbarContentColor)
            tvFontSizeAdd.setTextColor(toolbarContentColor)
        }

        readerView.setReadTheme(theme)
    }

    private fun loadDialogData(popupView: BasePopupView, process: Int) {
        val book = viewModel.book.value!!
        val tv = popupView.popupImplView?.findViewById<TextView>(R.id.tv_test_progress)
        val title = book.chapterTitles?.get(process - 1)?.title
        "跳至 $title".also { tv?.text = it }
    }

    private fun observe() {
        viewModel.book.observe(this) {
            bottomBar.seekBar.max = it.chapterNumber
            //暂时先默认从第一章开始阅读
            bottomBar.seekBar.progress = 1
            readerView.book = it

            val titles = arrayListOf<ChapterTitle>()
            it.chapterTitles?.let { ts ->
                titles.addAll(ts)
            }

            (recyclerView.adapter as TitleAdapter).updateAll(titles)
        }
        viewModel.chapters.observe(this) {
            readerView.updateChapters(it)
        }
    }

    private fun hideMenuBar() {
        appbar.animate().translationY(-appbar.height.toFloat())
        bottomBar.root.animate().translationY(bottomBar.root.height.toFloat())
        ImmersionBar.with(this@ReadActivity).hideBar(BarHide.FLAG_HIDE_BAR)
            .init()
        viewModel.isFullScreen = true
    }

    private fun showMenuBar() {
        appbar.visibility = View.VISIBLE
        bottomBar.root.visibility = View.VISIBLE
        ImmersionBar.with(this@ReadActivity).hideBar(BarHide.FLAG_SHOW_BAR)
            .statusBarDarkFont(true)
            .init()

        ObjectAnimator.ofFloat(appbar, "translationY", -appbar.height.toFloat(), 0f).start()
        ObjectAnimator.ofFloat(bottomBar.root, "translationY", bottomBar.root.height.toFloat(), 0f)
            .start()
        viewModel.isFullScreen = false
    }

    private var showingSettingBar = false
    private fun showSettingBar() {
        //先隐藏菜单栏
        hideMenuBar()
        settingBar.root.visibility = View.VISIBLE
        ObjectAnimator.ofFloat(
            settingBar.root,
            "translationY",
            settingBar.root.height.toFloat(),
            0f
        ).start()
        viewModel.isFullScreen = false
        showingSettingBar = true
    }

    private fun hideSettingBar() {
        settingBar.root.animate().translationY(settingBar.root.height.toFloat())
        viewModel.isFullScreen = true
        showingSettingBar = false
    }

    fun toggleMenuBar() {
        if (viewModel.isFullScreen)
            showMenuBar()
        else
            hideMenuBar()
    }
}