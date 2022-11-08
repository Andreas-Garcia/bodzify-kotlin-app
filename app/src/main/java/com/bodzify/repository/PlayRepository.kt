package com.bodzify.repository

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.WorkerThread
import com.bodzify.model.LibraryTrack
import com.bodzify.repository.storage.dao.PlayDao
import com.bodzify.repository.storage.database.Play
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class PlayRepository(private val playDao: PlayDao) {

    @RequiresApi(Build.VERSION_CODES.O)
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertPlay(libraryTrack: LibraryTrack) {
        playDao.insert(Play(track = libraryTrack.uuid, dateTime = LocalDateTime.now().toString()))
    }

    val lastPlay: Flow<Play> = playDao.getLast()

}