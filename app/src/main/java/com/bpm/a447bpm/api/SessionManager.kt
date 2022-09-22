package com.bpm.a447bpm.api

import android.content.Context
import android.content.SharedPreferences
import com.bpm.a447bpm.R
import com.bpm.a447bpm.model.JwtToken
import com.bpm.a447bpm.model.User

const val SHARED_PREFERENCE_USER_USERNAME_KEY = "USER_USERNAME"
const val SHARED_PREFERENCE_USER_PASSWORD_KEY = "USER_PASSWORD"
const val SHARED_PREFERENCE_USER_EMAIL_KEY = "USER_EMAIL"
const val SHARED_PREFERENCE_USER_JWTTOKEN_ACCESS_KEY = "USER_TOKEN_ACCESS"
const val SHARED_PREFERENCE_USER_JWTTOKEN_REFRESH_KEY = "USER_TOKEN_REFRESH"

class SessionManager {

    private var context: Context
    private var sharedPreferences: SharedPreferences

    var user: User?

    constructor (context: Context) {
        this.context = context
        sharedPreferences = context.getSharedPreferences(
            context.getString(R.string.app_name), Context.MODE_PRIVATE)

        val username = sharedPreferences.getString(SHARED_PREFERENCE_USER_USERNAME_KEY, null)
        val userJwtTokenAccess =
            sharedPreferences.getString(SHARED_PREFERENCE_USER_JWTTOKEN_ACCESS_KEY, null)
        val userJwtTokenRefresh =
            sharedPreferences.getString(SHARED_PREFERENCE_USER_JWTTOKEN_REFRESH_KEY, null)
        var jwtToken = if(userJwtTokenAccess != null && userJwtTokenRefresh != null) {
            JwtToken(userJwtTokenAccess, userJwtTokenRefresh)
        } else {
            null
        }
        user = if(username != null) {
            User(
                username,
                sharedPreferences.getString(SHARED_PREFERENCE_USER_PASSWORD_KEY, null),
                sharedPreferences.getString(SHARED_PREFERENCE_USER_EMAIL_KEY, null),
                jwtToken
            )
        } else {
            null
        }
    }

    companion object {
        const val USER_TOKEN = "user_token"
    }

    fun startSession(user: User) {
        val editor = sharedPreferences.edit()
        editor.putString(SHARED_PREFERENCE_USER_USERNAME_KEY, user.username)
        editor.putString(SHARED_PREFERENCE_USER_PASSWORD_KEY, user.password)
        editor.putString(SHARED_PREFERENCE_USER_EMAIL_KEY, user.email)
        editor.putString(SHARED_PREFERENCE_USER_JWTTOKEN_ACCESS_KEY, user.jwtToken?.access)
        editor.putString(SHARED_PREFERENCE_USER_JWTTOKEN_REFRESH_KEY, user.jwtToken?.refresh)
        editor.apply()
    }

    fun endSession() {
        val editor = sharedPreferences.edit()
        editor.putString(SHARED_PREFERENCE_USER_USERNAME_KEY, null)
        editor.putString(SHARED_PREFERENCE_USER_PASSWORD_KEY, null)
        editor.putString(SHARED_PREFERENCE_USER_EMAIL_KEY, null)
        editor.putString(SHARED_PREFERENCE_USER_JWTTOKEN_ACCESS_KEY, null)
        editor.putString(SHARED_PREFERENCE_USER_JWTTOKEN_REFRESH_KEY, null)
        editor.apply()
    }
}