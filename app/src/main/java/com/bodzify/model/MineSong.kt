package com.bodzify.model
import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class MineSong (
    val title: String,
    val artist: String,
    val duration: Int,
    val releaseDate: Int,
    val url : String,
)