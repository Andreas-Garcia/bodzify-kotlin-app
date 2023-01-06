package com.bodzify.viewmodel

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bodzify.application.AppApplication
import com.bodzify.datasource.repository.GenreRepository
import com.bodzify.model.Genre
import kotlinx.coroutines.launch

class GenreViewModel(private val repository: GenreRepository) : ViewModel() {

    val genresSearched: LiveData<MutableList<Genre>?> = repository.genresSearchedLiveData

    val genreSelected: LiveData<Genre> = repository.genreSelectedLiveData

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val repository = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as AppApplication).genreRepository
                GenreViewModel(repository = repository)
            }
        }
    }

    fun search(nameFilter: String?, parentFilter: String?) = viewModelScope.launch {
        repository.search(nameFilter, parentFilter)
    }

    fun select(genre: Genre) {
        repository.select(genre)
    }
}