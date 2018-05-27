package game.splashgame.base

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import game.splashgame.data.GameRepository
import game.splashgame.model.GameData

@Database(entities = [(GameData::class)], version = 1)
abstract class MyDatabase : RoomDatabase() {

    abstract fun getGameRepository(): GameRepository
}