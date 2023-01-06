package com.bodzify.viewmodelpattern.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.bodzify.model.LibraryTrack
import com.bodzify.datasource.repository.PlayedTrackRepository
import com.bodzify.datasource.storage.database.PlayedTrack
import kotlinx.coroutines.launch

class PlayedTrackViewModel(private val repository: PlayedTrackRepository) : ViewModel() {

    @RequiresApi(Build.VERSION_CODES.O)
    fun insert(libraryTrack: LibraryTrack) = viewModelScope.launch {
        repository.insertPlayedTrack(libraryTrack)
    }

    val lastPlayedTrack: LiveData<PlayedTrack> = repository.lastPlayedTrack.asLiveData()
}

class PlayedTrackViewModelFactory(private val repository: PlayedTrackRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayedTrackViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlayedTrackViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}