package game.splashgame.ui

import game.splashgame.base.BaseView
import game.splashgame.model.GameData

class MainContract {
    interface Presenter {

    }

    interface View: BaseView {
        fun onGameDataLoaded(gameData: List<GameData>)
    }
}
