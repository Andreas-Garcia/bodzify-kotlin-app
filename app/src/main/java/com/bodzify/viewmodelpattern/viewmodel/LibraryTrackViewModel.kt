package com.bodzify.viewmodelpattern.viewmodel

import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bodzify.application.AppApplication
import com.bodzify.datasource.repository.LibraryTrackRepository
import com.bodzify.model.LibraryTrack
import kotlinx.coroutines.launch

class LibraryTrackViewModel (val repository: LibraryTrackRepository): ViewModel() {

    val libraryTracksSearched: LiveData<MutableList<LibraryTrack>?> =
        repository.libraryTracksSearchedLiveData
    val libraryTrackRetrieved: LiveData<LibraryTrack> = repository.libraryTrackRetrievedLiveData
    val libraryTrackUpdated: LiveData<LibraryTrack> = repository.libraryTrackUpdatedLiveData

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val repository = (this[APPLICATION_KEY] as AppApplication).libraryTrackRepository
                LibraryTrackViewModel(repository = repository)
            }
        }
    }

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