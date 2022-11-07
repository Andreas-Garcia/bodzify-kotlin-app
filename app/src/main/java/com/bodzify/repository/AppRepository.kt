package com.bodzify.repository

import androidx.annotation.WorkerThread
import com.bodzify.dao.PlayDao
import com.bodzify.database.Play
import kotlinx.coroutines.flow.Flow

class AppRepository(private val playDao: PlayDao) {

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertPlay(play: Play) {
        playDao.insert(play)
    }

    val lastPlay: Flow<Play> = playDao.getLast()

}