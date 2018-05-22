package game.splashgame.ui

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import android.view.View
import game.splashgame.R
import game.splashgame.model.GamePoint
import game.splashgame.model.Location


class GameView(context: Context) : View(context) {

    private var linePaint: Paint = Paint()
    private var linePath: Path = Path()
    private var linePasses = mutableListOf<Location>()
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
            intArrayOf(1, 1, 0, 0, 0, 0, 1),
            intArrayOf(1, 0, 1, 0, 1, 1, 0),
            intArrayOf(0, 1, 1, 1, 1, 0, 1),
            intArrayOf(0, 0, 0, 0, 1, 1, 1),
            intArrayOf(1, 0, 1, 1, 0, 0, 0),
            intArrayOf(0, 0, 0, 1, 0, 1, 0),
            intArrayOf(0, 1, 0, 1, 1, 0, 0)
    )

    private var firstInit = true
    override fun onDraw(canvas: Canvas?) {
        Log.d("asd", "is click = $touchFlag")
        for (point in gameSubPoints) {
            if (mat[point.pos.x.toInt()][point.pos.y.toInt()] > 0)
                canvas?.drawBitmap(point.imgRes, point.currentMove.x, point.currentMove.y, null)
        }

        if (touchFlag) {
            move(canvas!!)
            handlers.postDelayed(runnable, 15)
        }

        linePath.reset()
        for (line in gameMainPoint.lastMove) {
            linePath.lineTo(line.x + gameMainPoint.imgRes.height / 2, line.y + gameMainPoint.imgRes.width / 2)
        }
        linePath.lineTo(gameMainPoint.currentMove.x + gameMainPoint.imgRes.height / 2, gameMainPoint.currentMove.y + gameMainPoint.imgRes.width / 2)
        canvas?.drawPath(linePath, linePaint)
        canvas?.drawBitmap(gameMainPoint.imgRes, gameMainPoint.currentMove.x, gameMainPoint.currentMove.y, null)
    }

    // point move from left to right
    private fun drawOnRight(): Boolean {
        val mainPos = gameMainPoint.pos
        var i = mainPos.y.toInt()
        while (++i < 7) {
            if (this.drawDirectionLeftRight(mainPos, i)) return true
        }
        return false
    }

    // point move from right to left
    private fun drawOnLeft(): Boolean {
        val mainPos = gameMainPoint.pos
        var i = mainPos.y.toInt()
        while (--i >= 0) {
            if (this.drawDirectionLeftRight(mainPos, i)) return true
        }
        return false
    }

    private fun drawDirectionLeftRight(mainPos: Location, i: Int): Boolean {
        if (mat[mainPos.x.toInt()][i] > 0) {
            val game = gameSubPoints.find {
                it.pos.x == mainPos.x && it.pos.y == i.toFloat()
            } ?: return false

            if (mat[mainPos.x.toInt()][i] == 2) {
                val lastGameItem = gameMainPoint.lastMove.takeLast(1)[0]
                if (game.currentMove.x == lastGameItem.x && // get last item
                        game.currentMove.y == lastGameItem.y) {

                    mat[gameMainPoint.pos.x.toInt()][gameMainPoint.pos.y.toInt()] = 1 // stay point is change to 1
                    this.speed = Math.abs(gameMainPoint.currentMove.y - lastGameItem.y) / 3
                    this.updateUndoGameMainPoint(game, lastGameItem)
                    return true
                }
            } else if (mat[mainPos.x.toInt()][i] == 1) {
                mat[game.pos.x.toInt()][game.pos.y.toInt()] = 2
                this.speed = Math.abs(gameMainPoint.currentMove.y - game.currentMove.y) / 3
                this.updateGameMainPoint(game)
                return true
            }
        }
        return false
    }

    private fun drawOnTop(): Boolean {
        val mainPos = gameMainPoint.pos
        var i = mainPos.x.toInt()
        while (--i >= 0) { // find the point for moving
            if(this.drawDirectionTopBottom(mainPos, i)) return true
        }
        return false
    }

    private fun drawOnBottom(): Boolean {
        val mainPos = gameMainPoint.pos
        var i = mainPos.x.toInt()
        while (++i < 7) {
            if(this.drawDirectionTopBottom(mainPos, i)) return true
        }
        return false
    }

    private fun drawDirectionTopBottom(mainPos: Location, i: Int): Boolean {
        if (mat[i][mainPos.y.toInt()] > 0) {
            val game = gameSubPoints.find {
                it.pos.y == mainPos.y && it.pos.x == i.toFloat()
            } ?: return false

            if (mat[i][mainPos.y.toInt()] == 2) {
                val lastGameItem = gameMainPoint.lastMove.takeLast(1)[0]
                if (game.currentMove.x == lastGameItem.x && // get last item
                        game.currentMove.y == lastGameItem.y) {

                    mat[gameMainPoint.pos.x.toInt()][gameMainPoint.pos.y.toInt()] = 1 // stay point is change to 1
                    this.speed = Math.abs(gameMainPoint.currentMove.x - lastGameItem.x) / 3
                    this.updateUndoGameMainPoint(game, lastGameItem)
                    return true
                }
            } else if (mat[i][mainPos.y.toInt()] == 1) {
                mat[game.pos.x.toInt()][game.pos.y.toInt()] = 2
                this.speed = Math.abs(gameMainPoint.currentMove.x - game.currentMove.x) / 3
                this.updateGameMainPoint(game)
                return true
            }
        }
        return false
    }

    private fun updateUndoGameMainPoint(game: GamePoint, lastGameItem: Location) {
        gameMainPoint.currentMove = lastGameItem

        gameMainPoint.lastMove = gameMainPoint.lastMove.dropLast(1).toMutableList()
        gameMainPoint.pos = game.pos
    }

    private fun updateGameMainPoint(game: GamePoint) {
        gameMainPoint.lastMove.add(gameMainPoint.currentMove)
        gameMainPoint.currentMove = game.currentMove
        gameMainPoint.pos = game.pos
    }

    private var isRaising = false // the location is has x or y +

    private fun move(canvas: Canvas) {
        val to = gameMainPoint.currentMove
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
            if (to.x > xMain || to.y > yMain) { // when the main point not moved to subpoint yet (on the way from main to sub)
                canvas.drawBitmap(gameMainPoint.imgRes, xMain, yMain, null)
            } else {
                xMain = to.x
                yMain = to.y
                canvas.drawBitmap(gameMainPoint.imgRes, xMain, yMain, null)
                linePasses.add(Location(xMain, yMain))
                touchFlag = false
            }
        } else {
            if (to.x < xMain || to.y < yMain) {
                canvas.drawBitmap(gameMainPoint.imgRes, xMain, yMain, null)
            } else {
                xMain = to.x
                yMain = to.y
                linePasses.add(Location(xMain, yMain))
                canvas.drawBitmap(gameMainPoint.imgRes, xMain, yMain, null)
                touchFlag = false
            }
        }
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
                // event before slide
                x1 = event.x
                y1 = event.y
            }
            MotionEvent.ACTION_UP -> {
                // after slide
                x2 = event.x
                y2 = event.y
                val deltaX = x2 - x1
                val deltaY = y2 - y1
                when {
                    Math.abs(deltaX) > MIN_DISTANCE && deltaX > 0 -> if (drawOnRight()) touchFlag = true
                    Math.abs(deltaX) > MIN_DISTANCE && deltaX < 0 -> if (drawOnLeft()) touchFlag = true
                    Math.abs(deltaY) > MIN_DISTANCE && deltaY > 0 -> if (drawOnBottom()) touchFlag = true
                    Math.abs(deltaY) > MIN_DISTANCE && deltaY < 0 -> if (drawOnTop()) touchFlag = true
                }
                invalidate()
                Log.d("Speed", this.speed.toString())
            }
        }
        return true
    }

    init {
        linePaint.color = Color.BLUE
        linePaint.strokeWidth = 36f
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

        linePath.moveTo(subX, subY)
        gameMainPoint = GamePoint("sub", Location(subX, subY), mutableListOf()
                , BitmapFactory.decodeResource(context.resources, R.drawable.ball1)
                , Location(0f, 0f), 1)

        xMain = subX
        yMain = subY
        for (i in 1..7) {
            for (j in 1..7) {
                gameSubPoints.add(GamePoint("sub", Location(subX, subY), mutableListOf()
                        , BitmapFactory.decodeResource(context.resources, R.drawable.ball2)
                        , Location(i.toFloat() - 1, j.toFloat() - 1), mat[i - 1][j - 1]))
                subX += range
                Log.d("check location", "x = $subX. y = $subY")

            }
            subX = 90f
            subY += range
        }
        Log.d("tag", "x = ${size.x}")
        Log.d("tag", "y = ${size.y}")
    }

    enum class SwipeDirection {
        DEFAULT, UP, DOWN, LEFT, RIGHT
    }
}
