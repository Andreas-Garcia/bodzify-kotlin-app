package com.bpm.a447bpm.api

import com.bpm.a447bpm.model.JwtToken
import com.bpm.a447bpm.model.Song
import com.bpm.a447bpm.model.User
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface Api {
    @GET("songs")
    suspend fun searchSongs(@Query("query") query: String): Response<MutableList<Song>>

    @POST("user/create")
    suspend fun createUser(@Body user: User, @Tag csrfToken : String): Response<User>

    @POST("auth/")
    suspend fun login(@Body requestBody: RequestBody): Response<JwtToken>
}