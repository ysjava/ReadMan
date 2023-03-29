package com.learndm.nnsman.novelreader.ui.flyer

import android.graphics.Bitmap

interface TurnPageListener {
    fun fillNextData(bitmap: Bitmap)
    fun fillLastData(bitmap: Bitmap)
    fun fillCurrentData(bitmap: Bitmap)
    fun hasNextPage():Boolean
    fun hasLastPage():Boolean
}