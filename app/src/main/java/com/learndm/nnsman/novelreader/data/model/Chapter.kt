package com.learndm.nnsman.novelreader.data.model

import androidx.room.*
import java.util.*

@Entity
data class Chapter(
    val title: String,
    val content: String = "",
    val number: Int,//章节号码
    val bookUrl: String,
    @PrimaryKey val id: String = UUID.randomUUID().toString()
) {
    @Ignore
    var isSubscribe: Boolean = false
}

