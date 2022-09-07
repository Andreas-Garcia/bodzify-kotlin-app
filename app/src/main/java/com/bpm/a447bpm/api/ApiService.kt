package com.bpm.a447bpm.api

import com.bpm.a447bpm.model.Song
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("songs")
    suspend fun searchSongs(@Query("query") query: String): Response<MutableList<Song>>
}