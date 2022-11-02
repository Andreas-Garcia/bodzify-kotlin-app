package com.bodzify.api

import com.bodzify.dto.*
import com.bodzify.model.MineSong
import com.bodzify.model.JwtToken
import com.bodzify.model.LibrarySong
import com.bodzify.model.User
import retrofit2.Response
import retrofit2.http.*


interface ApiInterface {

    @POST("user/create")
    suspend fun createUser(@Body user: User, @Tag csrfToken : String): Response<User>

    @POST("auth/token/")
    suspend fun login(@Body credentialsDTO: CredentialsDTO): Response<JwtToken>

    @POST("auth/token/refresh/")
    suspend fun refresh(@Body refreshTokenDTO: JWTRefreshTokenDTO): Response<JWTTokenAccessDTO>

    @GET("users/{username}/songs/")
    suspend fun searchLibrarySongs(
        @Header("Authorization") authorization: String,
        @Path("username") username: String?
    ): Response<PaginatedResponseDTO<MutableList<LibrarySong>>>

    @PUT("users/{username}/songs/{songUuid}/")
    suspend fun updateSong(
        @Header("Authorization") authorization: String,
        @Path("username") username: String,
        @Path("songUuid") songUuid: String,
        @Body librarySongUpdateDTO: LibrarySongUpdateDTO
    ): Response<LibrarySong>

    @GET("mine/songs/")
    suspend fun digSongs(
        @Header("Authorization") authorization: String,
        @Query("source") source: String,
        @Query("query") query: String
    ): Response<PaginatedResponseDTO<MutableList<MineSong>>>

    @POST("mine/songs/download/")
    suspend fun downloadMineSong(
        @Header("Authorization") authorization: String,
        @Body mineSongDownloadDTO: MineSongDownloadDTO
    ): Response<JWTTokenAccessDTO>
}