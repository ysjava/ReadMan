package com.learndm.nnsman.novelreader.ui.flyer

import android.content.Context
import android.graphics.Bitmap
import android.os.BatteryManager
import android.widget.Toast
import com.learndm.nnsman.novelreader.data.model.Book
import com.learndm.nnsman.novelreader.data.model.Chapter
import com.learndm.nnsman.utils.statusBarHeight
import kotlin.math.max
import kotlin.math.min


class ReaderDataProcessor(
    val book: Book,
    override var readerView: ReaderView,
    override val dataSource: DataSource,
    override val chapterChangeListener: OnChapterChangeListener? = null,
) : DataProcessor {

    private var currentPageIndex = 0

    //缓存的章节
    private val chapters = hashMapOf<Int, Chapter>()

    //当前页面所展示章节的章节号码
    private var chapterNumber = 1
        set(value) {
            field = value
            chapterChangeListener?.change(value)
        }

    //章节分页后的页面集合
    private val pages = arrayListOf<TextPage>()

    //页面状态
    private var status = STATUS_FINISH

    //当前展示的bitmap
    private var currentBitmap: Bitmap? = null

    init {
        book.chapter?.forEach {
            chapters[it.number] = it
        }
    }

    companion object {
        const val STATUS_LOADING = 101
        const val STATUS_FINISH = 102
        const val STATUS_ERROR = 103
    }

    /**
     * 检查chapters中是否存在当前章节的后两个章节,
     * 没有就进行请求.
     * 例: 当前章节为5,判断缓存中是否存在章节6,不存在直接请求6,7,8,
     * 存在则判断是否存在章节7,不存在则获取7,8,9,存在则视为不需要进行缓存.
     *
     * */
    private fun checkCacheNext(
        @androidx.annotation.IntRange(
            from = 1,
            to = 5
        ) cacheNumber: Int = 3
    ): Boolean {
        //当前已是最后章节,不用缓存
        if (chapterNumber == book.chapterNumber) return false
        //下一章
        val next = chapters[chapterNumber + 1]
        //下下一章
        val nextNext = chapters[chapterNumber + 2]
        //如果都有就不缓存
        if (next != null && nextNext != null) return false
        //缓存的开始章节
        val s =
            if (next == null) {
                //下一章为空就从下一章开始缓存
                chapterNumber + 1
            } else if (chapterNumber + 2 <= book.chapterNumber) {
                //下一章不为空,但下下一章为空并且该书存在下下一章,那么就从下下一章开始缓存
                chapterNumber + 2
            } else {
                //下一章不为空,该书不存在下下一章,所有不必缓存
                -1
            }
        if (s == -1) return false
        val e = min(s + cacheNumber - 1, book.chapterNumber)
        val list = IntRange(s, e).filter {
            //chapters中已存在的章节不需要再请求
            chapters[it] == null
        }

        dataSource.getChapterData(list.toIntArray())
        return true
    }

    private fun checkCacheLast(
        @androidx.annotation.IntRange(
            from = 1,
            to = 5
        ) cacheNumber: Int = 3
    ): Boolean {
        //当前已是第一章
        if (chapterNumber == 1) return false
        //上一章
        val last = chapters[chapterNumber - 1]
        //上上一章
        val lastLast = chapters[chapterNumber - 2]
        //如果都有就不缓存
        if (last != null && lastLast != null) return false
        //缓存的结束章节
        val e =
            if (last == null) {
                chapterNumber - 1
            } else if (chapterNumber - 2 >= 1) {
                chapterNumber - 2
            } else {
                //上一章不为空,该书不存在上上一章,所有不必缓存
                -1
            }
        if (e == -1) return false
        val s = max(1, e - cacheNumber + 1)
        val list = IntRange(s, e).filter {
            chapters[it] == null
        }
        dataSource.getChapterData(list.toIntArray())
        return true
    }


    override fun skipLastChapter(): Boolean {
        val hasLast = hasLastChapter()
        if (hasLast)
            skipChapter(chapterNumber - 1)

        return hasLast
    }

    override fun skipNextChapter(): Boolean {
        val hasNext = hasNextChapter()
        if (hasNext)
            skipChapter(chapterNumber + 1)

        return hasNext
    }

    override fun refreshDataToCurrentBitmap() {
        refreshData()
        //当字体大小更改导致的refreshData(), 新的pages.size可能比旧的少了,
        //所以currentPageIndex就需要指向新page的最后一页.
        currentPageIndex = min(currentPageIndex, pages.size - 1)

        fillData()
//        currentBitmap?.let { fillCurrentData(it) }
    }

    /**
     * 跳章,获取chapterNumber前后各两章
     *
     * */
    override fun skipChapter(chapterNumber: Int) {
        if (chapterNumber < 1 || chapterNumber > book.chapterNumber) return
        currentPageIndex = 0
        pages.clear()
        this.chapterNumber = chapterNumber
        val chapter = chapters[chapterNumber]
        if (chapter != null) {
            refreshData()
            fillData()
            return
        }
        val s = max(1, chapterNumber - 2)
        val e = min(book.chapterNumber, chapterNumber + 2)
        val list = IntRange(s, e).filter {
            chapters[it] == null
        }

        status = STATUS_LOADING
        currentBitmap?.let {
            readerView.drawStatus(it, STATUS_LOADING, chapterNumber)
            readerView.invalidate()
        }
        dataSource.getChapterData(list.toIntArray())
    }

    private fun fillData() {
        currentBitmap?.let {
            fillCurrentData(it)
        }
    }

    override fun updateChapters(chapterList: List<Chapter>) {
        chapterList.forEach {
            chapters[it.number] = it
        }
        //当前处于加载中,并且所需数据已有
        if (status == STATUS_LOADING && chapters[chapterNumber] != null) {
            status = STATUS_FINISH
            currentBitmap?.let {
                refreshData()
//                currentPageIndex = 0
                fillCurrentData(it)
                readerView.invalidate()
            }
        }
    }

    override fun fillNextData(bitmap: Bitmap) {
        dataCheck()
        currentBitmap = bitmap
        //pages是否还有下一页
        if (currentPageIndex < pages.size - 1) {
            currentPageIndex += 1
            drawContent(bitmap)
        } else {
            //chapters是否还有下一章
            val nextChapter = chapters[chapterNumber + 1]
            if (nextChapter != null) {
                //更新pages数据为上一章数据
                chapterNumber += 1
                refreshData()
                currentPageIndex = 0

                //是否进行缓存章节
                checkCacheNext()
                fillData()
            } else if (hasNextChapter()) {
                skipChapter(chapterNumber + 1)
            } else {
                //已经是最后一章最后一页了
                Toast.makeText(readerView.context, "已经是最后一章最后一页了", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun fillLastData(bitmap: Bitmap) {
        dataCheck()
        currentBitmap = bitmap
        //pages是否还有上一页
        if (currentPageIndex > 0) {
            currentPageIndex -= 1

            drawContent(bitmap)
        } else {
            //chapters是否还有上一章
            val lashChapter = chapters[chapterNumber - 1]
            if (lashChapter != null) {
                //更新pages数据为上一章数据
                chapterNumber -= 1
                refreshData()
                currentPageIndex = pages.size - 1
                checkCacheLast()
                fillData()
            } else if (hasLastChapter()) {
                skipChapter(chapterNumber - 1)
            } else {
                //已经是第一章第一页了
                Toast.makeText(readerView.context, "已经是第一章第一页了", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun drawContent(bitmap: Bitmap) {
        //当前章节的页面阅读进度
        val pageProcessText = "${currentPageIndex + 1} / ${pages.size}"
        val title = chapters[chapterNumber]!!.title
        val manager = readerView.context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        //电池剩余容量
        val battery = manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        val secTitle = if (currentPageIndex == 0) book.name else title
        //绘制
        readerView.drawContent(
            bitmap,
            pages[currentPageIndex],
            pageProcessText,
            battery,
            secTitle
        )
    }

    override fun fillCurrentData(bitmap: Bitmap) {
        dataCheck()
        currentBitmap = bitmap
        drawContent(bitmap)
    }

    private fun dataCheck() {
        if (pages.isEmpty())
            refreshData()
    }

    override fun hasLastPage(): Boolean {
        if (currentPageIndex > 0) return true
        return hasLastChapter()
    }

    override fun hasNextPage(): Boolean {
        if (currentPageIndex < pages.size - 1) return true
        return hasNextChapter()
    }

    private fun hasNextChapter(): Boolean {
        return chapterNumber < book.chapterNumber
    }

    private fun hasLastChapter(): Boolean {
        return chapterNumber > 1
    }


    private fun refreshData() {
        //章节数据分成适合屏幕的每页数据
        val textPages = arrayListOf<TextPage>()
        val chapter = chapters[chapterNumber] ?: return
        val paragraphs = chapter.content.split("\n").map {
            "       $it"
        }
        readerView.apply {
            //每页都有的顶部的小标题高度
            val secondTitleHeight = (secondTitlePaint.textSize * 2).toInt()
            var textPage = TextPage()
            var measureTop = secondTitleHeight
            //可用绘制宽度
            val availableWidth = width - leftAndRightGap
            //可用绘制高度
            val availableHeight = height - statusBarHeight(context) * 2
            //计算标题
            var nn = 0
            val title = chapter.title
            do {
                val str = title.substring(nn)
                val measureRowWordNumber =
                    titlePaint.breakText(str, true, availableWidth.toFloat(), null)
                val rowData = title.substring(nn, nn + measureRowWordNumber)
                nn += measureRowWordNumber

                textPage.titles.add(rowData)
                measureTop += titleRowHeight + titleGap

            } while (nn < title.length)

            measureTop += rowHeight

            //计算内容
            for ((index, it) in paragraphs.withIndex()) {
                var number = 0
                do {
                    val str = it.substring(number)
                    val measureRowWordNumber =
                        contentTextPaint.breakText(str, true, availableWidth.toFloat(), null)
                    val rowData = it.substring(number, number + measureRowWordNumber)
                    number += measureRowWordNumber

                    if (availableHeight - measureTop >= rowHeight + rowGap) {
                        textPage.texts.add(rowData)
                        measureTop += rowHeight + rowGap
                    } else {
                        textPages.add(textPage)
                        textPage = TextPage()
                        textPage.texts.add(rowData)
                        measureTop = rowHeight + rowGap + secondTitleHeight + rowHeight
                    }
                } while (number < it.length)

                if (availableHeight - measureTop >= rowHeight + rowGap) {
                    textPage.texts.add("\n")
                    measureTop += paragraphGap
                } else {
                    textPages.add(textPage)
                    textPage = TextPage()
                    measureTop = secondTitleHeight + rowHeight
                }
                if (index == paragraphs.size - 1)
                    textPages.add(textPage)
            }
        }

        if (pages.isNotEmpty())
            pages.clear()
        pages.addAll(textPages)
    }
}


