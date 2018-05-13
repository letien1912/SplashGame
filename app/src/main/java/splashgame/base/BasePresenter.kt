package splashgame.base

import splashgame.injection.component.PresenterInjector
import splashgame.injection.module.ContextModule

/**
* Created by Kushina on 25/03/2018.
*/

abstract class BasePresenter<out V : BaseView>(protected val view: V) {

    private val injector: PresenterInjector = DaggerPresenterInjector
            .builder()
            .baseView(view)
            .contextModule(ContextModule)
            .build()
    init {
        inject()
    }

    /**
     *  THis method may be called when the presenter is created
     */
    open fun onViewCreated(){}

    /**
     * This method may be called when the presenter view is destroyed
     */
    open fun onViewDestroyed(){}

    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when (this) {
        }
    }


}