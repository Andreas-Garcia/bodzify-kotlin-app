package com.bodzify.datasource.repository

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.WorkerThread
import com.bodzify.datasource.storage.dao.PlayedTrackDao
import com.bodzify.datasource.storage.database.PlayedTrack
import com.bodzify.model.LibraryTrack
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class PlayedTrackRepository(private val playDao: PlayedTrackDao) {

    @RequiresApi(Build.VERSION_CODES.O)
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertPlayedTrack(libraryTrack: LibraryTrack) {
        playDao.insert(
            PlayedTrack(trackUuid = libraryTrack.uuid, dateTime = LocalDateTime.now().toString())
        )
    }

    val lastPlayedTrack: Flow<PlayedTrack> = playDao.getLast()
}