package com.bodzify.api

import com.bodzify.dto.*
import com.bodzify.model.MineTrack
import com.bodzify.model.JwtToken
import com.bodzify.model.LibraryTrack
import com.bodzify.model.User
import retrofit2.Response
import retrofit2.http.*


interface ApiInterface {

    @POST("user/create")
    suspend fun createUser(@Body user: User, @Tag csrfToken : String): Response<User>

    @POST("auth/token/")
    suspend fun login(@Body credentialsDTO: CredentialsDto): Response<JwtToken>

    @POST("auth/token/refresh/")
    suspend fun refresh(@Body refreshTokenDTO: JWTRefreshTokenDto): Response<JWTTokenAccessDto>

    @GET("tracks/")
    suspend fun searchLibraryTracks(
        @Header("Authorization") authorization: String,
    ): Response<PaginatedResponseDto<MutableList<LibraryTrack>>>

    @GET("tracks/{trackUuid}/")
    suspend fun retrieveLibraryTrack(
        @Header("Authorization") authorization: String,
        @Path("trackUuid") trackUuid: String,
    ): Response<LibraryTrack>

    @PUT("tracks/{trackUuid}/")
    suspend fun updateTrack(
        @Header("Authorization") authorization: String,
        @Path("trackUuid") trackUuid: String,
        @Body libraryTrackUpdateDTO: LibraryTrackUpdateDto
    ): Response<LibraryTrack>

    @GET("mine/tracks/")
    suspend fun digTracks(
        @Header("Authorization") authorization: String,
        @Query("source") source: String = MINE_TRACKS_SOURCE_DEFAULT,
        @Query("query") query: String,
        @Query("page") page: Int = 0,
        @Query("pageSize") pageSize: Int = 29
    ): Response<PaginatedResponseDto<MutableList<MineTrack>>>

    @POST("mine/tracks/download/")
    suspend fun downloadMineTrack(
        @Header("Authorization") authorization: String,
        @Body mineTrackDownloadDTO: MineTrackDownloadDto
    ): Response<JWTTokenAccessDto>

    companion object {
        const val MINE_TRACKS_SOURCE_DEFAULT = "myfreemp3"
    }
}