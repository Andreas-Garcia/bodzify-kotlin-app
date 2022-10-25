package com.bodzify.dto
import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class ResponseJSON <T>(
    val count: Int,
    val next: String,
    val previous: String,
    val data: T
)