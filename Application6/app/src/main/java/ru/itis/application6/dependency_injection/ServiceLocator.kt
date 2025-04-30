package ru.itis.application6.dependency_injection

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import ru.itis.application6.data.SongDatabase
import ru.itis.application6.data.repository.SongRepository
import ru.itis.application6.data.repository.UserAndSongsRepository
import ru.itis.application6.data.repository.UserRepository
import ru.itis.application6.services.SongService
import ru.itis.application6.services.UserAndSongsService
import ru.itis.application6.services.UserService
import ru.itis.application6.utils.Properties

object ServiceLocator {

    private var dbInstance: SongDatabase? = null

    private var userService: UserService? = null
    private var songService: SongService? = null
    private var userAndSongsService: UserAndSongsService? = null

    private fun initDatabase(ctx: Context) {
        dbInstance = Room.databaseBuilder(ctx, SongDatabase::class.java, Properties.DATABASE_NAME)
            .build()
    }

    fun initDataLayerDependencies(ctx: Context) {
        if (dbInstance == null) {
            initDatabase(ctx)
            dbInstance?.let {

                userService = UserService(
                    userRepository = UserRepository(
                        userDao = it.userDao,
                        ioDispatcher = Dispatchers.IO,
                    )
                )

                songService = SongService(
                    songRepository = SongRepository(
                        songDao = it.songDao,
                        ioDispatcher = Dispatchers.IO,
                    )
                )

                userAndSongsService = UserAndSongsService(
                    userAndSongsRepository = UserAndSongsRepository(
                        userAndSongsDao = it.userAndSongDao,
                        ioDispatcher = Dispatchers.IO
                    )
                )
            }
        }
    }

    fun getUserService(): UserService =
        userService ?: throw IllegalStateException("User service not initialized")

    fun getSongService(): SongService =
        songService ?: throw IllegalStateException("Song service not initialized")

    fun getUserAndSongsService(): UserAndSongsService =
        userAndSongsService ?: throw IllegalStateException("UserAndSongs service not initialized")
}