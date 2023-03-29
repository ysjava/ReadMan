package com.learndm.nnsman.novelreader.data.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.learndm.nnsman.App
import com.learndm.nnsman.novelreader.data.model.*

@Database(
    entities = [Book::class, Chapter::class, Comment::class, Subscription::class, User::class],
    version = 15
)
abstract class NovelReaderDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun chapterDao(): ChapterDao
    abstract fun commentDao(): CommentDao

    companion object {
        private var instance: NovelReaderDatabase? = null
        fun getInstance(): NovelReaderDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    App.context,
                    NovelReaderDatabase::class.java,
                    "NovelReaderDatabase.db"
                ).build()
            }
            return instance as NovelReaderDatabase
        }
    }
}