package com.learndm.nnsman.novelreader.ui.flyer

import androidx.annotation.ColorRes
import com.learndm.nnsman.R

data class ReadTheme(
    @ColorRes val bgColor: Int,
    @ColorRes val toolBarContentColor: Int,
    @ColorRes val toolBarBgColor: Int,
    @ColorRes val subtitleTextColor: Int = R.color.black_200,
    @ColorRes val contentTextColor: Int = R.color.black_200
)
