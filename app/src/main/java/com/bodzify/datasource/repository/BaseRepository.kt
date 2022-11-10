package com.bodzify.datasource.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bodzify.datasource.network.api.BaseApi
import com.bodzify.datasource.network.api.SafeApiCall

open class BaseRepository (private val api: BaseApi) : SafeApiCall {

    private val logoutPerformedMutableLiveData = MutableLiveData<Boolean>()
    val logoutPerformedLiveData: LiveData<Boolean>
        get() = logoutPerformedMutableLiveData

    suspend fun logout() = safeApiCall {
        api.logout()
        logoutPerformedMutableLiveData.postValue(true)
    }
}