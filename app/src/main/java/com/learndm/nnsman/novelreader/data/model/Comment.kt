package com.learndm.nnsman.novelreader.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Comment(
    @PrimaryKey
    val id: String,
    val content: String,
    val userId: String,
    val chapterId: String,
    val startIndex: Int,
    val endIndex: Int
)
