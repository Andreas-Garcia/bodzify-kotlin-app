package com.bodzify.datasource.network.api

import com.bodzify.BuildConfig
import com.bodzify.datasource.network.TokenAuthenticator
import com.bodzify.session.SessionManager
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDataSource {

    companion object {
        private const val BASE_URL = "https://bodzify.com/"
        private const val API_VERSION = "v1"
        const val BASE_URL_WITH_API_VERSION: String = "$BASE_URL" + "api/$API_VERSION/"
    }

    fun <Api> buildApi(
        api: Class<Api>,
        sessionManager: SessionManager
    ): Api {
        val authenticator = TokenAuthenticator(buildTokenApi(), sessionManager)
        return Retrofit.Builder()
            .baseUrl(BASE_URL_WITH_API_VERSION)
            .client(getRetrofitClient(authenticator))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(api)
    }

    private fun buildTokenApi(): TokenRefreshApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_WITH_API_VERSION)
            .client(getRetrofitClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TokenRefreshApi::class.java)
    }

    private fun getRetrofitClient(authenticator: Authenticator? = null): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                chain.proceed(chain.request().newBuilder().also {
                    it.addHeader("Accept", "application/json")
                }.build())
            }.also { client ->
                authenticator?.let { client.authenticator(it) }
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.level = HttpLoggingInterceptor.Level.BODY
                    client.addInterceptor(logging)
                }
            }.build()
    }
}