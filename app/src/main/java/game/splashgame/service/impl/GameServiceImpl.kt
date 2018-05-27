package game.splashgame.service.impl

import game.splashgame.data.GameRepository
import game.splashgame.model.GameData
import game.splashgame.service.GameService
import io.reactivex.Flowable
import javax.inject.Inject

class GameServiceImpl @Inject constructor(private var repo: GameRepository) : GameService {

    override fun getAllGame(): Flowable<List<GameData>> {
        return repo.getAll().distinctUntilChanged()
    }

    override fun findGameByLevel(level: String): Flowable<List<GameData>> {
        return repo.getListGameByLevel(level).distinctUntilChanged()
    }

    override fun findGameByLevelAndNumber(level: String, number: Int): Flowable<GameData> {
        return repo.getGameByLevelAndNumber(level, number).distinctUntilChanged()
    }

    override fun saveGame(gameData: GameData) {
        return repo.insert(gameData)
    }

}