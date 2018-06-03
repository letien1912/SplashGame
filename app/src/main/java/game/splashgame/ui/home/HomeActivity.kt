package game.splashgame.ui.home

import android.content.Context
import android.os.Bundle
import game.splashgame.R
import game.splashgame.base.BaseActivity

class HomeActivity : BaseActivity<HomePresenter>(), HomeContract.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    override fun instantiatePresenter(): HomePresenter {
        return HomePresenter(this)
    }

    override fun getContexts(): Context {
        return this
    }

}
