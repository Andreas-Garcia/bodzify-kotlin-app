package com.bodzify.datasource.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bodzify.datasource.network.api.BaseApi
import com.bodzify.datasource.network.api.SafeApiCall
import com.bodzify.session.SessionManager

open class BaseRepository (private val api: BaseApi,
                           protected val sessionManager: SessionManager) : SafeApiCall {

    private val logoutPerformedMutableLiveData = MutableLiveData<Boolean>()
    val logoutPerformedLiveData: LiveData<Boolean>
        get() = logoutPerformedMutableLiveData

    suspend fun logout() = safeApiCall {
        api.logout()
    }

    fun endSession() {
        logoutPerformedMutableLiveData.postValue(true)
        sessionManager.endSession()
    }
}