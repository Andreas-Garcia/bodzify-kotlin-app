package com.bodzify.repository.network.api

import com.bodzify.model.JwtToken
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthApi : BaseApi {

    @FormUrlEncoded
    @POST("auth/token/")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): JwtToken
}