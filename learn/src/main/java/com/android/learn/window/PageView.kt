//package com.android.learn.window
//
//import android.content.Context
//import android.graphics.Bitmap
//import android.graphics.Canvas
//import android.graphics.Color
//import android.util.AttributeSet
//import android.view.MotionEvent
//import android.view.View
//import android.view.ViewConfiguration
//import android.view.ViewTreeObserver
//import android.widget.Scroller
//import androidx.core.content.res.ResourcesCompat
//import com.learndm.nnsman.R
//import com.learndm.nnsman.utils.*
//import kotlin.concurrent.thread
//import kotlin.math.abs
//import kotlin.math.log
//import kotlin.math.max
//import kotlin.math.min
//
//enum class Direction {
//    LEFT, RIGHT, MOTIONLESS
//}
//
//class PageView @JvmOverloads constructor(
//    context: Context,
//    attrs: AttributeSet? = null,
//    defStyleAttr: Int = 0
//) : View(context, attrs, defStyleAttr) {
//
//    private val defWidth = 600
//    private val defHeight = 1000
//    private var realWidth = defWidth
//    private var realHeight = defHeight
//    private lateinit var currentPage: Bitmap
//    private lateinit var nextPage: Bitmap
//    private lateinit var cachePage: Bitmap
//    private var pageDataArray: Array<Int> =
//        arrayOf(R.drawable.test1, R.drawable.test2, R.drawable.test3)
//    private var currentPageNumber = 1
//    private var moving = false
//    private var loosen = false
//
//    private var movingDirection = Direction.MOTIONLESS
//    private var lastDirection = Direction.MOTIONLESS
//    private val scroller = Scroller(context)
//
//    init {
//        viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
//            override fun onPreDraw(): Boolean {
//                viewTreeObserver.removeOnPreDrawListener(this)
//                currentPage = Bitmap.createBitmap(realWidth, realHeight, Bitmap.Config.RGB_565)
//                nextPage = Bitmap.createBitmap(realWidth, realHeight, Bitmap.Config.RGB_565)
//                cachePage = Bitmap.createBitmap(realWidth, realHeight, Bitmap.Config.RGB_565)
//                drawPage(currentPage, 1)
//                drawPage(nextPage, 1)
//                postInvalidate()
//                return true
//            }
//        })
//    }
//
//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//        val widthSize = measureSize(defWidth, widthMeasureSpec)
//        val heightSize = measureSize(defHeight, heightMeasureSpec)
//        realWidth = widthSize
//        realHeight = heightSize
//        setMeasuredDimension(widthSize, heightSize)
//
//    }
//
//    override fun onDraw(canvas: Canvas?) {
//        super.onDraw(canvas)
//
//        if (touchX == -1f && touchY == -1f) {
//            drawNextPage(canvas)
//            drawCurPage(canvas)
//        } else {
//            if (movingDirection == Direction.LEFT) {
//                drawNextPage(canvas)
//                drawCurPage(canvas)
//            } else if (movingDirection == Direction.RIGHT) {
//                drawCurPage(canvas)
//                drawNextPage(canvas)
//            }
//        }
//    }
//
//    var touchX = -1f
//    var touchY = -1f
//    override fun onTouchEvent(event: MotionEvent): Boolean {
//        val x = event.x
//        val y = event.y
//
//        when (event.action) {
//            MotionEvent.ACTION_DOWN -> {
//                if (!scroller.isFinished) return false
//                touchX = x
//                touchY = y
//                moving = false
//                movingDirection = Direction.MOTIONLESS
//            }
//            MotionEvent.ACTION_MOVE -> {
//                //val slop = ViewConfiguration.get(context).scaledTouchSlop
//                if (!moving) {
//                    moving = true
//                    val dx = x - touchX
//                    val pageNum = currentPageNumber
//                    movingDirection = if (dx < 0) {
//                        if (pageNum == pageDataArray.size) {
//                            Direction.MOTIONLESS
//                        } else {
//                            swapPage(Direction.LEFT)
//                            currentPageNumber++
//                            lastDirection = Direction.LEFT
//                            Direction.LEFT
//                        }
//                    } else {
//                        if (pageNum == 1) {
//                            Direction.MOTIONLESS
//                        } else {
//                            swapPage(Direction.RIGHT)
//                            currentPageNumber--
//                            lastDirection = Direction.RIGHT
//                            Direction.RIGHT
//                        }
//                    }
//                }
//                if (movingDirection != Direction.MOTIONLESS)
//                    doMove(x, y)
//            }
//            MotionEvent.ACTION_UP -> {
//                if (movingDirection != Direction.MOTIONLESS) {
//                    loosen = true
//                    autoScrollPage()
//                    invalidate()
//                }
//            }
//        }
//
//        return true
//    }
//
//    private fun swapPage(direction: Direction) {
//
//        val temp = currentPage
//        if (direction != lastDirection) {
//            currentPage = nextPage
//            nextPage = temp
//        } else {
//            currentPage = nextPage
//            nextPage = cachePage
//            cachePage = temp
//        }
//
//        //第一次滑动
//        if (lastDirection == Direction.MOTIONLESS) {
//            if (direction == Direction.LEFT) {
//                drawPage(nextPage, currentPageNumber + 1)
//            } else if (direction == Direction.RIGHT) {
//                drawPage(nextPage, currentPageNumber - 1)
//            }
//        }
//    }
//
//    private fun autoScrollPage() {
//        val width = realWidth
//        var dx = width - abs(currentLeft)
//        val direction = movingDirection
//        if (direction==Direction.LEFT){
//            dx = -dx
//        }
//
//        scroller.startScroll(currentLeft.toInt(), 0, dx.toInt(), 0, 100)
//
//    }
//
//    /*
//    * 往左滑时,next不动,cur的left值变化,从0至-width
//    * 往右滑时,cur不动,next(pre)的left值变化,从-width至0
//    *
//    * */
//
//    var currentLeft = 0f
//    private fun doMove(x: Float, y: Float) {
//        var dx = x - touchX
//        var pageNumber = currentPageNumber
//
//        //向左滑,dx值最大为0;向右滑,dx最小值为0
//        if (movingDirection == Direction.LEFT) {
//            pageNumber-=1
//            if (pageNumber == pageDataArray.size) dx = 0f
//            dx = min(0f, dx)
//        } else if (movingDirection == Direction.RIGHT) {
//            pageNumber+=1
//            if (pageNumber == 1) dx = 0f
//            dx = max(0f, dx)
//        }
//
//        currentLeft = dx
//
//        postInvalidate()
//    }
//
//    fun drawPage(bitmap: Bitmap, pageNumber: Int) {
//        val canvas = Canvas(bitmap)
//        canvas.drawColor(getBitmap(pageNumber - 1))
//        //canvas.drawBitmap(getBitmap(pageNumber - 1), 0f, 0f, null)
//    }
//
//    fun drawCurPage(canvas: Canvas?) {
//         val left = if (movingDirection == Direction.LEFT) currentLeft else 0f
//        canvas?.drawBitmap(currentPage, left, 0f, null)
//    }
//
//    fun drawNextPage(canvas: Canvas?) {
//        val left = if (movingDirection == Direction.RIGHT) -width+currentLeft else 0f
//        canvas?.drawBitmap(nextPage, left, 0f, null)
//    }
//
//    private fun getBitmap(index: Int): Int {
//        val colors = intArrayOf(Color.YELLOW,Color.RED,Color.GRAY,Color.BLUE,Color.RED)
//        return colors[index]
////        val drawableId = pageDataArray[index]
////        val drawable = ResourcesCompat.getDrawable(resources, drawableId, null)
////        return BitmapUtils.drawableToBitmap(
////            drawable,
////            screenWidth(context),
////            screenHeight(context, true)
////        )
//    }
//
//    override fun computeScroll() {
//        if (scroller.computeScrollOffset()){
//            currentLeft = scroller.currX.toFloat()
//            postInvalidate()
//        }else if (loosen){
//            loosen = false
//            if(!isFirst() && !isLast()){
//                //cache page
//                val direction = movingDirection
//                if (direction == Direction.LEFT) {
//                    drawPage(cachePage, currentPageNumber + 1)
//                } else if (direction == Direction.RIGHT) {
//                    drawPage(cachePage, currentPageNumber - 1)
//                }
//            }
//        }
////        when {
////             -> {
////
////            }
////            loosen -> {
////
////            }
////        }
//    }
//
//    private fun isLast(): Boolean {
//        return currentPageNumber == pageDataArray.size
//    }
//
//    private fun isFirst(): Boolean {
//        return currentPageNumber == 1
//    }
//}

