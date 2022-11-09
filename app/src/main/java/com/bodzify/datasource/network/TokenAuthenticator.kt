package com.bodzify.datasource.network
import android.content.Context
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
    context: Context,
    private val tokenApi: TokenRefreshApi
) : Authenticator, BaseRepository(tokenApi) {

    private val appContext = context.applicationContext
    private val userPreferences = SessionManager(appContext)

    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {
            when (val accessToken = getUpdatedToken()) {
                is Resource.Success -> {
                    userPreferences.saveAccessToken(accessToken.value.access)
                    response.request.newBuilder()
                        .header(
                            "Authorization",
                            "Bearer ${accessToken.value.access}")
                        .build()
                }
                else -> null
            }
        }
    }

    private suspend fun getUpdatedToken(): Resource<AccessToken> {
        val refreshToken = userPreferences.getUser()!!.jwtToken.refresh
        return safeApiCall { tokenApi.refreshAccessToken(refreshToken) }
    }

}