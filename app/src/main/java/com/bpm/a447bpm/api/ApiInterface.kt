package com.bpm.a447bpm.api

import com.bpm.a447bpm.model.JwtToken
import com.bpm.a447bpm.dto.JWTTokenAccessDTO
import com.bpm.a447bpm.model.SongExternal
import com.bpm.a447bpm.model.User
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    @POST("user/create")
    suspend fun createUser(@Body user: User, @Tag csrfToken : String): Response<User>

    @POST("auth/token/")
    suspend fun login(@Body loginCredentialsRequestBody: RequestBody): Response<JwtToken>

    @POST("auth/token/refresh/")
    suspend fun refresh(@Body refreshTokenRequestBody: RequestBody): Response<JWTTokenAccessDTO>

    @GET("songs/external/")
    suspend fun searchSongs(
        @Header("Authorization") authorization: String,
        @Query("source") source: String,
        @Query("query") query: String
    ): Response<MutableList<SongExternal>>

    @POST("songs/external/download/")
    suspend fun downloadExternalSong(
        @Header("Authorization") authorization: String,
        @Body externalSongRequestBody: RequestBody
    ): Response<JWTTokenAccessDTO>
}