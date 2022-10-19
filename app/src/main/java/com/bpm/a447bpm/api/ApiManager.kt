package com.bpm.a447bpm.api

import android.content.Context
import android.widget.Toast
import com.bpm.a447bpm.R
import com.bpm.a447bpm.dto.ResponseJSON
import com.bpm.a447bpm.model.SongExternal
import com.bpm.a447bpm.model.SongLibrary
import com.bpm.a447bpm.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.internal.format

class ApiManager (private val sessionManager: SessionManager, private val apiClient: ApiClient){

    private fun refresh(context: Context, callback: () -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val requestBody: RequestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(context.getString(R.string.bpm_api_auth_refresh_refresh_field), sessionManager.getUser()!!.jwtToken.refresh).build()

                val response = apiClient.apiService.refresh(requestBody)
                if (response.isSuccessful && response.body() != null) {
                    var user = sessionManager.getUser()
                    user!!.jwtToken.access = response.body()!!.access
                    sessionManager.startSession(user)
                    callback()
                } else {
                    if (response.code() == 401) {
                        login(
                            context,
                            sessionManager.getUser()!!.username,
                            sessionManager.getUser()!!.password
                        ) { callback() }
                    }
                    else {
                        callback()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(context, context.getString(R.string.error_occurred_label) + " " + e.message, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    fun login(context: Context, username: String, password: String, callback: () -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val requestBody: RequestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(
                        context.getString(R.string.bpm_api_auth_username_field),
                        username)
                    .addFormDataPart(
                        context.getString(R.string.bpm_api_auth_password_field),
                        password)
                    .build()
                val response = apiClient.apiService.login(requestBody)
                if (response.body() != null) {
                    Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show()
                    sessionManager.startSession(User(username, password, response.body()!!))
                    callback()
                } else {
                    Toast.makeText(context, "error null", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "error " + e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun searchLibrarySongs(context: Context,
                 callback: (songsExternal: ResponseJSON<MutableList<SongLibrary>>?) -> Unit) {
        val accessToken = sessionManager.getUser()!!.jwtToken?.access
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = apiClient.apiService
                    .searchLibrarySongs(format(context.getString(R.string.bpm_api_auth_bearer_format), accessToken!!))
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    if(response.code() == 401) {
                        refresh(context) { searchLibrarySongs(context, callback) }
                    } else {
                        Toast.makeText(context,
                            context.getString(R.string.error_occurred_label) + " " + response.toString(), Toast.LENGTH_LONG)
                            .show()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(context, context.getString(R.string.error_occurred_label)+ " " + e.message, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    fun digSongs(context: Context,
                 query: String,
                 callback: (songsExternal: ResponseJSON<MutableList<SongExternal>>?) -> Unit) {
        val accessToken = sessionManager.getUser()!!.jwtToken?.access
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = apiClient.apiService
                    .digSongs(
                        format(
                            context.getString(R.string.bpm_api_auth_bearer_format),
                            accessToken!!),
                        context.getString(R.string.bpm_api_songs_external_source_myfreemp_value),
                        query)
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    if(response.code() == 401) {
                        refresh(context) { digSongs(context, query, callback) }
                    } else {
                        Toast.makeText(context,
                            context.getString(R.string.error_occurred_label) + " " + response.toString(), Toast.LENGTH_LONG)
                            .show()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(context, context.getString(R.string.error_occurred_label)+ " " + e.message, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    fun downloadExternalSong(context: Context, user: User, songExternal: SongExternal) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val postSongUrlField = context
                    .getString(R.string.bpm_api_songs_external_download_url)
                val postSongTitleField = context
                    .getString(R.string.bpm_api_songs_external_download_title)
                val postSongArtistField = context
                    .getString(R.string.bpm_api_songs_external_download_artist)
                val postSongDurationField = context
                    .getString(R.string.bpm_api_songs_external_download_duration)
                val postSongDateField = context
                    .getString(R.string.bpm_api_songs_external_download_date)
                val jwtTokenAccess = user.jwtToken.access

                val downloadExternalSongRequestBody: RequestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(postSongUrlField, songExternal.url)
                    .addFormDataPart(postSongTitleField, songExternal.title)
                    .addFormDataPart(postSongArtistField, songExternal.artist)
                    .addFormDataPart(postSongDurationField, "" + songExternal.duration)
                    .addFormDataPart(postSongDateField, "" + songExternal.date)
                    .build()
                val response = apiClient.apiService
                    .downloadExternalSong(format(context.getString(R.string.bpm_api_auth_bearer_format), jwtTokenAccess), downloadExternalSongRequestBody)

                if (response.isSuccessful) {
                    Toast.makeText(context, response.body().toString(), Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context,
                        context.getString(R.string.error_occurred_label) + " " + response.toString(), Toast.LENGTH_LONG)
                        .show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}