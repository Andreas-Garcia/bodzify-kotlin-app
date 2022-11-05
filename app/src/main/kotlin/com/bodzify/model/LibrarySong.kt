package com.bodzify.model
import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class LibrarySong (
    val uuid: String,
    val relativeUrl: String,
    val filename: String,
    val fileExtension: String,
    val title: String?,
    val artist: String?,
    val album: String?,
    val genre: String?,
    val duration: String,
    val rating: Int?,
    val language: String?,
    val releasedOn: String?,
    val addedOn: String
) : java.io.Serializable