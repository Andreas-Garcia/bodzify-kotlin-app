package com.bodzify.datasource.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bodzify.datasource.network.api.MineTrackApi
import com.bodzify.datasource.network.request.dto.MineTrackDownloadRequestDto
import com.bodzify.model.LibraryTrack
import com.bodzify.model.MineTrack
import com.bodzify.session.SessionManager
import javax.inject.Inject

class MineTrackRepository @Inject constructor(
    private val api: MineTrackApi,
    sessionManager: SessionManager
) : BaseRepository(api, sessionManager) {

    private val mineTracksMutableLiveData = MutableLiveData<MutableList<MineTrack>?>()
    val mineTracksDugLiveData: LiveData<MutableList<MineTrack>?>
        get() = mineTracksMutableLiveData

    suspend fun dig(query: String) = safeApiCall {
        mineTracksMutableLiveData.postValue(api.dig(
            authorization = sessionManager.getUser()!!.jwtToken.authorization,
            query = query
        ).body()!!.results)
    }

    private val mineTrackExtractedMutableLiveData = MutableLiveData<LibraryTrack>()
    val mineTrackExtractedLiveData: LiveData<LibraryTrack>
        get() = mineTrackExtractedMutableLiveData

    suspend fun extract(mineTrack: MineTrack) = safeApiCall {
        mineTrackExtractedMutableLiveData.postValue(api.extract(
            sessionManager.getUser()!!.jwtToken.authorization,
            MineTrackDownloadRequestDto(mineTrack)
        ).body())
    }
}