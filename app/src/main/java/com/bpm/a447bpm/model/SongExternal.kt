package com.bpm.a447bpm.model
import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class SongExternal (
    val title: String,
    val artist: String,
    val duration: Int,
    val date: Int,
    val url : String,
)