package com.bodzify.api

import android.content.Context
import android.widget.Toast
import com.bodzify.R
import com.bodzify.dto.*
import com.bodzify.model.MineTrack
import com.bodzify.model.LibraryTrack
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
                        JWTRefreshTokenDto(sessionManager.getUser()!!.jwtToken.refresh))
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
                val response = apiClient.apiService.login(CredentialsDto(username, password))
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

    fun updateLibraryTrack(context: Context,
                           trackUuid: String,
                           libraryTrackUpdateDTO: LibraryTrackUpdateDto,
                           callback: (libraryTrack: LibraryTrack) -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = apiClient.apiService.updateTrack(
                    format(context.getString(R.string.api_auth_bearer_format), sessionManager.getUser()!!.jwtToken!!.access),
                    trackUuid,
                    libraryTrackUpdateDTO
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

    fun retrieveLibraryTrack(context: Context,
                          trackUuid: String,
                          callback: (librarySong: LibraryTrack) -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = apiClient.apiService.retrieveLibraryTrack(
                    format(context.getString(R.string.api_auth_bearer_format), sessionManager.getUser()!!.jwtToken!!.access),
                    trackUuid)
                if (response.body() != null) {
                    if(response.isSuccessful) {
                        callback(response.body()!!)
                    }
                    else {
                        if(response.code() == 401) {
                            refresh(context) { retrieveLibraryTrack(context, trackUuid, callback) }
                        } else {
                            Toast.makeText(context,
                                context.getString(R.string.error_occurred_label) + " " + response.toString(), Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }
                else {
                    Toast.makeText(
                        context,
                        context.getString(R.string.error_occurred_label) + " " + context.getString(R.string.error_empty_response_message),
                        Toast.LENGTH_LONG
                    )
                        .show()
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

    fun searchLibraryTracks(context: Context, callback:
        (songsExternal: PaginatedResponseDto<MutableList<LibraryTrack>>?) -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = apiClient.apiService
                    .searchLibraryTracks(
                        format(context.getString(R.string.api_auth_bearer_format), sessionManager.getUser()!!.jwtToken?.access!!))
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    if(response.code() == 401) {
                        refresh(context) { searchLibraryTracks(context, callback) }
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

    fun digTracks(context: Context,
                  query: String,
                  callback: (songsExternal: PaginatedResponseDto<MutableList<MineTrack>>?)
                  -> Unit) {
        val accessToken = sessionManager.getUser()!!.jwtToken?.access
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = apiClient.apiService
                    .digTracks(
                        format(
                            context.getString(R.string.api_auth_bearer_format),
                            accessToken!!),
                        context.getString(R.string.api_mine_song_source_myfreemp_value),
                        query)
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    if(response.code() == 401) {
                        refresh(context) { digTracks(context, query, callback) }
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

    fun downloadMineTrack(context: Context, user: User, mineSong: MineTrack) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val jwtTokenAccess = user.jwtToken.access
                val response = apiClient.apiService.downloadMineTrack(
                        format(context.getString(R.string.api_auth_bearer_format),
                        jwtTokenAccess),
                        MineTrackDownloadDto(mineSong)
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