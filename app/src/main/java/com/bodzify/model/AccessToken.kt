package com.bodzify.model
import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class AccessToken (
    var access: String
)