package com.bpm.a447bpm.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiClient {
    private lateinit var baseUrl: String

    operator fun invoke(baseUrl: String): ApiClient {
        this.baseUrl = baseUrl
        return this
    }

    private val gson : Gson by lazy {
        GsonBuilder().setLenient()
            .create()
    }

    private val httpClient : OkHttpClient by lazy {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val httpClientBuilder = OkHttpClient.Builder()
        httpClientBuilder.addInterceptor(interceptor)
        httpClientBuilder.build()
    }

    private val retrofit : Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val apiService : Api by lazy {
        retrofit.create(Api::class.java)
    }
}