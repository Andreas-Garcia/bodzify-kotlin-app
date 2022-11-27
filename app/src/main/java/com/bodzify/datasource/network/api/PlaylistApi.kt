package com.bodzify.datasource.network.api

import com.bodzify.datasource.network.response.dto.PaginatedResponseDto
import com.bodzify.model.playlist.Playlist
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface PlaylistApi : BaseApi {

    @GET("playlists/")
    suspend fun search(
        @Header("Authorization") authorization: String,
    ): Response<PaginatedResponseDto<MutableList<Playlist>?>>

    @GET("playlists/{playlistUuid}/")
    suspend fun retrieve(
        @Header("Authorization") authorization: String,
        @Path("playlistUuid") playlistUuid: String,
    ): Response<Playlist>
}