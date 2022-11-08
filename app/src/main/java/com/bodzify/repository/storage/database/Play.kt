package com.bodzify.repository.storage.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "play")
class Play (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "track") val track: String,
    @ColumnInfo(name = "datetime") val dateTime: String
)