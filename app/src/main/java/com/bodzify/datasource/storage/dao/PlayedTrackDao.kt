package com.bodzify.datasource.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.bodzify.datasource.storage.database.PlayedTrack
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayedTrackDao {
    @Insert
    suspend fun insert(playedTrack: PlayedTrack)

    @Query("" +
            "SELECT * " +
            "FROM playedTrack " +
            "ORDER BY datetime DESC " +
            "LIMIT 1" +
            "")
    fun getLast(): Flow<PlayedTrack>
}