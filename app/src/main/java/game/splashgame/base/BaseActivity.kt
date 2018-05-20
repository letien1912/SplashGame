package game.splashgame.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
* Created by Kushina on 25/03/2018.
*/
abstract class BaseActivity<P : BasePresenter<BaseView>> : BaseView, AppCompatActivity(), BaseFragment.Callback {

    protected lateinit var presenter: P

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        Thread.setDefaultUncaughtExceptionHandler { _, _ -> Log.e("Alert", "Lets See if it Works !!!") }
        presenter = instantiatePresenter()
    }

    /**
     * Instantiates the presenter the Activity is based on.
     */
    protected abstract fun instantiatePresenter(): P


    override fun onFragmentAttached() { }

    override fun onFragmentDetached(tag: String) { }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
