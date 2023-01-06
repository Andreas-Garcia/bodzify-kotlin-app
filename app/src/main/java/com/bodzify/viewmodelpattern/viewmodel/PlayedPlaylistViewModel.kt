package com.bodzify.viewmodelpattern.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bodzify.application.AppApplication
import com.bodzify.datasource.repository.PlayedPlaylistRepository
import com.bodzify.datasource.storage.database.PlayedPlaylist
import com.bodzify.model.playlist.Playlist
import kotlinx.coroutines.launch

class PlayedPlaylistViewModel(private val repository: PlayedPlaylistRepository) : ViewModel() {

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val repository = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as AppApplication).playedPlaylistRepository
                PlayedPlaylistViewModel(repository = repository)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun insert(playlist: Playlist) = viewModelScope.launch {
        repository.insertPlayedPlaylist(playlist)
    }

    val lastPlayedPlaylist: LiveData<PlayedPlaylist> = repository.lastPlayedPlaylist.asLiveData()
}