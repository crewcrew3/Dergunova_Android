package ru.itis.application6.services

import android.net.Uri
import ru.itis.application6.data.entities.SongEntity
import ru.itis.application6.data.repository.SongRepository
import ru.itis.application6.utils.ValidationHelper

class SongService(
    private val songRepository: SongRepository
) {

    suspend fun saveSong(
        userId: Int,
        name: String,
        author: String?,
        year: Int?,
        lyrics: String?,
        posterUrl: Uri?
    ) : Boolean {
        if (ValidationHelper.validateSongData(name, year)) {
            songRepository.saveSong(
                SongEntity(
                    userId = userId,
                    songName = name,
                    songAuthor = author,
                    songYear = year,
                    songLyrics = lyrics,
                    songPosterUrl = posterUrl?.toString(),
                )
            )
            return true
        }
        return false
    }
}