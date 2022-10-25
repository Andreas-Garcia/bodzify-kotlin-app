package com.bodzify.api

import android.content.Context
import android.widget.Toast
import com.bodzify.R
import com.bodzify.dto.ResponseJSON
import com.bodzify.model.MineSong
import com.bodzify.model.LibrarySong
import com.bodzify.model.User
import com.bodzify.session.SessionManager
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
                    .addFormDataPart(context.getString(R.string.api_auth_refresh_refresh_field), sessionManager.getUser()!!.jwtToken.refresh).build()

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
                        context.getString(R.string.api_auth_username_field),
                        username)
                    .addFormDataPart(
                        context.getString(R.string.api_auth_password_field),
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

    fun updateLibrarySong(context: Context, librarySong: LibrarySong, callback:
        (librarySongs: ResponseJSON<MutableList<LibrarySong>>?) -> Unit) {
        val user = sessionManager.getUser()
        val accessToken = user!!.jwtToken?.access
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = apiClient.apiService.updateSong(
                    format(context.getString(R.string.api_auth_bearer_format), accessToken!!),
                    user.username,
                    librarySong.uuid
                )

            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    context.getString(R.string.error_occurred_label) + " " + e.message,
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        }
    }

    fun searchLibrarySongs(context: Context, callback:
        (songsExternal: ResponseJSON<MutableList<LibrarySong>>?) -> Unit) {
        val user = sessionManager.getUser()
        val accessToken = user!!.jwtToken?.access
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = apiClient.apiService
                    .searchLibrarySongs(
                        format(context.getString(R.string.api_auth_bearer_format),
                            accessToken!!),
                        user.username)
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
                 callback: (songsExternal: ResponseJSON<MutableList<MineSong>>?) -> Unit) {
        val accessToken = sessionManager.getUser()!!.jwtToken?.access
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = apiClient.apiService
                    .digSongs(
                        format(
                            context.getString(R.string.api_auth_bearer_format),
                            accessToken!!),
                        context.getString(R.string.api_mine_songs_source_myfreemp_value),
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

    fun downloadMineSong(context: Context, user: User, mineSong: MineSong) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val postSongUrlField = context
                    .getString(R.string.api_mine_song_url)
                val postSongTitleField = context
                    .getString(R.string.api_mine_song_title)
                val postSongArtistField = context
                    .getString(R.string.api_mine_song_artist)
                val postSongDurationField = context
                    .getString(R.string.bpm_api_songs_external_download_duration)
                val postSongDateField = context
                    .getString(R.string.api_mine_song_released_on)
                val jwtTokenAccess = user.jwtToken.access

                val downloadExternalSongRequestBody: RequestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(postSongUrlField, mineSong.url)
                    .addFormDataPart(postSongTitleField, mineSong.title)
                    .addFormDataPart(postSongArtistField, mineSong.artist)
                    .addFormDataPart(postSongDurationField, "" + mineSong.duration)
                    .addFormDataPart(postSongDateField, "" + mineSong.date)
                    .build()
                val response = apiClient.apiService
                    .downloadMineSong(format(context.getString(R.string.api_auth_bearer_format), jwtTokenAccess), downloadExternalSongRequestBody)

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