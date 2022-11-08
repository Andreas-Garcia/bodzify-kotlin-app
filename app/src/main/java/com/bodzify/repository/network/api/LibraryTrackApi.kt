package com.bodzify.repository.network.api

import com.bodzify.model.LibraryTrack
import com.bodzify.repository.network.request.dto.LibraryTrackUpdateRequestDto
import com.bodzify.repository.network.response.dto.PaginatedResponseDto
import retrofit2.Response
import retrofit2.http.*

interface LibraryTrackApi : BaseApi {

    @GET("tracks/")
    suspend fun search(
        @Header("Authorization") accessToken: String,
    ): Response<PaginatedResponseDto<MutableList<LibraryTrack>?>>

    @GET("tracks/{trackUuid}/")
    suspend fun retrieve(
        @Header("Authorization") accessToken: String,
        @Path("trackUuid") trackUuid: String,
    ): Response<LibraryTrack>

    @PUT("tracks/{trackUuid}/")
    suspend fun update(
        @Header("Authorization") accessToken: String,
        @Path("trackUuid") trackUuid: String,
        @Body libraryTrackUpdateRequest: LibraryTrackUpdateRequestDto
    ): Response<LibraryTrack>
}