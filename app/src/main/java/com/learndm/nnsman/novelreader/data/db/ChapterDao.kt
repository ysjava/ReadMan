package com.learndm.nnsman.novelreader.data.db

import androidx.room.*
import com.learndm.nnsman.novelreader.data.model.Book
import com.learndm.nnsman.novelreader.data.model.Chapter
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

@Dao
interface ChapterDao {
    @Insert
    fun addChapter(chapter: Chapter)

    @Query("SELECT * FROM Chapter")
    fun findChapters(): List<Chapter>



}