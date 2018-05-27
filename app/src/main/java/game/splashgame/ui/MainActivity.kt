package game.splashgame.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import game.splashgame.base.BaseActivity
import game.splashgame.model.GameData


class MainActivity : BaseActivity<MainPresenter>(), MainContract.View {

    companion object {
        val TAG = MainActivity::class.java.simpleName
    }

    private lateinit var gameView: GameView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.createData()
    }

    override fun onResume() {

        super.onResume()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUI()
        }
    }

    override fun onGameDataLoaded(gameData: List<GameData>) {
        Log.d(TAG, "game data with data = ${gameData[0].question} number: ${gameData[0].number}")
        gameView = GameView(this, gameData[0])
        setContentView(gameView)
    }


    private fun hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE
        val decorView = window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    override fun instantiatePresenter(): MainPresenter {
        return MainPresenter(this)
    }

    override fun getContexts(): Context {
        return this
    }

}
