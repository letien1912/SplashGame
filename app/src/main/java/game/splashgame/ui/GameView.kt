package game.splashgame.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.*
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import android.view.View
import game.splashgame.R
import game.splashgame.model.GameData
import game.splashgame.model.GamePoint
import game.splashgame.model.Location


class GameView(context: Context, private val gameData: GameData) : View(context) {

    private var linePaint: Paint = Paint()
    private var linePath: Path = Path()
    private var linePasses = mutableListOf<Location>()
    private var gameSubPoints: MutableList<GamePoint> = mutableListOf()

    private lateinit var gameMainPoint: GamePoint
    private lateinit var nextMove: GamePoint

    private var touchFlag = false
    private var isMovieBack = false

    private var xMovingWay: Float = 0f
    private var yMovingWay: Float = 0f

    private var handlers: Handler
    private var runnable: Runnable

    private var gameX: Float = 0f
    private var gameY: Float = 0f
    private var range: Float = 0f

    private var xStartPos: Float = 0f
    private var yStartPos: Float = 0f

    private var speed: Float = 20f

    private var directionSwipe = SwipeDirection.DEFAULT

    private var mat = arrayOf(
            intArrayOf(1, 0, 0, 5, 9, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 5, 5, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0)
    )

    override fun onDraw(canvas: Canvas?) {
        Log.d("asd", "is click = $touchFlag")
        for (point in gameSubPoints) {
            if (mat[point.pos.x.toInt()][point.pos.y.toInt()] > UNAVAINABLE_POINT)
                canvas?.drawBitmap(point.imgRes, point.currentMove.x, point.currentMove.y, null)
        }


        linePath.reset()
        linePath.moveTo(xStartPos + gameMainPoint.imgRes.height / 2, yStartPos + gameMainPoint.imgRes.width / 2)
        for (line in gameMainPoint.lastMove) {
            linePath.lineTo(line.x + gameMainPoint.imgRes.height / 2, line.y + gameMainPoint.imgRes.width / 2)
        }
        linePath.lineTo(gameMainPoint.currentMove.x + gameMainPoint.imgRes.height / 2, gameMainPoint.currentMove.y + gameMainPoint.imgRes.width / 2)
        canvas?.drawPath(linePath, linePaint)

        if (touchFlag) {
            move(canvas!!)
            handlers.postDelayed(runnable, 15)
        } else {
            canvas?.drawBitmap(gameMainPoint.imgRes, gameMainPoint.currentMove.x, gameMainPoint.currentMove.y, null)
        }
    }

