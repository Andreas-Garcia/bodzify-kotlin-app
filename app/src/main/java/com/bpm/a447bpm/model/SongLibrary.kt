package com.bpm.a447bpm.model
import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class SongLibrary (
    val path: String,
    val addedOn: Int
)