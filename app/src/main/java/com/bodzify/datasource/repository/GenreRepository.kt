package com.bodzify.datasource.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bodzify.datasource.network.api.GenreApi
import com.bodzify.model.Genre
import com.bodzify.session.SessionManager
import javax.inject.Inject

class GenreRepository @Inject constructor(
    private val api: GenreApi,
    sessionManager: SessionManager
) : BaseRepository(api, sessionManager) {

    private val genresSearchedMutableLiveData = MutableLiveData<MutableList<Genre>?>()
    val genresSearchedLiveData: LiveData<MutableList<Genre>?>
        get() = genresSearchedMutableLiveData

    suspend fun search(nameFilter: String?, parentFilter: String?) = safeApiCall {
        genresSearchedMutableLiveData.postValue(api.search(
            authorization = sessionManager.getUser()!!.jwtToken.authorization,
            name = nameFilter,
            parent = parentFilter
        ).body()!!.results)
    }

    private val genreSelectedMutableLiveData = MutableLiveData<Genre>()
    val genreSelectedLiveData: LiveData<Genre>
        get() = genreSelectedMutableLiveData

    fun select(genre: Genre) {
        genreSelectedMutableLiveData.postValue(genre)
    }
}