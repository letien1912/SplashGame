package game.splashgame.ui.level

import android.content.Context
import android.os.Bundle
import game.splashgame.R
import game.splashgame.base.BaseActivity

class LevelActivity : BaseActivity<LevelPresenter>(), LevelContract.View {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level)
    }

    override fun getContexts(): Context {
        return this
    }

    override fun instantiatePresenter(): LevelPresenter {
        return LevelPresenter(this)
    }

}
