package com.learndm.nnsman.novelreader.ui.flyer

import android.graphics.Bitmap
import com.learndm.nnsman.novelreader.data.model.Chapter

interface DataProcessor {

    val readerView: ReaderView
    val dataSource: DataSource
    val chapterChangeListener: OnChapterChangeListener?
        

    fun fillNextData(bitmap: Bitmap)
    fun fillLastData(bitmap: Bitmap)
    fun fillCurrentData(bitmap: Bitmap)

    fun hasNextPage(): Boolean
    fun hasLastPage(): Boolean

    fun updateChapters(chapterList: List<Chapter>)

    fun skipChapter(chapterNumber: Int)
    fun skipLastChapter(): Boolean
    fun skipNextChapter(): Boolean

    fun refreshDataToCurrentBitmap()

}

interface OnChapterChangeListener {
    fun change(chapterNumber: Int)
}