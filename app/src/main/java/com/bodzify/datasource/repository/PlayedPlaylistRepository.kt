package com.bodzify.datasource.repository

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.WorkerThread
import com.bodzify.datasource.storage.dao.PlayedPlaylistDao
import com.bodzify.datasource.storage.database.PlayedPlaylist
import com.bodzify.model.playlist.Playlist
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class PlayedPlaylistRepository(private val playedPlaylistDao: PlayedPlaylistDao) {

    @RequiresApi(Build.VERSION_CODES.O)
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertPlayedPlaylist(playlist: Playlist) {
        playedPlaylistDao.insert(
            PlayedPlaylist(playlistUuid = playlist.uuid, dateTime = LocalDateTime.now().toString())
        )
    }

    val lastPlayedPlaylist: Flow<PlayedPlaylist> = playedPlaylistDao.getLast()
}