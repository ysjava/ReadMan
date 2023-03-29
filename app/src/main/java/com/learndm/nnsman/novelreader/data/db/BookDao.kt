package com.learndm.nnsman.novelreader.data.db

import androidx.room.*
import com.learndm.nnsman.novelreader.data.model.Book
import com.learndm.nnsman.novelreader.data.model.Chapter
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Query("SELECT * FROM Book")
    fun findBooks(): List<Book>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addBooks(vararg book: Book)

    @Insert
    fun addBooks(books: List<Book>)

    @Query("select * from Book")
    fun testFlowData(): Flow<List<Book>>
}