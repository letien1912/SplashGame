package game.splashgame.ui

import game.splashgame.base.BasePresenter
import game.splashgame.model.GameData
import game.splashgame.service.GameService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainPresenter(gameView: MainContract.View) : BasePresenter<MainContract.View>(gameView), MainContract.Presenter {

    @Inject
    lateinit var service: GameService

    private
    var subscription: Disposable? = null
    val gameQuest =
            "0000000" +
                    "0100000" +
                    "0000000" +
                    "0505005" +
                    "0000005" +
                    "0000005" +
                    "0000009"

    fun createData() {
        Observable.fromCallable {
            service.saveGame(GameData(null, gameQuest, "1", 1, "unlock"))

        }.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe {
                    getData()
                }
    }

    fun getData() {
        service.getAllGame()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe {
                    view.onGameDataLoaded(it)
                }
    }

    override fun onViewDestroyed() {
        subscription!!.dispose()
    }
}