package com.learndm.nnsman.novelreader.data.db

import androidx.room.*
import com.learndm.nnsman.novelreader.data.model.Book
import com.learndm.nnsman.novelreader.data.model.Chapter
import com.learndm.nnsman.novelreader.data.model.Comment

@Dao
interface CommentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addComment(vararg book: Comment)

    @Insert
    fun addComments(books: List<Comment>)
}