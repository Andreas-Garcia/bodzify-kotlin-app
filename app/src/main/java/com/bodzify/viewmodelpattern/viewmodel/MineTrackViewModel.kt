package com.bodzify.viewmodelpattern.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bodzify.model.LibraryTrack
import com.bodzify.model.MineTrack
import com.bodzify.datasource.repository.MineTrackRepository
import kotlinx.coroutines.launch

class MineTrackViewModel(private val repository: MineTrackRepository) : ViewModel() {

    val mineTracksDug: LiveData<MutableList<MineTrack>?> = repository.mineTracksDugLiveData
    val mineTrackExtracted: LiveData<LibraryTrack> = repository.mineTrackExtractedLiveData

    fun dig(query: String) = viewModelScope.launch {
        repository.dig(query)
    }

    fun extract(mineTrack: MineTrack) = viewModelScope.launch {
        repository.extract(mineTrack)
    }
}

class MineTrackViewModelFactory(private val repository: MineTrackRepository)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MineTrackViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MineTrackViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}