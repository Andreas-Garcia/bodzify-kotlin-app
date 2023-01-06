package com.bodzify.viewmodel

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bodzify.application.AppApplication
import com.bodzify.datasource.repository.PlaylistRepository
import com.bodzify.model.playlist.Playlist
import kotlinx.coroutines.launch

class PlaylistViewModel(private val repository: PlaylistRepository) : ViewModel() {

    val playlistsSearched: LiveData<MutableList<Playlist>?> =
        repository.playlistsSearchedLiveData
    val playlistRetrieved: LiveData<Playlist> = repository.playlistRetrievedLiveData

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val repository = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as AppApplication).playlistRepository
                PlaylistViewModel(repository = repository)
            }
        }
    }

    fun search(nameFilter: String? = null, parentFilter: String? = null) = viewModelScope.launch {
        repository.search(nameFilter, parentFilter)
    }

    fun retrieve(playlistUuid: String) = viewModelScope.launch {
        repository.retrieve(playlistUuid)
    }
}