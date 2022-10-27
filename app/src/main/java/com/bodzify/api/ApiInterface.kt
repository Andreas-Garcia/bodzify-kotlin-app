package com.bodzify.api

import com.bodzify.dto.*
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
    suspend fun login(@Body credentialsDTO: CredentialsDTO): Response<JwtToken>

    @POST("auth/token/refresh/")
    suspend fun refresh(@Body refreshTokenDTO: RefreshTokenDTO): Response<JWTTokenAccessDTO>

    @GET("users/{username}/songs/")
    suspend fun searchLibrarySongs(
        @Header("Authorization") authorization: String,
        @Path("username") username: String?
    ): Response<ResponseJSON<MutableList<LibrarySong>>>

    @PUT("users/{username}/songs/{song_id}/")
    suspend fun updateSong(
        @Header("Authorization") authorization: String,
        @Path("username") username: String,
        @Path("song_id") songId: String,
        @Body librarySongUpdateDTO: LibrarySongUpdateDTO
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
        @Body mineSongDownloadDTO: MineSongDownloadDTO
    ): Response<JWTTokenAccessDTO>
}