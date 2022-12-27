package com.bodzify.datasource.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.bodzify.datasource.storage.database.PlayedPlaylist
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayedPlaylistDao {
    @Insert
    suspend fun insert(playedPlaylist: PlayedPlaylist)

    @Query("" +
            "SELECT * " +
            "FROM playedPlaylist " +
            "ORDER BY datetime DESC " +
            "LIMIT 1" +
            "")
    fun getLast(): Flow<PlayedPlaylist>
}