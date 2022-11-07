package com.bodzify.api

import com.bodzify.dto.*
import com.bodzify.model.MineSong
import com.bodzify.model.JwtToken
import com.bodzify.model.LibrarySong
import com.bodzify.model.User
import retrofit2.Response
import retrofit2.http.*


interface ApiInterface {

    @POST(RELATIVE_URL_WITH_API + "user/create")
    suspend fun createUser(@Body user: User, @Tag csrfToken : String): Response<User>

    @POST(RELATIVE_URL_WITH_API + "auth/token/")
    suspend fun login(@Body credentialsDTO: CredentialsDTO): Response<JwtToken>

    @POST(RELATIVE_URL_WITH_API + "auth/token/refresh/")
    suspend fun refresh(@Body refreshTokenDTO: JWTRefreshTokenDTO): Response<JWTTokenAccessDTO>

    @GET(RELATIVE_URL_WITH_API + "tracks/")
    suspend fun searchLibrarySongs(
        @Header("Authorization") authorization: String,
    ): Response<PaginatedResponseDTO<MutableList<LibrarySong>>>

    @PUT("/tracks/{trackUuid}/")
    suspend fun updateSong(
        @Header("Authorization") authorization: String,
        @Path("trackUuid") songUuid: String,
        @Body librarySongUpdateDTO: LibrarySongUpdateDTO
    ): Response<LibrarySong>

    @GET(RELATIVE_URL_WITH_API + "mine/tracks/")
    suspend fun digSongs(
        @Header("Authorization") authorization: String,
        @Query("source") source: String = MINE_TRACKS_SOURCE_DEFAULT,
        @Query("query") query: String,
        @Query("page") page: Int = 0,
        @Query("pageSize") pageSize: Int = 29
    ): Response<PaginatedResponseDTO<MutableList<MineSong>>>

    @POST("mine/tracks/download/")
    suspend fun downloadMineSong(
        @Header("Authorization") authorization: String,
        @Body mineSongDownloadDTO: MineSongDownloadDTO
    ): Response<JWTTokenAccessDTO>

    companion object {
        private const val API_VERSION = "v1"
        const val RELATIVE_URL_WITH_API = "api/$API_VERSION/"
        const val MINE_TRACKS_SOURCE_DEFAULT = "myfreemp3"
    }
}