    // point move from left to right
    private fun drawOnRight(): Boolean {
        val mainPos = gameMainPoint.pos
        var i = mainPos.y.toInt()
        while (++i < TOTAL_POINT_LENGTH) {
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
        if (this.isAvailablePoint(mainPos.x.toInt(), i)) {
            val game = gameSubPoints.find {
                it.pos.x == mainPos.x && it.pos.y == i.toFloat()
            } ?: return false

            if (this.couldMoveBack(mainPos.x.toInt(), i)) {
                if (gameMainPoint.lastMove.size <= 0) return false// no way to go
                val lastGameItem = gameMainPoint.lastMove.takeLast(1)[0]
                if (game.currentMove.x == lastGameItem.x && // get last item
                        game.currentMove.y == lastGameItem.y) {

                    this.markAsCouldMove(gameMainPoint.pos.x.toInt(), gameMainPoint.pos.y.toInt())
                    this.speed = Math.abs(gameMainPoint.currentMove.x - lastGameItem.x) / 3
                    this.updateUndoGameMainPoint(game, lastGameItem) // have to update point first because
                    this.nextMove = game
                    this.isMovieBack = true // make sure the point updated
                    return true
                }
            } else if (this.couldMoveForward(mainPos.x.toInt(), i)) {
                mat[game.pos.x.toInt()][i] = 2
                this.speed = Math.abs(gameMainPoint.currentMove.x - game.currentMove.x) / 3
                this.nextMove = game
                this.isMovieBack = false
                return true
            } else if (isDestinationPoint(mainPos.x.toInt(), i)) {
                if (couldMoveToDestination(mainPos.x.toInt(), i)) {
                    this.speed = Math.abs(gameMainPoint.currentMove.x - game.currentMove.x) / 3
                    this.nextMove = game
                    this.isMovieBack = false
                    return true
                }
            }
        }
        return false
    }

    private fun drawOnTop(): Boolean {
        val mainPos = gameMainPoint.pos
        var i = mainPos.x.toInt()
        while (--i >= 0) { // find the point for moving
            if (this.drawDirectionTopBottom(mainPos, i)) return true
        }
        return false
    }

    private fun drawOnBottom(): Boolean {
        val mainPos = gameMainPoint.pos
        var i = mainPos.x.toInt()
        while (++i < TOTAL_POINT_LENGTH) {
            if (this.drawDirectionTopBottom(mainPos, i)) return true
        }
        return false
    }

    private fun drawDirectionTopBottom(mainPos: Location, i: Int): Boolean {
        if (mat[i][mainPos.y.toInt()] > 0) {
            val game = gameSubPoints.find {
                it.pos.y == mainPos.y && it.pos.x == i.toFloat()
            } ?: return false

            if (this.couldMoveBack(i, mainPos.y.toInt())) {
                if (gameMainPoint.lastMove.size <= 0) return false
                val lastGameItem = gameMainPoint.lastMove.takeLast(1)[0]
                if (game.currentMove.x == lastGameItem.x && // get last item
                        game.currentMove.y == lastGameItem.y) {

                    this.markAsCouldMove(gameMainPoint.pos.x.toInt(), gameMainPoint.pos.y.toInt()) // mark current main point moved
                    this.speed = Math.abs(gameMainPoint.currentMove.y - lastGameItem.y) / 3
                    this.nextMove = game
                    this.updateUndoGameMainPoint(game, lastGameItem)
                    this.isMovieBack = true
                    return true
                }
            } else if (this.couldMoveForward(i, mainPos.y.toInt())) {
                this.markAsMoved(i, game.pos.y.toInt())
                this.speed = Math.abs(gameMainPoint.currentMove.y - game.currentMove.y) / 3
                this.nextMove = game
                this.isMovieBack = false
                return true
            } else if (isDestinationPoint(i, mainPos.y.toInt())) {
                if (couldMoveToDestination(i, mainPos.y.toInt())) {
                    this.speed = Math.abs(gameMainPoint.currentMove.y - game.currentMove.y) / 3
                    this.nextMove = game
                    this.isMovieBack = false
                    return true
                }
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
        val to = nextMove.currentMove
        if (to.x > xMovingWay) {
            xMovingWay += speed
            isRaising = true
        } else if (to.x < xMovingWay) {
            xMovingWay -= speed
            isRaising = false
        }

        if (to.y > yMovingWay) {
            yMovingWay += speed
            isRaising = true
        } else if (to.y < yMovingWay) {
            yMovingWay -= speed
            isRaising = false
        }

        if (isRaising) {
            if (to.x > xMovingWay || to.y > yMovingWay) { // when the main point not moved to subpoint yet (on the way from main to sub)
                canvas.drawBitmap(gameMainPoint.imgRes, xMovingWay, yMovingWay, null)
            } else {
                xMovingWay = to.x
                yMovingWay = to.y
                canvas.drawBitmap(gameMainPoint.imgRes, xMovingWay, yMovingWay, null)
                linePasses.add(Location(xMovingWay, yMovingWay))
                touchFlag = false
                if (!isMovieBack) // is case of moving back, we make sure the point is updated
                    this.updateGameMainPoint(this.nextMove) // replace location of main point when it move to the new one
            }
        } else {
            if (to.x < xMovingWay || to.y < yMovingWay) {
                canvas.drawBitmap(gameMainPoint.imgRes, xMovingWay, yMovingWay, null)
            } else {
                xMovingWay = to.x
                yMovingWay = to.y
                linePasses.add(Location(xMovingWay, yMovingWay))
                canvas.drawBitmap(gameMainPoint.imgRes, xMovingWay, yMovingWay, null)
                touchFlag = false
                if (!isMovieBack)
                    this.updateGameMainPoint(this.nextMove)
            }
        }
    }

    private var x1: Float = 0.toFloat()
    private var x2: Float = 0.toFloat()
    private var y1: Float = 0.toFloat()
    private var y2: Float = 0.toFloat()
    private val MIN_DISTANCE = 150

    @SuppressLint("ClickableViewAccessibility")
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
        // init draw line
        linePaint.color = Color.BLUE
        linePaint.strokeWidth = 36f
        linePaint.style = Paint.Style.STROKE

        // loop for animation
        handlers = Handler()
        runnable = Runnable {
            invalidate()
        }

        initMat()

        // device screen
        val display = (context as Activity).windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        gameX = size.x.toFloat()
        gameY = size.y.toFloat()
        xStartPos = 90f
        yStartPos = 200f
        range = gameX / 8

        val startPos = Location(0f, 0f)


        xMovingWay = xStartPos
        yMovingWay = yStartPos


        var xPointLocation = xStartPos
        var yPointLocation = yStartPos
        for (i in 1..TOTAL_POINT_LENGTH) {
            for (j in 1..TOTAL_POINT_LENGTH) {

                when {
                    this.isStartPoint(i - 1, j - 1) -> {
                        gameMainPoint = GamePoint("main", Location(xStartPos, yStartPos), mutableListOf()
                                , BitmapFactory.decodeResource(context.resources, R.drawable.ball1)
                                , startPos, READY_POINT)
                        this.nextMove = this.gameMainPoint
                    }
                }

                when {
                    this.isDestinationPoint(i - 1, j - 1) -> {
                        gameSubPoints.add(GamePoint("des", Location(xPointLocation, yPointLocation), mutableListOf()
                                , BitmapFactory.decodeResource(context.resources, R.drawable.ball3)
                                , Location(i.toFloat() - 1, j.toFloat() - 1), mat[i - 1][j - 1]))
                    }
                    else ->
                        gameSubPoints.add(GamePoint("sub", Location(xPointLocation, yPointLocation), mutableListOf()
                                , BitmapFactory.decodeResource(context.resources, R.drawable.ball2)
                                , Location(i.toFloat() - 1, j.toFloat() - 1), mat[i - 1][j - 1]))

                }

                xPointLocation += range
                Log.d("check location", "x = $xPointLocation. y = $yPointLocation")
            }
            xPointLocation = 90f
            yPointLocation += range
        }


        Log.d("tag", "x = ${size.x}")
        Log.d("tag", "y = ${size.y}")
    }

    private fun initMat() {
        val quest = gameData.question
        var k = 0
        for (i in 1..TOTAL_POINT_LENGTH) {
            for (j in 1..TOTAL_POINT_LENGTH) {
                mat[i - 1][j - 1] = quest[k++].toInt() - '0'.toInt()
            }
        }
    }

    private fun debugLocation() {
        for (i in 1..TOTAL_POINT_LENGTH) {
            for (j in 1..TOTAL_POINT_LENGTH) {
                print(mat[i - 1][j - 1])
            }
            println()
        }
    }


    companion object {
        val UNAVAINABLE_POINT = 0
        val READY_POINT = 5
        val ARRIVED_POINT = 2
        val START_POINT = 1
        val DESTINATION_POINT_INDEX = 9
        val TOTAL_POINT_LENGTH = 7
    }

    private fun isMovedPoint(x: Int, y: Int): Boolean {
        return mat[x][y] == ARRIVED_POINT
    }

    private fun isReadyPoint(x: Int, y: Int): Boolean {
        return mat[x][y] == READY_POINT
    }

    private fun isStartPoint(x: Int, y: Int): Boolean {
        return mat[x][y] == START_POINT
    }

    private fun isDestinationPoint(x: Int, y: Int): Boolean {
        return mat[x][y] == DESTINATION_POINT_INDEX
    }

    private fun isAvailablePoint(x: Int, y: Int): Boolean {
        return mat[x][y] > UNAVAINABLE_POINT
    }

    private fun couldMoveForward(x: Int, y: Int): Boolean {
        if (isReadyPoint(x, y) && !isStartPoint(x, y))
            return true
        return false
    }

    private fun couldMoveBack(x: Int, y: Int): Boolean {
        if (isMovedPoint(x, y) || isStartPoint(x, y))
            return true
        return false
    }


    private fun couldMoveToDestination(x: Int, y: Int): Boolean {
        for (i in 1..TOTAL_POINT_LENGTH) {
            for (j in 1..TOTAL_POINT_LENGTH) {
                if (i == x && j == y) continue
                if (mat[i - 1][j - 1] == READY_POINT)
                    return false
            }
        }
        return true
    }

    private fun markAsMoved(x: Int, y: Int) {
        mat[x][y] = ARRIVED_POINT
    }

    private fun markAsCouldMove(x: Int, y: Int) {
        mat[x][y] = READY_POINT
    }

    enum class SwipeDirection {
        DEFAULT, UP, DOWN, LEFT, RIGHT
    }
}
