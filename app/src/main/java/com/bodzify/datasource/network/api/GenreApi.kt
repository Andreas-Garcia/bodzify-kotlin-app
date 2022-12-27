package com.bodzify.datasource.network.api

import com.bodzify.datasource.network.response.dto.PaginatedResponseDto
import com.bodzify.model.Genre
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GenreApi : BaseApi {

    @GET("genres/")
    suspend fun search(
        @Header("Authorization") authorization: String,
        @Query("name") name: String?,
        @Query("parent") parent: String?,
    ): Response<PaginatedResponseDto<MutableList<Genre>?>>
}