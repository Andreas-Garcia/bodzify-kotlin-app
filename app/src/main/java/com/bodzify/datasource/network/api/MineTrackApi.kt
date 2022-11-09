package com.bodzify.datasource.network.api

import com.bodzify.datasource.network.request.dto.MineTrackDownloadRequestDto
import com.bodzify.datasource.network.response.dto.PaginatedResponseDto
import com.bodzify.model.LibraryTrack
import com.bodzify.model.MineTrack
import retrofit2.Response
import retrofit2.http.*

const val MINE_TRACKS_SOURCE_DEFAULT = "myfreemp3"

interface MineTrackApi : BaseApi {

    @GET("mine/tracks/")
    suspend fun dig(
        @Header("Authorization") authorization: String,
        @Query("source") source: String? = MINE_TRACKS_SOURCE_DEFAULT,
        @Query("query") query: String,
        @Query("page") page: Int = 0,
        @Query("pageSize") pageSize: Int = 29
    ): Response<PaginatedResponseDto<MutableList<MineTrack>>?>

    @POST("mine/tracks/extract/")
    suspend fun extract(
        @Header("Authorization") authorization: String,
        @Body mineTrackDownloadRequestDTO: MineTrackDownloadRequestDto
    ): Response<LibraryTrack>
}