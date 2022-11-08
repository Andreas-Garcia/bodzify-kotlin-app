package com.bodzify.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.bodzify.model.LibraryTrack
import com.bodzify.repository.PlayRepository
import com.bodzify.repository.storage.database.Play
import kotlinx.coroutines.launch

class PlayViewModel(private val repository: PlayRepository) : ViewModel() {

    @RequiresApi(Build.VERSION_CODES.O)
    fun insert(libraryTrack: LibraryTrack) = viewModelScope.launch {
        repository.insertPlay(libraryTrack)
    }

    val lastPlay: LiveData<Play> = repository.lastPlay.asLiveData()
}

class PlayViewModelFactory(private val repository: PlayRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlayViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}