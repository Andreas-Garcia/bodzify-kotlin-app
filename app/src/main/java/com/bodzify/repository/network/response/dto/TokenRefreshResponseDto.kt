package com.bodzify.repository.network.response.dto

data class TokenRefreshResponseDto(
    val accessToken: String?,
    val refreshToken: String?
)