//==============================================================分割线===========================
//package com.learndm.nnsman.novelreader.ui
//
//import android.content.Context
//import android.graphics.Bitmap
//import android.graphics.Canvas
//import android.graphics.Color
//import android.text.BoringLayout
//import android.text.TextPaint
//import android.util.AttributeSet
//import android.view.MotionEvent
//import android.view.View
//import android.view.ViewTreeObserver
//import android.widget.Scroller
//import androidx.core.content.ContextCompat
//import com.learndm.nnsman.R
//import com.learndm.nnsman.novelreader.ui.home.Direction
//import com.learndm.nnsman.utils.*
//import kotlin.math.abs
//import kotlin.math.max
//import kotlin.math.min
//
//class PageView @JvmOverloads constructor(
//    context: Context,
//    attrs: AttributeSet? = null,
//    defStyleAttr: Int = 0
//) : View(context, attrs, defStyleAttr) {
//
//    private val defWidth = 600
//    private val defHeight = 1000
//    private var realWidth = defWidth
//    private var realHeight = defHeight
//    private lateinit var currentPage: Bitmap
//    private lateinit var nextPage: Bitmap
//    private var pageDataArray: IntArray =
//        intArrayOf(R.color.color1, R.color.color2, R.color.color3, R.color.color4, R.color.color5,R.color.color6,R.color.color7,R.color.color8)
//    private var currentPageNumber = 1
//    private var moving = false
//    private var bottomPageIndex = -1
//    private var movingDirection = Direction.MOTIONLESS
//    private var lastMovingDirection = Direction.MOTIONLESS
//    private val scroller = Scroller(context)
//    private val textPaint: TextPaint = TextPaint()
//    private var currentLeft = 0f
//
//    init {
//        textPaint.color = Color.BLACK
//        textPaint.textSize = sp2px(22f)
//        viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
//            override fun onPreDraw(): Boolean {
//                viewTreeObserver.removeOnPreDrawListener(this)
//                currentPage = Bitmap.createBitmap(realWidth, realHeight, Bitmap.Config.RGB_565)
//                nextPage = Bitmap.createBitmap(realWidth, realHeight, Bitmap.Config.RGB_565)
//                drawDataToPageBitmap(currentPage, 1)
//                postInvalidate()
//                return true
//            }
//        })
//    }
//
//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//        val widthSize = measureSize(defWidth, widthMeasureSpec)
//        val heightSize = measureSize(defHeight, heightMeasureSpec)
//        realWidth = widthSize
//        realHeight = heightSize
//        setMeasuredDimension(widthSize, heightSize)
//    }
//
//    override fun onDraw(canvas: Canvas?) {
//        super.onDraw(canvas)
//        if (touchX == -1f && touchY == -1f) {
//            drawPageBitmap(canvas, currentPage, 0f)
//        } else {
//            val left = if (movingDirection == Direction.LEFT) currentLeft else -width + currentLeft
//            if (bottomPageIndex == 1) {
//                drawPageBitmap(canvas, currentPage, 0f)
//                drawPageBitmap(canvas, nextPage, left)
//            } else {
//                drawPageBitmap(canvas, nextPage, 0f)
//                drawPageBitmap(canvas, currentPage, left)
//            }
//        }
//    }
//
//    private var touchX = -1f
//    private var touchY = -1f
//    override fun onTouchEvent(event: MotionEvent): Boolean {
//        val x = event.x
//        val y = event.y
//
//        when (event.action) {
//            MotionEvent.ACTION_DOWN -> {
//                if (!scroller.isFinished) return false
//                touchX = x
//                touchY = y
//                moving = false
//                movingDirection = Direction.MOTIONLESS
//            }
//            MotionEvent.ACTION_MOVE -> {
//                //val slop = ViewConfiguration.get(context).scaledTouchSlop
//                if (!moving) {
//                    moving = true
//                    val dx = x - touchX
//                    val pageNum = currentPageNumber
//                    movingDirection = when {
//                        (dx < 0 && pageNum == pageDataArray.size) || (dx > 0 && pageNum == 1) -> Direction.MOTIONLESS
//                        dx < 0 -> {
//                            val pn = ++currentPageNumber
//                            if (isSameDirection(Direction.LEFT)) {
//                                changeBottomIndex()
//                                drawDataToPageBitmap(
//                                    getBitmapByBottomIndexAndDir(Direction.LEFT),
//                                    pn
//                                )
//                            }
//                            lastMovingDirection = Direction.LEFT
//                            Direction.LEFT
//                        }
//                        dx > 0 -> {
//                            val pn = --currentPageNumber
//                            if (isSameDirection(Direction.RIGHT)) {
//                                changeBottomIndex()
//                                drawDataToPageBitmap(
//                                    getBitmapByBottomIndexAndDir(Direction.RIGHT),
//                                    pn
//                                )
//                            }
//                            lastMovingDirection = Direction.RIGHT
//                            Direction.RIGHT
//                        }
//                        else -> Direction.MOTIONLESS
//                    }
//                }
//                if (movingDirection != Direction.MOTIONLESS)
//                    doMove(x)
//            }
//            MotionEvent.ACTION_UP -> {
//                if (movingDirection != Direction.MOTIONLESS) {
//                    autoScrollPage()
//                    invalidate()
//                }
//            }
//        }
//
//        return true
//    }
//
//    private fun getBitmapByBottomIndexAndDir(direction: Direction): Bitmap {
//        val index = bottomPageIndex
//        val resultBitmap = if (direction == Direction.LEFT) {
//            if (index == 1) currentPage else nextPage
//        } else {
//            if (index == 1) nextPage else currentPage
//        }
//
//        return resultBitmap
//    }
//
//    private fun changeBottomIndex() {
//        bottomPageIndex = if(bottomPageIndex == -1 || bottomPageIndex ==1) 2 else 1
//    }
//
//    private fun autoScrollPage() {
//        val width = realWidth
//        var dx = width - abs(currentLeft)
//        if (movingDirection == Direction.LEFT) {
//            dx = -dx
//        }
//        scroller.startScroll(currentLeft.toInt(), 0, dx.toInt(), 0, 100)
//    }
//
//    private fun doMove(x: Float) {
//        var dx = x - touchX
//        var pageNumber = currentPageNumber
//
//        //向左滑,dx值最大为0;向右滑,dx最小值为0
//        if (movingDirection == Direction.LEFT) {
//            pageNumber -= 1
//            if (pageNumber == pageDataArray.size) dx = 0f
//            dx = min(0f, dx)
//        } else if (movingDirection == Direction.RIGHT) {
//            pageNumber += 1
//            if (pageNumber == 1) dx = 0f
//            dx = max(0f, dx)
//        }
//
//        currentLeft = dx
//        postInvalidate()
//    }
//
//    fun drawDataToPageBitmap(bitmap: Bitmap, pageNumber: Int) {
//        val canvas = Canvas(bitmap)
//        canvas.drawColor(ContextCompat.getColor(context,pageDataArray[pageNumber - 1]))
//        val text = "PageNumber: $pageNumber"
//        val boring = BoringLayout.isBoring(text, textPaint)
//        canvas.drawText(text, realWidth / 2f - boring.width / 2, realHeight / 2f, textPaint)
//    }
//
//    private fun drawPageBitmap(canvas: Canvas?, page: Bitmap, left: Float) {
//        canvas?.drawBitmap(page, left, 0f, null)
//    }
//
//    override fun computeScroll() {
//        if (scroller.computeScrollOffset()) {
//            currentLeft = scroller.currX.toFloat()
//            postInvalidate()
//        }
//    }
//
//    private fun isSameDirection(direction: Direction): Boolean {
//        return direction == lastMovingDirection || bottomPageIndex == -1
//    }
//}