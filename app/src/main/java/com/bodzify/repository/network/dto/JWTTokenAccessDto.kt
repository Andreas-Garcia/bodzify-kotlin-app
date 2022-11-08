package com.bodzify.repository.network.dto
import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class JWTTokenAccessDto (
    val access: String,
)