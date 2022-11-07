package com.bodzify.api

import com.bodzify.dto.*
import com.bodzify.model.MineTrack
import com.bodzify.model.JwtToken
import com.bodzify.model.LibraryTrack
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
    suspend fun searchLibraryTracks(
        @Header("Authorization") authorization: String,
    ): Response<PaginatedResponseDTO<MutableList<LibraryTrack>>>

    @PUT(RELATIVE_URL_WITH_API + "tracks/{trackUuid}/")
    suspend fun updateTrack(
        @Header("Authorization") authorization: String,
        @Path("trackUuid") trackUuid: String,
        @Body libraryTrackUpdateDTO: LibraryTrackUpdateDTO
    ): Response<LibraryTrack>

    @GET(RELATIVE_URL_WITH_API + "mine/tracks/")
    suspend fun digTracks(
        @Header("Authorization") authorization: String,
        @Query("source") source: String = MINE_TRACKS_SOURCE_DEFAULT,
        @Query("query") query: String,
        @Query("page") page: Int = 0,
        @Query("pageSize") pageSize: Int = 29
    ): Response<PaginatedResponseDTO<MutableList<MineTrack>>>

    @POST(RELATIVE_URL_WITH_API + "mine/tracks/download/")
    suspend fun downloadMineTrack(
        @Header("Authorization") authorization: String,
        @Body mineTrackDownloadDTO: MineTrackDownloadDTO
    ): Response<JWTTokenAccessDTO>

    companion object {
        private const val API_VERSION = "v1"
        const val RELATIVE_URL_WITH_API = "api/$API_VERSION/"
        const val MINE_TRACKS_SOURCE_DEFAULT = "myfreemp3"
    }
}