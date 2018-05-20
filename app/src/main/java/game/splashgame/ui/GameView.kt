package game.splashgame.ui

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.*
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import game.splashgame.R
import game.splashgame.model.GamePoint
import game.splashgame.model.Location
import kotlin.math.log


class GameView(context: Context) : View(context) {

    private var linePaint: Paint = Paint()

    private var gameSubPoints: MutableList<GamePoint> = mutableListOf()
    private var gameMainPoint: GamePoint

    private var touchFlag = false

    private var xMain: Float = 0f
    private var yMain: Float = 0f

    private var handlers: Handler
    private var runnable: Runnable

    private var gameX: Float = 0f
    private var gameY: Float = 0f
    private var range: Float = 0f

    private var subX: Float = 0f
    private var subY: Float = 0f

    private var speed: Float = 20f

    private var directionSwipe = SwipeDirection.DEFAULT

    private var mat = arrayOf(
            intArrayOf(11, 2, 0, 0, 0, 0, 17),
            intArrayOf(21, 22, 23, 24, 25, 26, 27),
            intArrayOf(31, 32, 33, 34, 35, 36, 37),
            intArrayOf(41, 42, 43, 44, 45, 46, 74),
            intArrayOf(51, 52, 53, 54, 55, 56, 75),
            intArrayOf(61, 62, 63, 64, 65, 66, 76),
            intArrayOf(71, 72, 73, 74, 75, 76, 77)
    )

    private var boolean = true
    override fun onDraw(canvas: Canvas?) {
        Log.d("asd", "is click = $touchFlag")
        for (point in gameSubPoints) {
            canvas?.drawBitmap(point.imgRes, point.lot.x, point.lot.y, null)
        }

        if (touchFlag)
            if (lotToMove != null)
                move(canvas!!)

        canvas?.drawBitmap(gameMainPoint.imgRes, gameMainPoint.lot.x, gameMainPoint.lot.y, null)

        handlers.postDelayed(runnable, 20)
    }

    var lotToMove: GamePoint? = null
    // point move from left to right
    private fun drawOnRight(): Boolean {
        val mainPos = gameMainPoint.pos
        var i = mainPos.y.toInt()
        while (++i < 7) {
            if (mat[mainPos.x.toInt()][i] > 1) {
                val game = gameSubPoints.find {
                    it.pos.x == mainPos.x && it.pos.y == i.toFloat()
                }
                this.lotToMove = game
                 this.speed = Math.abs(gameMainPoint.lot.x - game!!.lot.x) / 4
                return true
            }
        }
        return false
    }

    // point move from right to left
    private fun drawOnLeft(): Boolean {
        val mainPos = gameMainPoint.pos
        var i = mainPos.y.toInt()
        while (--i >= 0) {
            if (mat[mainPos.x.toInt()][i] > 1) {
                val game = gameSubPoints.find {
                    it.pos.x == mainPos.x && it.pos.y == i.toFloat()
                }
                this.lotToMove = game
                this.speed = Math.abs(gameMainPoint.lot.x - game!!.lot.x) / 4
                return true
            }
        }
        return false
    }

    // point move from right to left
    private fun drawOnBottom(): Boolean {
        val mainPos = gameMainPoint.pos
        var i = mainPos.x.toInt()
        while (++i < 7) {
            if (mat[i][mainPos.y.toInt()] > 1) {
                val game = gameSubPoints.find {
                    it.pos.y == mainPos.y && it.pos.x == i.toFloat()
                }
                this.lotToMove = game
                this.speed = Math.abs(gameMainPoint.lot.y - game!!.lot.y) / 4
                return true
            }
        }
        return false
    }

    // point move from right to left
    private fun drawOnTop(): Boolean {
        val mainPos = gameMainPoint.pos
        var i = mainPos.x.toInt()
        while (--i >= 0) {
            if (mat[i][mainPos.y.toInt()] > 1) {
                val game = gameSubPoints.find {
                    it.pos.y == mainPos.y && it.pos.x == i.toFloat()
                }
                this.lotToMove = game
                this.speed = Math.abs(gameMainPoint.lot.y - game!!.lot.y) / 4
                return true
            }
        }
        return false
    }

