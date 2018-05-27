package game.splashgame.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import game.splashgame.model.GameData
import io.reactivex.Flowable

@Dao
interface GameRepository {

    @Query("SELECT * from GameData")
    fun getAll(): Flowable<List<GameData>>

    @Query("SELECT * from GameData WHERE level = :level")
    fun getListGameByLevel(level: String): Flowable<List<GameData>>

    @Query("SELECT * from GameData WHERE level = :level AND number = :number")
    fun getGameByLevelAndNumber(level: String, number: Int): Flowable<GameData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(gameData: GameData)

}