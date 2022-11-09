package com.bodzify.datasource.repository

import com.bodzify.datasource.network.api.BaseApi
import com.bodzify.datasource.network.api.SafeApiCall

open class BaseRepository (private val api: BaseApi) : SafeApiCall {

    suspend fun logout() = safeApiCall {
        api.logout()
    }
}