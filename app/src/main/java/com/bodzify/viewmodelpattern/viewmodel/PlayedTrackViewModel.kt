package com.bodzify.viewmodelpattern.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bodzify.application.AppApplication
import com.bodzify.model.LibraryTrack
import com.bodzify.datasource.repository.PlayedTrackRepository
import com.bodzify.datasource.storage.database.PlayedTrack
import kotlinx.coroutines.launch

class PlayedTrackViewModel(private val repository: PlayedTrackRepository) : ViewModel() {

    val lastPlayedTrack: LiveData<PlayedTrack> = repository.lastPlayedTrack.asLiveData()

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val repository = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as AppApplication).playedTrackRepository
                PlayedTrackViewModel(repository = repository)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun insert(libraryTrack: LibraryTrack) = viewModelScope.launch {
        repository.insertPlayedTrack(libraryTrack)
    }
}