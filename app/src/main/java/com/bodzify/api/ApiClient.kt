package com.bodzify.api

import android.content.Context
import com.bodzify.R
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiClient {

    private const val BASE_URL = "https://bodzify.com/"
    private const val API_VERSION = "v1"
    lateinit var baseUrlWithVersion: String

    operator fun invoke(context: Context): ApiClient {
        this.baseUrlWithVersion = BASE_URL + "api/" + API_VERSION + "/"
        return this
    }

    private val gson: Gson by lazy {
        GsonBuilder().setLenient()
            .create()
    }

    private val httpClient: OkHttpClient by lazy {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val httpClientBuilder = OkHttpClient.Builder()
        httpClientBuilder.addInterceptor(interceptor)
        httpClientBuilder.build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrlWithVersion)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val apiService: ApiInterface by lazy {
        retrofit.create(ApiInterface::class.java)
    }
}