package com.learndm.nnsman.novelreader.ui.flyer

import android.content.Context
import android.graphics.*
import android.text.BoringLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.core.content.ContextCompat
import com.learndm.nnsman.R
import com.learndm.nnsman.novelreader.data.model.Book
import com.learndm.nnsman.novelreader.data.model.Chapter
import com.learndm.nnsman.utils.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs
import kotlin.math.log

/**
 *
 * 阅读内容绘制view
 *
 * */
class ReaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    //默认宽600px
    private val defWidth = 600

    //默认高1000px
    private val defHeight = 1000

    //背景色
    @ColorInt
    private var bgColor = ContextCompat.getColor(context, R.color.color9)

    var pageTurningModel: PageTurningModel = PageTurningModel.COVER
        set(value) {
            field = value
            turnPageAnimation = when (value) {
                PageTurningModel.COVER -> CoverAnimation(this, listener)
                PageTurningModel.SIMULATION -> CoverAnimation(this, listener)
            }
        }

    var touchListener: OnReaderViewTouchListener? = null

    //数据处理器
    private var dataProcessor: DataProcessor? = null

    //翻页回调
    private val listener = object : TurnPageListener {
        override fun fillNextData(bitmap: Bitmap) {
            dataProcessor?.fillNextData(bitmap)
        }

        override fun fillLastData(bitmap: Bitmap) {
            dataProcessor?.fillLastData(bitmap)
        }

        override fun fillCurrentData(bitmap: Bitmap) {
            dataProcessor?.fillCurrentData(bitmap)
        }

        override fun hasNextPage(): Boolean {
            return dataProcessor?.hasNextPage() ?: false
        }

        override fun hasLastPage(): Boolean {
            return dataProcessor?.hasLastPage() ?: false
        }

    }

    var chapterChangeListener: OnChapterChangeListener? = null

    //获取数据的回调
    var dataSource: DataSource? = null

    //书本信息
    var book: Book? = null
        set(value) {
            field = value
            invalidate()
        }

    //翻页动画
    private var turnPageAnimation: TurnPageAnimation? = null

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSize = measureSize(defWidth, widthMeasureSpec)
        val heightSize = measureSize(defHeight, heightMeasureSpec)
        setMeasuredDimension(widthSize, heightSize)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val result = checkArguments()
        if (result) {
            if (dataProcessor == null) {
                dataProcessor =
                    ReaderDataProcessor(book!!, this, dataSource!!, chapterChangeListener)
            }
            if (turnPageAnimation == null)
                turnPageAnimation = CoverAnimation(this, listener)
            turnPageAnimation?.draw(canvas)
            isInit = true
        } else {
            canvas.drawColor(bgColor)
            val text = "加载中,请稍等..."
            val textWidth = contentTextPaint.measureText(text)
            canvas.drawText(text, width / 2f - textWidth / 2, height / 2f, contentTextPaint)
        }
    }

    private fun checkArguments(): Boolean {
        return book != null && dataSource != null
    }

    private var isInit = false
    private var isMove = false
    private var touchX = 0f
    private var touchY = 0f
    private var canMove = true
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchX = event.x
                touchY = event.y
                isMove = false
                turnPageAnimation?.onTouchEvent(event)
                canMove = touchListener?.canMove() ?: true
            }
            MotionEvent.ACTION_MOVE -> {
                if (!canMove) return true
                val slop = ViewConfiguration.get(context).scaledTouchSlop
                if (!isMove) {
                    isMove = abs(event.x - touchX) > slop || abs(event.y - touchY) > slop
                }
                if (isMove)
                    turnPageAnimation?.onTouchEvent(event)
            }
            MotionEvent.ACTION_UP -> {
                if (!isMove) {
                    if (centerRect == null) centerRect =
                        Rect(width / 5, height / 3, width * 4 / 5, height * 2 / 3)
                    centerRect?.let {
                        if (it.contains(touchX.toInt(), touchY.toInt()) && isInit) {
                            touchListener?.onCenterClick(this)
                        }
                    }
                }
                if (!canMove) {
                    touchListener?.resumeMovement()
                    canMove = true
                }
                turnPageAnimation?.onTouchEvent(event)
            }
        }

        return true
    }

    override fun computeScroll() {
        turnPageAnimation?.computeScroll()
    }


    fun setReadTheme(readTheme: ReadTheme) {
        bgColor = resources.getColor(readTheme.bgColor, null)
        val color = resources.getColor(readTheme.contentTextColor, null)
        titlePaint.color = color
        secondTitlePaint.color = color
        contentTextPaint.color = color
        timePaint.color = color
        chapterShowProcessPaint.color = color

        dataProcessor?.refreshDataToCurrentBitmap()
        invalidate()
    }

    private var centerRect: Rect? = null

    val contentTextPaint = TextPaint().apply {
        textSize = dp2px(24)
    }

    //主标题
    val titlePaint = TextPaint().apply {
        textSize = sp2px(36)
    }

    //小标题
    val secondTitlePaint = TextPaint().apply {
        textSize = sp2px(10)
    }

    //当前章节观看进度
    private val chapterShowProcessPaint = TextPaint().apply {
        textSize = sp2px(10)
    }

    //时间戳
    private val timePaint = TextPaint().apply {
        textSize = sp2px(10)
    }

    private val linePaint = Paint().apply {
        color = Color.RED
        strokeWidth = 3f
    }

    //电池
    val batteryPaint = Paint()

    /**
     * 设置内容的大小,包括标题文字和内容文字,
     * 以及行高,行距,标题行高,标题行距.
     * @param sp 内容文字的大小,其它几项都是跟随改变的,例如标题文字
     * 大小是内容文字大小的1.5倍,行高等于内容文字等等
     * */
    fun setContentSize(sp: Int) {
        logDebug("size: $sp")
        if (sp > 33 || sp < 12) return
        logDebug("size: $sp")
        contentTextPaint.textSize = sp2px(sp)
        titlePaint.textSize = 1.5f * sp2px(sp)

        rowHeight = contentTextPaint.textSize.toInt()
        rowGap = (rowHeight * 0.5).toInt()
        titleRowHeight = titlePaint.textSize.toInt()
        titleGap = (titleRowHeight * 0.5).toInt()

        dataProcessor?.refreshDataToCurrentBitmap()
        postInvalidate()
    }

