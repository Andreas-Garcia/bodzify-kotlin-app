package com.bodzify.datasource.network.response.dto
import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class PaginatedResponseDto <T>(
    val count: Int,
    val next: String,
    val previous: String,
    val results: T
)