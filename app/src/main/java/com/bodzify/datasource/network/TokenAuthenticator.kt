package com.bodzify.datasource.network
import com.bodzify.datasource.network.api.TokenRefreshApi
import com.bodzify.datasource.repository.BaseRepository
import com.bodzify.model.AccessToken
import com.bodzify.session.SessionManager
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val tokenApi: TokenRefreshApi,
    sessionManager: SessionManager
) : Authenticator, BaseRepository(tokenApi, sessionManager) {

    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {
            when (val accessToken = getUpdatedToken()) {
                is Resource.Success -> {
                    sessionManager.saveAccessToken(accessToken.value.access)
                    response.request.newBuilder()
                        .header(
                            "Authorization",
                            "Bearer ${accessToken.value.access}")
                        .build()
                }
                else -> {
                    endSession()
                    logout()
                    null
                }
            }
        }
    }

    private suspend fun getUpdatedToken(): Resource<AccessToken> {
        val refreshToken = sessionManager.getUser()!!.jwtToken.refresh
        return safeApiCall { tokenApi.refreshAccessToken(refreshToken) }
    }

}