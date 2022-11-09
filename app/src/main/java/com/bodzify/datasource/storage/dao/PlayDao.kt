package com.bodzify.datasource.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.bodzify.datasource.storage.database.Play
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayDao {
    @Insert
    suspend fun insert(play: Play)

    @Query("" +
            "SELECT * " +
            "FROM play " +
            "ORDER BY datetime DESC " +
            "LIMIT 1" +
            "")
    fun getLast(): Flow<Play>
}