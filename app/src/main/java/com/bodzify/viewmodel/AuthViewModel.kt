package com.bodzify.viewmodel

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bodzify.application.AppApplication
import com.bodzify.datasource.repository.AuthRepository
import com.bodzify.model.JwtToken
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    val jwtTokenGiven: LiveData<JwtToken> = repository.jwtTokenGivenLiveData

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val repository = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as AppApplication).authRepository
                AuthViewModel(repository = repository)
            }
        }
    }

    fun login(username: String, password: String) = viewModelScope.launch {
        repository.login(username, password)
    }
}