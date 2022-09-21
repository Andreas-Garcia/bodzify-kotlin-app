package com.bpm.a447bpm.api

import android.content.Context
import android.content.SharedPreferences
import com.bpm.a447bpm.R

class SessionManager (context: Context) {
    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val USER_TOKEN = "user_token"
    }

    fun saveAuthToken(token: String) {
        val editor = sharedPreferences.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun fetchAuthToken(): String? {
        return sharedPreferences.getString(USER_TOKEN, null)
    }
}