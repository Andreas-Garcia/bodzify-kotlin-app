package com.bodzify.datasource.storage.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playedPlaylist")
class PlayedPlaylist (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "playlistUuid") val playlistUuid: String,
    @ColumnInfo(name = "datetime") val dateTime: String
)