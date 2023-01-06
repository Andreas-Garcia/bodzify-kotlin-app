package com.bodzify.viewmodel

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bodzify.application.AppApplication
import com.bodzify.datasource.repository.BaseRepository
import kotlinx.coroutines.launch

class LogoutViewModel(private val baseRepositories: MutableList<BaseRepository>): ViewModel() {

    private val logoutPerformedMutableLiveData = MutableLiveData<Boolean>()

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as AppApplication)
                val baseRepositories = mutableListOf<BaseRepository>()
                baseRepositories.add(application.authRepository)
                baseRepositories.add(application.genreRepository)
                baseRepositories.add(application.libraryTrackRepository)
                baseRepositories.add(application.mineTrackRepository)
                baseRepositories.add(application.playlistRepository)
                LogoutViewModel(baseRepositories = baseRepositories)
            }
        }
    }

    val logoutPerformedLiveData: LiveData<Boolean>
        get() = logoutPerformedMutableLiveData

    fun logout() = viewModelScope.launch {
        baseRepositories[0].logout()
    }

    fun endSession() {
        baseRepositories[0].endSession()
    }

    fun observeOnceLogoutPerformed(lifecycleOwner: LifecycleOwner, observer: Observer<Boolean>) {
        logoutPerformedLiveData.observe(lifecycleOwner, observer)
        for(baseRepository in baseRepositories) {
            baseRepository.logoutPerformedLiveData.observeOnce(lifecycleOwner) {
                logoutPerformedMutableLiveData.postValue(true)
            }
        }
    }
    private fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }
}