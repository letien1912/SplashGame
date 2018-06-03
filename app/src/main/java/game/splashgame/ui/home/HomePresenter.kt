package game.splashgame.ui.home

import game.splashgame.base.BasePresenter
import io.reactivex.disposables.Disposable


class HomePresenter(views: HomeContract.View) : BasePresenter<HomeContract.View>(views), HomeContract.Presenter {

    private
    var subscription: Disposable? = null

    override fun onViewDestroyed() {
        subscription!!.dispose()
    }
}