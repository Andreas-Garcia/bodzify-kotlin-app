package com.bodzify.datasource.network.api

import com.bodzify.datasource.network.request.dto.LibraryTrackUpdateRequestDto
import com.bodzify.datasource.network.response.dto.PaginatedResponseDto
import com.bodzify.model.LibraryTrack
import retrofit2.Response
import retrofit2.http.*

interface LibraryTrackApi : BaseApi {

    @GET("tracks/")
    suspend fun search(
        @Header("Authorization") authorization: String,
    ): Response<PaginatedResponseDto<MutableList<LibraryTrack>?>>

    @GET("tracks/{trackUuid}/")
    suspend fun retrieve(
        @Header("Authorization") authorization: String,
        @Path("trackUuid") trackUuid: String,
    ): Response<LibraryTrack>

    @PUT("tracks/{trackUuid}/")
    suspend fun update(
        @Header("Authorization") authorization: String,
        @Path("trackUuid") trackUuid: String,
        @Body libraryTrackUpdateRequest: LibraryTrackUpdateRequestDto
    ): Response<LibraryTrack>
}