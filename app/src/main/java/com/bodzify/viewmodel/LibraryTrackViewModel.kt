package com.bodzify.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bodzify.datasource.repository.LibraryTrackRepository
import com.bodzify.model.LibraryTrack
import kotlinx.coroutines.launch

class LibraryTrackViewModel(private val repository: LibraryTrackRepository) : ViewModel() {

    val libraryTracksSearched: LiveData<MutableList<LibraryTrack>?> =
        repository.libraryTracksLiveData
    val libraryTrackRetrieved: LiveData<LibraryTrack> = repository.libraryTrackRetrievedLiveData
    val libraryTrackUpdated: LiveData<LibraryTrack> = repository.libraryTrackUpdatedLiveData

    fun search() = viewModelScope.launch {
        repository.search()
    }

    fun retrieve(trackUuid: String) = viewModelScope.launch {
        repository.retrieve(trackUuid)
    }

    fun update(
        uuid: String,
        title: String?,
        artist: String?,
        album: String?,
        genre: String?,
        rating: Int?,
        language: String?
    ) = viewModelScope.launch {
        repository.update(
            uuid = uuid,
            title = title,
            artist = artist,
            album = album,
            genre = genre,
            rating = rating,
            language = language
        )
    }
}

class LibraryTrackViewModelFactory(private val repository: LibraryTrackRepository)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LibraryTrackViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LibraryTrackViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}