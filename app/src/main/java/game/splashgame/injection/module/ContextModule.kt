package game.splashgame.injection.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import game.splashgame.base.BaseView


@Module
@Suppress("unused")
object ContextModule {

    /**
     * Provides the Context
     * @param baseView the BaseView used to provides the context
     * @return the Context to be provided
     */
    @Provides
    @JvmStatic
    internal fun provideContext(baseView: BaseView): Context {
        return baseView.getContexts()
    }

    /**
     * Provides the Application Context
     * @param context Context in which the application is running
     * @return the Application Context to be provided
     */
    @Provides
    @JvmStatic
    internal fun provideApplication(context: Context): Application {
        return context.applicationContext as Application
    }

    /**
     * Provides the Shared Preferences
     * @param context Context in which the application is running
     * @return the Shared Preferences to be provied
     */
//    @Singleton
//    @Provides
//    @JvmStatic
//    internal fun provideSharedPreferences(context: Context): SharedPreferences {
//        return context.getSharedPreferences(MySharedPreference.SharedPrefKey.DATA_STORE,
//                Context.MODE_PRIVATE)
//    }
}