package com.bodzify.repository

import com.bodzify.repository.network.api.BaseApi
import com.bodzify.repository.network.api.SafeApiCall

open class BaseRepository (private val api: BaseApi) : SafeApiCall {

    suspend fun logout() = safeApiCall {
        api.logout()
    }
}