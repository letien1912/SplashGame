package game.splashgame.ui.level

import game.splashgame.base.BasePresenter
import io.reactivex.disposables.Disposable

class LevelPresenter(views: LevelContract.View): BasePresenter<LevelContract.View>(views), LevelContract.Presenter {

    private
    var subscription: Disposable? = null

    override fun onViewDestroyed() {
        subscription!!.dispose()
    }
}