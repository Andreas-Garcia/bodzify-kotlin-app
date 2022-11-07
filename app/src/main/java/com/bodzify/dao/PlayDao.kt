package com.bodzify.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.bodzify.database.Play
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