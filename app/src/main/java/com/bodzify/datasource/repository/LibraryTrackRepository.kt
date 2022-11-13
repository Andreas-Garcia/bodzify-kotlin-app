package com.bodzify.datasource.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bodzify.datasource.network.api.LibraryTrackApi
import com.bodzify.datasource.network.request.dto.LibraryTrackUpdateRequestDto
import com.bodzify.model.LibraryTrack
import com.bodzify.session.SessionManager
import javax.inject.Inject

class LibraryTrackRepository @Inject constructor(
    private val api: LibraryTrackApi,
    sessionManager: SessionManager
) : BaseRepository(api, sessionManager) {

    private val libraryTracksSearchedMutableLiveData = MutableLiveData<MutableList<LibraryTrack>?>()
    val libraryTracksSearchedLiveData: LiveData<MutableList<LibraryTrack>?>
        get() = libraryTracksSearchedMutableLiveData

    suspend fun search() = safeApiCall {
        libraryTracksSearchedMutableLiveData.postValue(
            api.search(sessionManager.getUser()!!.jwtToken.authorization).body()!!.results)
    }

    private val libraryTrackRetrievedMutableLiveData = MutableLiveData<LibraryTrack>()
    val libraryTrackRetrievedLiveData: LiveData<LibraryTrack>
        get() = libraryTrackRetrievedMutableLiveData

    suspend fun retrieve(trackUuid: String) = safeApiCall {
        libraryTrackRetrievedMutableLiveData.postValue(api.retrieve(
            sessionManager.getUser()!!.jwtToken.authorization,
            trackUuid
        ).body())
    }

    private val libraryTrackUpdatedMutableLiveData = MutableLiveData<LibraryTrack>()
    val libraryTrackUpdatedLiveData: LiveData<LibraryTrack>
        get() = libraryTrackUpdatedMutableLiveData

    suspend fun update(
        uuid: String,
        title: String?,
        artist: String?,
        album: String?,
        genre: String?,
        rating: Int?,
        language: String?)
    = safeApiCall {
        libraryTrackUpdatedMutableLiveData.postValue(api.update(
            sessionManager.getUser()!!.jwtToken.authorization,
            uuid,
            LibraryTrackUpdateRequestDto(
                title = title,
                artist = artist,
                album = album,
                genre = genre,
                rating = rating,
                language= language)
        ).body())
    }
}