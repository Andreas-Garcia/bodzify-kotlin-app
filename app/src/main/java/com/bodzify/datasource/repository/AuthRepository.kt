package com.bodzify.datasource.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bodzify.datasource.network.api.AuthApi
import com.bodzify.model.JwtToken
import com.bodzify.session.SessionManager
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: AuthApi,
    private val userPreferences: SessionManager
) : BaseRepository(api) {

    val jwtTokenGivenMutableLiveData = MutableLiveData<JwtToken>()
    val jwtTokenGivenLiveData: LiveData<JwtToken>
        get() = jwtTokenGivenMutableLiveData

    suspend fun login(
        username: String,
        password: String
    ) = safeApiCall {
        jwtTokenGivenMutableLiveData.postValue(api.login(username, password))
    }

    suspend fun saveAccessTokens(accessToken: String) {
        userPreferences.saveAccessToken(accessToken)
    }
}