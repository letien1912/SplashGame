package game.splashgame.model

import android.graphics.Bitmap

data class GamePoint(
        var type: String,
        var lot: Location,
        var lastMove: Location,
        var imgRes: Bitmap,
        var pos: Location,
        var status: Int
)

data class Location(
        var x: Float
        , var y: Float
)