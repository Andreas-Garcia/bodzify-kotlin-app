package com.bodzify.model.playlist
import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class Playlist (
    val uuid: String,
    val name: String,
    val type: PlaylistType,
    val trackCount: Int,
    val addedOn: String
) : java.io.Serializable