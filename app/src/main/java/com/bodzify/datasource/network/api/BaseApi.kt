package com.bodzify.datasource.network.api

import okhttp3.ResponseBody
import retrofit2.http.POST

interface BaseApi {
    @POST("auth/logout/")
    suspend fun logout(): ResponseBody
}