package com.bodzify.model
import androidx.annotation.Keep
import kotlinx.serialization.Serializable

const val AUTHORIZATION_PREFIX = "Bearer "

@Keep
@Serializable
data class JwtToken (
    var access: String,
    val refresh: String,
) {
    var authorization: String = "$AUTHORIZATION_PREFIX$access"
}