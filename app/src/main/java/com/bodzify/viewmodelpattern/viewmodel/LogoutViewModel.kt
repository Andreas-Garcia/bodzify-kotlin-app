package com.bodzify.viewmodelpattern.viewmodel

import androidx.lifecycle.*
import com.bodzify.datasource.repository.BaseRepository
import kotlinx.coroutines.launch

class LogoutViewModel(private val baseRepositories: MutableList<BaseRepository>): ViewModel() {
    private val logoutPerformedMutableLiveData = MutableLiveData<Boolean>()
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
    fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }
}

class LogoutViewModelFactory(private val baseRepositories: MutableList<BaseRepository>)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LogoutViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LogoutViewModel(baseRepositories) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}