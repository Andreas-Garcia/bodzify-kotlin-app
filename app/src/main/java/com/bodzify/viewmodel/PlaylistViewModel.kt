package com.bodzify.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bodzify.datasource.repository.PlaylistRepository
import com.bodzify.model.playlist.Playlist
import kotlinx.coroutines.launch

class PlaylistViewModel(private val repository: PlaylistRepository) : ViewModel() {

    val playlistsSearched: LiveData<MutableList<Playlist>?> =
        repository.playlistsSearchedLiveData
    val playlistRetrieved: LiveData<Playlist> = repository.playlistRetrievedLiveData

    fun search(nameFilter: String?, parentFilter: String?) = viewModelScope.launch {
        repository.search(nameFilter, parentFilter)
    }

    fun retrieve(playlistUuid: String) = viewModelScope.launch {
        repository.retrieve(playlistUuid)
    }
}

class PlaylistViewModelFactory(private val repository: PlaylistRepository)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlaylistViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlaylistViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}