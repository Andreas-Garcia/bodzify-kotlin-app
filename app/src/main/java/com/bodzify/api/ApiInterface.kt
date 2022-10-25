package com.bodzify.api

import com.bodzify.dto.JWTTokenAccessDTO
import com.bodzify.dto.ResponseJSON
import com.bodzify.model.MineSong
import com.bodzify.model.JwtToken
import com.bodzify.model.LibrarySong
import com.bodzify.model.User
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

    @GET("users/{username}/songs/")
    suspend fun searchLibrarySongs(
        @Header("Authorization") authorization: String,
        @Path("username") username: String?
    ): Response<ResponseJSON<MutableList<LibrarySong>>>

    @PUT("users/{username}/song/{song_id}")
    suspend fun updateSong(
        @Header("Authorization") authorization: String,
        @Path("username") username: String?,
        @Path("song_id") songId: String?
    ): Response<LibrarySong>

    @GET("mine/songs/")
    suspend fun digSongs(
        @Header("Authorization") authorization: String,
        @Query("source") source: String,
        @Query("query") query: String
    ): Response<ResponseJSON<MutableList<MineSong>>>

    @POST("mine/songs/download/")
    suspend fun downloadMineSong(
        @Header("Authorization") authorization: String,
        @Body externalSongRequestBody: RequestBody
    ): Response<JWTTokenAccessDTO>
}