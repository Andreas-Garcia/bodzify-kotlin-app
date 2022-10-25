package com.bodzify.dto
import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class JWTTokenAccessDTO (
    val access: String,
)