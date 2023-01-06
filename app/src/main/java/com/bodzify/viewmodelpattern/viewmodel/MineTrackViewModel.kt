package com.bodzify.viewmodelpattern.viewmodel

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bodzify.application.AppApplication
import com.bodzify.model.LibraryTrack
import com.bodzify.model.MineTrack
import com.bodzify.datasource.repository.MineTrackRepository
import kotlinx.coroutines.launch

class MineTrackViewModel(private val repository: MineTrackRepository) : ViewModel() {

    val mineTracksDug: LiveData<MutableList<MineTrack>?> = repository.mineTracksDugLiveData
    val mineTrackExtracted: LiveData<LibraryTrack> = repository.mineTrackExtractedLiveData

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val repository = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as AppApplication).mineTrackRepository
                MineTrackViewModel(repository = repository)
            }
        }
    }

    fun dig(query: String) = viewModelScope.launch {
        repository.dig(query)
    }

    fun extract(mineTrack: MineTrack) = viewModelScope.launch {
        repository.extract(mineTrack)
    }
}