package com.learndm.nnsman.utils

import androidx.annotation.IntRange
import com.learndm.nnsman.novelreader.data.db.NovelReaderDatabase
import com.learndm.nnsman.novelreader.data.model.Book
import com.learndm.nnsman.novelreader.data.model.Chapter
import com.learndm.nnsman.novelreader.data.model.Comment
import java.util.*

object DataCreator {

//    private fun createBook(): Book {
//        val bookId = UUID.randomUUID().toString()
//        val chapterId = UUID.randomUUID().toString()
//        val chapter =
//            Chapter(chapterId, bookId.substring(0, 8), text, 0, bookId)
//        createChapter(chapter)
//        return Book(bookId, bookId.substring(0, 8),"",1)
//    }
//
//    fun createBooks(@IntRange(from = 0, to = 10) number: Int): List<Book> {
//        val list = arrayListOf<Book>()
//        repeat(number) {
//            list.add(createBook())
//        }
//        return list
//    }

    private fun createChapter(chapter: Chapter) {
        NovelReaderDatabase.getInstance().chapterDao().addChapter(chapter)
        createComment(chapter.id)
    }

    private fun createComment(chapterId:String){
        val commentId = UUID.randomUUID().toString()
        val userId = UUID.randomUUID().toString()
        val comment = Comment(commentId,"我是评论: 我觉得我是一个好的评论!",userId, chapterId,0,10)
        NovelReaderDatabase.getInstance().commentDao().addComment(comment)
    }

    val text = "红日西坠，！!列车远去，在与铁轨的震动声中带起大片枯黄的落叶，也带起秋的萧瑟。\n\n" +
            "王煊注视，直至列车渐消失，他才收回目光，又送走了几位同学。\n\n" +
            "自此一别，将天各一方，不知道多少年后才能再相见，甚至有些人再无重逢期。\n" +
            "周围，有人还在缓慢地挥手，久久未曾放下，也有人沉默着，颇为伤感。\n" +
            "红日西坠，！!列车远去，在与铁轨的震动声中带起大片枯黄的落叶，也带起秋的萧瑟。\n" +
            "王煊注视，直至列车渐消失，他才收回目光，又送走了几位同学。\n" +
            "自此一别，将天各一方，不知道多少年后才能再相见，甚至有些人再无重逢期。\n" +
            "周围，有人还在缓慢地挥手，久久未曾放下，也有人沉默着，颇为伤感。\n" +
            "红日西坠，！!列车远去，在与铁轨的震动声中带起大片枯黄的落叶，也带起秋的萧瑟。\n" +
            "王煊注视，直至列车渐消失，他才收回目光，又送走了几位同学。\n" +
            "自此一别，将天各一方，不知道多少年后才能再相见，甚至有些人再无重逢期。\n" +
            "周围，有人还在缓慢地挥手，久久未曾放下，也有人沉默着，颇为伤感。\n" +
            "红日西坠，！!列车远去，在与铁轨的震动声中带起大片枯黄的落叶，也带起秋的萧瑟。\n" +
            "王煊注视，直至列车渐消失，他才收回目光，又送走了几位同学。\n" +
            "自此一别，将天各一方，不知道多少年后才能再相见，甚至有些人再无重逢期。\n" +
            "周围，有人还在缓慢地挥手，久久未曾放下，也有人沉默着，颇为伤感。\n" +
            "红日西坠，！!列车远去，在与铁轨的震动声中带起大片枯黄的落叶，也带起秋的萧瑟。\n" +
            "王煊注视，直至列车渐消失，他才收回目光，又送走了几位同学。\n" +
            "自此一别，将天各一方，不知道多少年后才能再相见，甚至有些人再无重逢期。\n" +
            "周围，有人还在缓慢地挥手，久久未曾放下，也有人沉默着，颇为伤感。\n" +
            "红日西坠，！!列车远去，在与铁轨的震动声中带起大片枯黄的落叶，也带起秋的萧瑟。\n" +
            "王煊注视，直至列车渐消失，他才收回目光，又送走了几位同学。\n" +
            "自此一别，将天各一方，不知道多少年后才能再相见，甚至有些人再无重逢期。\n" +
            "周围，有人还在缓慢地挥手，久久未曾放下，也有人沉默着，颇为伤感。\n" +
            "红日西坠，！!列车远去，在与铁轨的震动声中带起大片枯黄的落叶，也带起秋的萧瑟。\n" +
            "王煊注视，直至列车渐消失，他才收回目光，又送走了几位同学。\n" +
            "自此一别，将天各一方，不知道多少年后才能再相见，甚至有些人再无重逢期。\n" +
            "周围，有人还在缓慢地挥手，久久未曾放下，也有人沉默着，颇为伤感。\n" +
            "红日西坠，！!列车远去，在与铁轨的震动声中带起大片枯黄的落叶，也带起秋的萧瑟。\n" +
            "王煊注视，直至列车渐消失，他才收回目光，又送走了几位同学。\n" +
            "自此一别，将天各一方，不知道多少年后才能再相见，甚至有些人再无重逢期。\n" +
            "周围，有人还在缓慢地挥手，久久未曾放下，也有人沉默着，颇为伤感。\n" +
            "红日西坠，！!列车远去，在与铁轨的震动声中带起大片枯黄的落叶，也带起秋的萧瑟。\n" +
            "王煊注视，直至列车渐消失，他才收回目光，又送走了几位同学。\n" +
            "自此一别，将天各一方，不知道多少年后才能再相见，甚至有些人再无重逢期。\n" +
            "周围，有人还在缓慢地挥手，久久未曾放下，也有人沉默着，颇为伤感。\n" +
            "红日西坠，！!列车远去，在与铁轨的震动声中带起大片枯黄的落叶，也带起秋的萧瑟。\n" +
            "王煊注视，直至列车渐消失，他才收回目光，又送走了几位同学。\n" +
            "自此一别，将天各一方，不知道多少年后才能再相见，甚至有些人再无重逢期。\n" +
            "周围，有人还在缓慢地挥手，久久未曾放下，也有人沉默着，颇为伤感。结束！\n"
}