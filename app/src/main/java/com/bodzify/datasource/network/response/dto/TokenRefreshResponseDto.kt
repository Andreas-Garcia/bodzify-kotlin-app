package com.bodzify.datasource.network.response.dto

data class TokenRefreshResponseDto(
    val accessToken: String?,
    val refreshToken: String?
)