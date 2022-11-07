package com.bodzify.viewmodel

import androidx.lifecycle.*
import com.bodzify.database.Play
import com.bodzify.repository.AppRepository
import kotlinx.coroutines.launch

class PlayViewModel(private val repository: AppRepository) : ViewModel() {
    fun insert(play: Play) = viewModelScope.launch {
        repository.insertPlay(play)
    }

    val lastPlay: LiveData<Play> = repository.lastPlay.asLiveData()
}

class PlayViewModelFactory(private val repository: AppRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlayViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}