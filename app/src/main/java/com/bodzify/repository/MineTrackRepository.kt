package com.bodzify.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bodzify.model.LibraryTrack
import com.bodzify.model.MineTrack
import com.bodzify.repository.network.api.MineTrackApi
import com.bodzify.repository.network.request.dto.MineTrackDownloadRequestDto
import com.bodzify.session.SessionManager
import javax.inject.Inject

class MineTrackRepository @Inject constructor(
    private val api: MineTrackApi,
    private val userPreferences: SessionManager
) : BaseRepository(api) {

    private val mineTracksMutableLiveData = MutableLiveData<MutableList<MineTrack>?>()
    val mineTracksDugLiveData: LiveData<MutableList<MineTrack>?>
        get() = mineTracksMutableLiveData

    suspend fun dig(query: String) = safeApiCall {
        mineTracksMutableLiveData.postValue(api.dig(
            accessToken = userPreferences.getUser()!!.jwtToken.access,
            query = query
        ).body()!!.results)
    }

    private val mineTrackExtractedMutableLiveData = MutableLiveData<LibraryTrack>()
    val mineTrackExtractedLiveData: LiveData<LibraryTrack>
        get() = mineTrackExtractedMutableLiveData

    suspend fun extract(mineTrack: MineTrack) = safeApiCall {
        mineTrackExtractedMutableLiveData.postValue(api.extract(
            userPreferences.getUser()!!.jwtToken.access,
            MineTrackDownloadRequestDto(mineTrack)
        ).body())
    }
}