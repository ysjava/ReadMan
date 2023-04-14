package com.learndm.nnsman.novelreader.ui.flyer

interface DataSource {
    fun getChapterData(chapterNumbers: IntArray)
}