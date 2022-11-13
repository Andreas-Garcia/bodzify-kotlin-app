package com.bodzify.model
import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class Genre (
    val uuid: String,
    val name: String,
    val parent: String,
    val addedOn: String
) : java.io.Serializable