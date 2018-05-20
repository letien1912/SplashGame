package game.splashgame.base

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log

/**
* Created by Kushina on 26/03/2018.
*/

abstract class BaseFragment<P : BasePresenter<BaseView>> : BaseView, Fragment() {

    protected lateinit var baseActivity: BaseActivity<*>
    protected lateinit var presenter: P

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.setDefaultUncaughtExceptionHandler { _, _ -> Log.e("Alert", "Lets See if it Works !!!") }
        setHasOptionsMenu(false)
        presenter = instantiatePresenter()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context is BaseActivity<*>) {
            baseActivity = context
            context.onFragmentAttached()
        }
    }

    /**
     * Instantiates the presenter the Activity is based on.
     */
    protected abstract fun instantiatePresenter(): P

    interface Callback {

        fun onFragmentAttached()

        fun onFragmentDetached(tag: String)
    }
}