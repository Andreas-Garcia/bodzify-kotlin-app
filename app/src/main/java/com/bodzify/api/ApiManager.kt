package com.bodzify.api

import android.content.Context
import android.widget.Toast
import com.bodzify.R
import com.bodzify.dto.*
import com.bodzify.model.MineSong
import com.bodzify.model.LibrarySong
import com.bodzify.model.User
import com.bodzify.session.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.internal.format

class ApiManager (private val sessionManager: SessionManager, private val apiClient: ApiClient){

    private fun refresh(context: Context, callback: () -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = apiClient.apiService.refresh(
                        JWTRefreshTokenDTO(sessionManager.getUser()!!.jwtToken.refresh))
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
                val response = apiClient.apiService.login(CredentialsDTO(username, password))
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

    fun updateLibrarySong(context: Context,
                          trackUuid: String,
                          songUpdateDTO: LibrarySongUpdateDTO,
                          callback: (librarySong: LibrarySong) -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = apiClient.apiService.updateSong(
                    format(context.getString(R.string.api_auth_bearer_format), sessionManager.getUser()!!.jwtToken!!.access),
                    trackUuid,
                    songUpdateDTO
                )
                if (response.body() != null) {
                    if(response.isSuccessful) {
                        callback(response.body()!!)
                    }
                }
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
        (songsExternal: PaginatedResponseDTO<MutableList<LibrarySong>>?) -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = apiClient.apiService
                    .searchLibrarySongs(
                        format(context.getString(R.string.api_auth_bearer_format), sessionManager.getUser()!!.jwtToken?.access!!))
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
                 callback: (songsExternal: PaginatedResponseDTO<MutableList<MineSong>>?) -> Unit) {
        val accessToken = sessionManager.getUser()!!.jwtToken?.access
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = apiClient.apiService
                    .digSongs(
                        format(
                            context.getString(R.string.api_auth_bearer_format),
                            accessToken!!),
                        context.getString(R.string.api_mine_song_source_myfreemp_value),
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
                val jwtTokenAccess = user.jwtToken.access
                val response = apiClient.apiService.downloadMineSong(
                        format(context.getString(R.string.api_auth_bearer_format),
                        jwtTokenAccess),
                        MineSongDownloadDTO(mineSong)
                    )

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