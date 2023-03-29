package com.learndm.nnsman.novelreader.data.model

import androidx.room.*
import androidx.room.util.UUIDUtil
import java.util.*

@Entity
data class Book(
    val name: String,
    val author: String,
    val coverUrl: String,
    val url: String,
    val desc: String,
    var chapterNumber: Int = 0,
    @PrimaryKey
    val id: String = UUID.randomUUID().toString()
) {
    @Ignore
    var chapter: List<Chapter>? = null

    @Ignore
    var chapterTitles: List<ChapterTitle>? = null
}
