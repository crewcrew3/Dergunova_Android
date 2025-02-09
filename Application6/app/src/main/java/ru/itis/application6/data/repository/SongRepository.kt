package ru.itis.application6.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.itis.application6.data.dao.SongDao
import ru.itis.application6.data.entities.SongEntity

class SongRepository(
    private val songDao: SongDao,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun saveSong(song: SongEntity) {
        return withContext(ioDispatcher) {
            songDao.saveSong(song = song)
        }
    }
}