package game.splashgame.injection.component

import game.splashgame.injection.module.ContextModule
import dagger.BindsInstance
import dagger.Component
import game.splashgame.base.BaseView
import javax.inject.Singleton

/**
* Created by Kushina on 25/03/2018.
*/

@Singleton
@Component(modules = [(ContextModule::class)])
interface PresenterInjector {

    /**
     * using for get saved data from client
     */
//    fun getSharedPreferences(): SharedPreferences

    @Component.Builder
    interface Builder {
        fun build(): PresenterInjector

        fun contextModule(contextModule: ContextModule): Builder

        @BindsInstance
        fun baseView(baseView: BaseView): Builder
    }

}
