package com.bodzify.session

import android.content.Context
import android.content.SharedPreferences
import com.bodzify.R
import com.bodzify.model.JwtToken
import com.bodzify.model.User

const val SHARED_PREFERENCE_USER_USERNAME_KEY = "USER_USERNAME"
const val SHARED_PREFERENCE_USER_PASSWORD_KEY = "USER_PASSWORD"
const val SHARED_PREFERENCE_USER_JWTTOKEN_ACCESS_KEY = "USER_TOKEN_ACCESS"
const val SHARED_PREFERENCE_USER_JWTTOKEN_REFRESH_KEY = "USER_TOKEN_REFRESH"

class SessionManager {

    private var context: Context
    private var sharedPreferences: SharedPreferences

    constructor (context: Context) {
        this.context = context
        sharedPreferences = context.getSharedPreferences(
            context.getString(R.string.app_name), Context.MODE_PRIVATE)
    }

    private fun getUserFromSharedPreferences(): User? {
        var user: User? = null
        val username = sharedPreferences.getString(SHARED_PREFERENCE_USER_USERNAME_KEY, null)
        if(username != null) {
            val password = sharedPreferences.getString(SHARED_PREFERENCE_USER_PASSWORD_KEY, null)
            val userJwtTokenAccess =
                sharedPreferences.getString(SHARED_PREFERENCE_USER_JWTTOKEN_ACCESS_KEY, null)
            val userJwtTokenRefresh =
                sharedPreferences.getString(SHARED_PREFERENCE_USER_JWTTOKEN_REFRESH_KEY, null)

            if (password != null && userJwtTokenAccess != null && userJwtTokenRefresh != null) {
                var jwtToken = JwtToken(userJwtTokenAccess, userJwtTokenRefresh)
                user = User(username, password, jwtToken)
            }
            else {
                user = null
                fetchSession(null)
            }
        }
        else
            null

        return user
    }

    fun getUser(): User? {
        return getUserFromSharedPreferences()
    }

    private fun fetchSession(user: User?) {
        val editor = sharedPreferences.edit()
        var usernameToPut: String? = null
        var passwordToPut: String? = null
        var jwtTokenAccessToPut: String? = null
        var jwtTokenRefreshToPut: String? = null

        if(user != null) {
            usernameToPut = user!!.username
            passwordToPut = user!!.password
            jwtTokenAccessToPut = user!!.jwtToken.access
            jwtTokenRefreshToPut = user!!.jwtToken.refresh
        }

        editor.putString(SHARED_PREFERENCE_USER_USERNAME_KEY, usernameToPut)
        editor.putString(SHARED_PREFERENCE_USER_PASSWORD_KEY, passwordToPut)
        editor.putString(SHARED_PREFERENCE_USER_JWTTOKEN_ACCESS_KEY, jwtTokenAccessToPut)
        editor.putString(SHARED_PREFERENCE_USER_JWTTOKEN_REFRESH_KEY, jwtTokenRefreshToPut)
        editor.apply()
    }

    fun startSession(user: User) {
        fetchSession(user)
    }

    fun endSession() {
        fetchSession(null)
    }

    fun isLoggedIn(): Boolean {
        return getUserFromSharedPreferences() != null
    }
}