//    fun contentSizeMinus() = setContentSize(sp2px())
//    fun contentSizeAdd() = setContentSize(contentTextPaint.textSize.toInt() + 1)

    //页面左右两边绘制间距
    var leftAndRightGap = dp2px(50).toInt()
        private set

    //行高
    var rowHeight = contentTextPaint.textSize.toInt()
        private set

    //行间距,默认0.5倍行高
    var rowGap = (rowHeight * 0.5).toInt()
        private set

    //标题行高
    var titleRowHeight = titlePaint.textSize.toInt()
        private set

    //标题间距,0.5倍标题行高
    var titleGap = (titleRowHeight * 0.5).toInt()

    private var drawTop = 0f

    //段落间距
    var paragraphGap = dp2px(10).toInt()
        private set

    fun drawContent(
        bitmap: Bitmap,
        textPage: TextPage,
        pageProcess: String,
        battery: Int,
        secTitle: String
    ) {
        val canvas = Canvas(bitmap)
        drawTop = statusBarHeight(context).toFloat()
        canvas.drawColor(bgColor)
//        canvas.drawLine(leftAndRightGap / 2f, 0f, leftAndRightGap / 2f, height.toFloat(), linePaint)
//        canvas.drawLine(
//            width - leftAndRightGap / 2f,
//            0f,
//            width - leftAndRightGap / 2f,
//            height.toFloat(),
//            linePaint
//        )
//
//        canvas.drawLine(
//            0f,
//            statusBarHeight(context).toFloat(),
//            width.toFloat(),
//            statusBarHeight(context).toFloat(),
//            linePaint
//        )
//        canvas.drawLine(
//            0f,
//            height - statusBarHeight(context).toFloat(),
//            width.toFloat(),
//            height - statusBarHeight(context).toFloat(),
//            linePaint
//        )
        //绘制小标题
        drawTop += secondTitlePaint.textSize * 2
        canvas.drawText(secTitle, leftAndRightGap / 2f, drawTop, secondTitlePaint)

        //绘制标题
        textPage.titles.forEach {
            drawTop += titleRowHeight + titleGap
            canvas.drawText(it, leftAndRightGap / 2f, drawTop, titlePaint)
        }

        drawTop += rowHeight

        //绘制内容
        textPage.texts.forEach {
            if (it != "\n") {
                drawTop += rowHeight + rowGap
                canvas.drawText(it, leftAndRightGap / 2f, drawTop, contentTextPaint)
            } else {
                drawTop += paragraphGap
            }
        }

        val y = height - dp2px(10)
        //绘制观看进度
        canvas.drawText(pageProcess, leftAndRightGap / 2f, y, chapterShowProcessPaint)
        //绘制时间
        val date = Date(System.currentTimeMillis())
        val format = SimpleDateFormat("HH:mm", Locale.CHINA)
        val str = format.format(date)
        val boring = BoringLayout.isBoring(str, timePaint)
        val x = width - leftAndRightGap / 2f - boring.width
        canvas.drawText(str, x, y, timePaint)
    }

    fun drawStatus(bitmap: Bitmap, status: Int, chapterNumber: Int) {
        when (status) {
            ReaderDataProcessor.STATUS_LOADING -> {
                val canvas = Canvas(bitmap)
                canvas.drawColor(bgColor)
                val title = book!!.chapterTitles?.get(chapterNumber - 1)?.title ?: ""
                val tip = "正在加载${title}..."
                val textWidth = contentTextPaint.measureText(tip)
                canvas.drawText(tip, width / 2f - textWidth / 2, height / 2f, contentTextPaint)
            }
            ReaderDataProcessor.STATUS_ERROR -> {

            }
        }
    }

    fun updateChapters(chapterList: List<Chapter>) = dataProcessor?.updateChapters(chapterList)

    fun skipNextChapter(): Boolean = dataProcessor?.skipNextChapter() ?: false

    fun skipLastChapter(): Boolean = dataProcessor?.skipLastChapter() ?: false

    fun skipChapter(chapterNumber: Int) = dataProcessor?.skipChapter(chapterNumber)


}