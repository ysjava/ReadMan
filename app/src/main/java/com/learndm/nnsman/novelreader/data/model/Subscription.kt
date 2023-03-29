package com.learndm.nnsman.novelreader.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Subscription(
    @PrimaryKey
    val id: String,

    val userId: String,
    val chapterId: String
)