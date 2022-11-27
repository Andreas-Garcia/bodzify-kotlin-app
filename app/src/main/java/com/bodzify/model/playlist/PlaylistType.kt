package com.bodzify.model.playlist
import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class PlaylistType (
    val label: String,
) : java.io.Serializable