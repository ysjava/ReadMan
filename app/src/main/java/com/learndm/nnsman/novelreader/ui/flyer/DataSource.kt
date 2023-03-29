package com.learndm.nnsman.novelreader.ui.flyer

import com.learndm.nnsman.novelreader.data.model.Chapter

interface DataSource {
//    fun getChapterData(indexRange: IntRange): List<Chapter>
    fun getChapterData(chapterNumbers: IntArray)
}