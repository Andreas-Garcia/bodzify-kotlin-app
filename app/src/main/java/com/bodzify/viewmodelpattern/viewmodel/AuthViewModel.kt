package com.bodzify.viewmodelpattern.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bodzify.datasource.repository.AuthRepository
import com.bodzify.model.JwtToken
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    val jwtTokenGiven: LiveData<JwtToken> = repository.jwtTokenGivenLiveData

    fun login(username: String, password: String) = viewModelScope.launch {
        repository.login(username, password)
    }
}

class AuthViewModelFactory(private val repository: AuthRepository)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}