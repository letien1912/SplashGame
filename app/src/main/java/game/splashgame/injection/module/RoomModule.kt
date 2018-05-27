package game.splashgame.injection.module

import android.app.Application
import android.arch.persistence.room.Room
import dagger.Module
import dagger.Provides
import dagger.Reusable
import game.splashgame.base.MyDatabase
import game.splashgame.data.GameRepository
import game.splashgame.service.GameService
import game.splashgame.service.impl.GameServiceImpl

@Module
@Suppress("unused")
object RoomModule {

    /**
     * initial database before use it
     */
    @Provides
    @Reusable
    @JvmStatic
    internal fun provideMyDatabase(application: Application): MyDatabase {
        return Room.databaseBuilder(application, MyDatabase::class.java, "game-db").build()
    }

    /**
     * Register Reporitory
     */
    @Provides
    @Reusable
    @JvmStatic
    internal fun provideGameDataRepository(database: MyDatabase): GameRepository {
        return database.getGameRepository()
    }

    /**
     * DI GameService
     */
    @Provides
    @Reusable
    @JvmStatic
    internal fun provideTicketDao(repository: GameRepository): GameService {
        return GameServiceImpl(repository)

    }
}