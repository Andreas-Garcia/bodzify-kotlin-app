package com.bodzify.model
import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class MineTrack (
    val title: String,
    val artist: String,
    val duration: Int,
    val releaseOn: Int,
    val url : String,
)