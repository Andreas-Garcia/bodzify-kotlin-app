package com.bpm.a447bpm.model
import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class User (
    val username: String,
    val password: String?,
    val email: String?,
    val jwtToken: JwtToken?
)