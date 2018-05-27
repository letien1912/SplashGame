package game.splashgame.service

import game.splashgame.model.GameData
import io.reactivex.Flowable

interface GameService {

    /**
     * Game all data
     */
    fun getAllGame(): Flowable<List<GameData>>

    /**
     * get all game in one level: ex: Level: Hard
     * @param: level
     */
    fun findGameByLevel(level: String): Flowable<List<GameData>>

    /**
     * get exactly game for loading game
     * @param level
     * @param number
     */
    fun findGameByLevelAndNumber(level: String, number: Int): Flowable<GameData>

    /**
     *  save game to data
     *  change status game ex: Lock -> unLock, UnLock -> Complete
     */
    fun saveGame(gameData: GameData)
}