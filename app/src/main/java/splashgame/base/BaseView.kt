package splashgame.base

import android.content.Context


interface BaseView {

    /**
     * start loading process
     */
    fun showError(string: String)

    /**
     * start loading process
     */
    fun showLoading()

    /**
     * start loading process
     */
    fun hideLoading()

    /**
     * terminate loading process
     */
    fun showSuccess(string: String)

    /**
     *  activity context
     */
    fun getContexts() : Context
}