    private var isRaising = false // the location is has x or y +
    private fun move(canvas: Canvas) {
        val to = lotToMove!!.lot
        if (to.x > xMain) {
            xMain += speed
            isRaising = true
        } else if (to.x < xMain) {
            xMain -= speed
            isRaising = false
        }

        if (to.y > yMain) {
            yMain += speed
            isRaising = true
        } else if (to.y < yMain) {
            yMain -= speed
            isRaising = false
        }

        if (isRaising) {
            if (to.x > xMain || to.y > yMain) {
                canvas.drawBitmap(gameMainPoint.imgRes, xMain, yMain, null)
            } else {
                xMain = to.x
                yMain = to.y
                canvas.drawBitmap(gameMainPoint.imgRes, xMain, yMain, null)
                gameMainPoint.pos = lotToMove!!.pos // cap nhat position khi da toi dia diem
                touchFlag = false
            }
        } else {
            if (to.x < xMain || to.y < yMain) {
                canvas.drawBitmap(gameMainPoint.imgRes, xMain, yMain, null)
            } else {
                xMain = to.x
                yMain = to.y
                canvas.drawBitmap(gameMainPoint.imgRes, xMain, yMain, null)
                gameMainPoint.pos = lotToMove!!.pos // cap nhat position khi da toi dia diem
                touchFlag = false
            }
        }
        gameMainPoint.lot = Location(xMain, yMain)
    }

    private var x1: Float = 0.toFloat()
    private var x2: Float = 0.toFloat()
    private var y1: Float = 0.toFloat()
    private var y2: Float = 0.toFloat()
    private val MIN_DISTANCE = 150
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val action = event?.action
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                x1 = event.x
                y1 = event.y
            }
            MotionEvent.ACTION_UP -> {
                x2 = event.x
                y2 = event.y
                val deltaX = x2 - x1
                val deltaY = y2 - y1
                when {
                    Math.abs(deltaX) > MIN_DISTANCE && deltaX > 0 -> if (drawOnRight()) touchFlag = true
                    Math.abs(deltaX) > MIN_DISTANCE && deltaX < 0 -> if (drawOnLeft()) touchFlag = true
                    Math.abs(deltaY) > MIN_DISTANCE && deltaY > 0 -> if (drawOnBottom()) touchFlag = true
                    Math.abs(deltaY) > MIN_DISTANCE && deltaY < 0 -> if (drawOnTop()) touchFlag = true
                    else -> SwipeDirection.DEFAULT
                }
                Log.d("Speed", this.speed.toString())
            }
        }
        return true
    }


    init {
        linePaint.color = Color.BLUE
        linePaint.strokeWidth = 12f
        linePaint.style = Paint.Style.STROKE

        handlers = Handler()
        runnable = Runnable {
            invalidate()
        }

        val display = (context as Activity).windowManager.defaultDisplay;
        val size = Point()
        display.getSize(size)

        gameX = size.x.toFloat()
        gameY = size.y.toFloat()
        subX = 90f
        subY = 200f
        range = gameX / 8

        gameMainPoint = GamePoint("sub", Location(subX, subY), Location(subX, subY)
                , BitmapFactory.decodeResource(context.resources, R.drawable.ball1)
                , Location(0f, 0f), 1)

        xMain = subX
        yMain = subY
        for (i in 1..7) {
            for (j in 1..7) {
                gameSubPoints.add(GamePoint("sub", Location(subX, subY), Location(subX, subY)
                        , BitmapFactory.decodeResource(context.resources, R.drawable.ball2)
                        , Location(i.toFloat() - 1, j.toFloat() - 1), mat[i - 1][j - 1]))
                subX += range
                Log.d("check location", "x = $subX. y = $subY")
            }
            subX = 90f
            subY += range
        }
        boolean = false

        Log.d("tag", "x = ${size.x}")
        Log.d("tag", "y = ${size.y}")
    }

    enum class SwipeDirection {
        DEFAULT, UP, DOWN, LEFT, RIGHT
    }
}
