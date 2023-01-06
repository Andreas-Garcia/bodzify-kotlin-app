package com.bodzify.viewmodelpattern.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.bodzify.datasource.repository.PlayedPlaylistRepository
import com.bodzify.datasource.storage.database.PlayedPlaylist
import com.bodzify.model.playlist.Playlist
import kotlinx.coroutines.launch

class PlayedPlaylistViewModel(private val repository: PlayedPlaylistRepository) : ViewModel() {

    @RequiresApi(Build.VERSION_CODES.O)
    fun insert(playlist: Playlist) = viewModelScope.launch {
        repository.insertPlayedPlaylist(playlist)
    }

    val lastPlayedPlaylist: LiveData<PlayedPlaylist> = repository.lastPlayedPlaylist.asLiveData()
}

class PlayedPlaylistViewModelFactory(private val repository: PlayedPlaylistRepository)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayedPlaylistViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlayedPlaylistViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}