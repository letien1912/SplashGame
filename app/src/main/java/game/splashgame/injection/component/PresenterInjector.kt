package game.splashgame.injection.component

import game.splashgame.injection.module.ContextModule
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import game.splashgame.base.BaseView
import game.splashgame.injection.module.RoomModule
import game.splashgame.ui.MainPresenter
import game.splashgame.ui.home.HomePresenter
import game.splashgame.ui.level.LevelPresenter
import javax.inject.Singleton

/**
* Created by Kushina on 25/03/2018.
*/

@Singleton
@Component(modules = [(ContextModule::class), (RoomModule::class)])
interface PresenterInjector {

    /**
     * using for get saved data from client
     */
//    fun getSharedPreferences(): SharedPreferences

    fun inject(presenter: MainPresenter)
    fun inject(presenter: LevelPresenter)
    fun inject(presenter: HomePresenter)

    @Component.Builder
    interface Builder {
        fun build(): PresenterInjector

        fun contextModule(contextModule: ContextModule): Builder
        fun roomModule(roomModule: RoomModule): Builder

        @BindsInstance
        fun baseView(baseView: BaseView): Builder
    }

}
