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
    private val userPreferences: SessionManager
) : BaseRepository(api) {

    private val libraryTracksMutableLiveData = MutableLiveData<MutableList<LibraryTrack>?>()
    val libraryTracksLiveData: LiveData<MutableList<LibraryTrack>?>
        get() = libraryTracksMutableLiveData

    suspend fun search() = safeApiCall {
        libraryTracksMutableLiveData.postValue(
            api.search(userPreferences.getUser()!!.jwtToken.authorization).body()!!.results)
    }

    private val libraryTrackMutableLiveData = MutableLiveData<LibraryTrack>()
    val libraryTrackRetrievedLiveData: LiveData<LibraryTrack>
        get() = libraryTrackMutableLiveData

    suspend fun retrieve(trackUuid: String) = safeApiCall {
        libraryTrackMutableLiveData.postValue(api.retrieve(
            userPreferences.getUser()!!.jwtToken.authorization,
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
            userPreferences.getUser()!!.jwtToken.authorization,
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