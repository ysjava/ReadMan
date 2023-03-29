package com.learndm.nnsman.novelreader.ui.flyer

import android.graphics.Bitmap
import android.graphics.Canvas
import android.text.BoringLayout
import android.text.TextPaint
import android.view.MotionEvent
import android.widget.Scroller
import androidx.core.content.ContextCompat
import com.learndm.nnsman.R
import com.learndm.nnsman.novelreader.ui.home.Direction
import com.learndm.nnsman.utils.logDebug
import com.learndm.nnsman.utils.sp2px
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class CoverAnimation(override var readerView: ReaderView, override val listener: TurnPageListener) : TurnPageAnimation() {
    private lateinit var oneBitmap: Bitmap
    private lateinit var twoBitmap: Bitmap

    //底页页码,先绘制的为底页
    var bottomPageIndex = -1

    //上一次移动方向
    var lastMovingDirection = Direction.MOTIONLESS

    //移动方向
    var movingDirection: Direction = Direction.MOTIONLESS

    //触碰的位置
    var touchX: Float = -1f
    var touchY: Float = -1f

    //是否移动中
    var moving: Boolean = false

    private var currentLeft = 0f

    //当前页码
    var currentPageNumber = 1
    var scroller: Scroller = Scroller(readerView.context)

    var pageDataArray: IntArray =
        intArrayOf(
            R.color.color1,
            R.color.color2,
            R.color.color3,
            R.color.color4,
            R.color.color5,
            R.color.color6,
            R.color.color7,
            R.color.color8
        )

    override fun draw(canvas: Canvas) {

        if (!this@CoverAnimation::oneBitmap.isInitialized) {
            bitmapInit()
            listener.fillCurrentData(oneBitmap)
            canvas.drawBitmap(oneBitmap, 0f, 0f, null)
        } else {
            if (lastMovingDirection == Direction.MOTIONLESS && movingDirection == Direction.MOTIONLESS) {
                drawPageBitmap(canvas, twoBitmap, 0f)
                drawPageBitmap(canvas, oneBitmap, 0f)
                return
            }
            val left =
                if (movingDirection == Direction.LEFT) currentLeft else -readerView.width + currentLeft
            if (bottomPageIndex == 1) {
                drawPageBitmap(canvas, oneBitmap, 0f)
                drawPageBitmap(canvas, twoBitmap, left)
            } else {
                drawPageBitmap(canvas, twoBitmap, 0f)
                drawPageBitmap(canvas, oneBitmap, left)
            }
        }
    }

    private fun bitmapInit() {
        oneBitmap = Bitmap.createBitmap(readerView.width, readerView.height, Bitmap.Config.RGB_565)
        twoBitmap = oneBitmap.copy(Bitmap.Config.RGB_565, true)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!scroller.isFinished) return false
                touchX = x
                touchY = y
                moving = false
                movingDirection = Direction.MOTIONLESS

            }
            MotionEvent.ACTION_MOVE -> {
                //val slop = ViewConfiguration.get(context).scaledTouchSlop
                if (!moving) {
                    moving = true
                    val dx = x - touchX
                    movingDirection = when {
                        (dx < 0 && !listener.hasNextPage()) || (dx > 0 && !listener.hasLastPage()) ->
                            Direction.MOTIONLESS
                        dx < 0 -> {
                            //如果本次滑动的方向和上一次的方向相同,那么才去绘制数据到新的一页,
                            //相反的话就不绘制新数据到新的一页,因为相反的话理想情况下,直接改变两页的位置即可,反正展示的数据还是刚才的,
                            //但是这是没考虑到特殊情况的,比如网络问题导致上一页没有数据,就会绘制正在加载中或者加载失败等,
                            //如果回到上一页前,数据加载完成了,那么上一页显示的就应该是正常的数据信息了,
                            //所以不管本次和上次滑动的方向是否相同,当开始滑动时,下一页的的数据都应该重新绘制一遍.
                            if (isSameDirection(Direction.LEFT))
                                changeBottomIndex(Direction.LEFT)

                            //待填充数据的对象
                            ++currentPageNumber
                            val bitmap = getBitmapByBottomIndexAndDir(Direction.LEFT)
                            listener.fillNextData(bitmap)
                            lastMovingDirection = Direction.LEFT
                            Direction.LEFT
                        }
                        dx > 0 -> {
                            if (isSameDirection(Direction.RIGHT))
                                changeBottomIndex(Direction.RIGHT)

                            --currentPageNumber
                            //待填充数据的对象
                            val bitmap = getBitmapByBottomIndexAndDir(Direction.RIGHT)
                            listener.fillLastData(bitmap)
                            lastMovingDirection = Direction.RIGHT
                            Direction.RIGHT
                        }
                        else -> Direction.MOTIONLESS
                    }
                }

                if (movingDirection != Direction.MOTIONLESS)
                    doMove(x)
            }
            MotionEvent.ACTION_UP -> {
                if (movingDirection != Direction.MOTIONLESS) {
                    autoScrollPage()
                    readerView.invalidate()
                }
            }

        }


        return true
    }

    private fun drawPageBitmap(canvas: Canvas?, bitmap: Bitmap, left: Float) {
        canvas?.drawBitmap(bitmap, left, 0f, null)
    }

    override fun computeScroll() {
        if (scroller.computeScrollOffset()) {
            currentLeft = scroller.currX.toFloat()
            readerView.postInvalidate()
            if (moving) moving = false
        }
    }

    private fun doMove(x: Float) {
        var dx = x - touchX
        var pageNumber = currentPageNumber

        //向左滑,dx值最大为0;向右滑,dx最小值为0
        if (movingDirection == Direction.LEFT) {
            pageNumber -= 1
            if (pageNumber == pageDataArray.size) dx = 0f
            dx = min(0f, dx)
        } else if (movingDirection == Direction.RIGHT) {
            pageNumber += 1
            if (pageNumber == 1) dx = 0f
            dx = max(0f, dx)
        }
        currentLeft = dx
        readerView.postInvalidate()
    }

    private fun autoScrollPage() {
        val width = readerView.width
        var dx = width - abs(currentLeft)
        if (movingDirection == Direction.LEFT) {
            dx = -dx
        }
        scroller.startScroll(currentLeft.toInt(), 0, dx.toInt(), 0, 100)
    }

    private fun getBitmapByBottomIndexAndDir(direction: Direction): Bitmap {
        val index = bottomPageIndex
        return if ((direction == Direction.LEFT && index == 1) || (direction == Direction.RIGHT && index == 2)) oneBitmap else twoBitmap
    }

    private fun changeBottomIndex(direction: Direction) {
        val index = bottomPageIndex
        bottomPageIndex = when {
            index == 1 || (direction == Direction.LEFT && index == -1) -> 2
            else -> 1
        }
    }

    private fun isSameDirection(direction: Direction): Boolean {
        return direction == lastMovingDirection || bottomPageIndex == -1
    }
}