package com.bodzify.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bodzify.datasource.repository.GenreRepository
import com.bodzify.model.Genre
import kotlinx.coroutines.launch

class GenreViewModel(private val repository: GenreRepository) : ViewModel() {

    val genresSearched: LiveData<MutableList<Genre>?> = repository.genresSearchedLiveData

    fun search(nameFilter: String?) = viewModelScope.launch {
        repository.search(nameFilter)
    }

    val genreSelected: LiveData<Genre> = repository.genreSelectedLiveData

    fun select(genre: Genre) {
        repository.select(genre)
    }
}

class GenreViewModelFactory(private val repository: GenreRepository)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GenreViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GenreViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}