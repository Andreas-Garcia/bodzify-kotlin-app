package com.bodzify.datasource.network.api

import com.bodzify.model.AccessToken
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface TokenRefreshApi: BaseApi {
    @FormUrlEncoded
    @POST("auth/token/refresh")
    suspend fun refreshAccessToken(
        @Field("refresh") refreshToken: String?
    ): AccessToken
}