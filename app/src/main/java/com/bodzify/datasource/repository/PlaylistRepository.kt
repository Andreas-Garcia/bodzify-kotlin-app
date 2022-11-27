package com.bodzify.datasource.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bodzify.datasource.network.api.PlaylistApi
import com.bodzify.model.playlist.Playlist
import com.bodzify.session.SessionManager
import javax.inject.Inject

class PlaylistRepository @Inject constructor(
    private val api: PlaylistApi,
    sessionManager: SessionManager
) : BaseRepository(api, sessionManager) {

    private val playlistsSearchedMutableLiveData = MutableLiveData<MutableList<Playlist>?>()
    val playlistsSearchedLiveData: LiveData<MutableList<Playlist>?>
        get() = playlistsSearchedMutableLiveData

    suspend fun search() = safeApiCall {
        playlistsSearchedMutableLiveData.postValue(
            api.search(sessionManager.getUser()!!.jwtToken.authorization).body()!!.results)
    }

    private val playlistRetrievedMutableLiveData = MutableLiveData<Playlist>()
    val playlistRetrievedLiveData: LiveData<Playlist>
        get() = playlistRetrievedMutableLiveData

    suspend fun retrieve(trackUuid: String) = safeApiCall {
        playlistRetrievedMutableLiveData.postValue(api.retrieve(
            sessionManager.getUser()!!.jwtToken.authorization,
            trackUuid
        ).body())
    }
}