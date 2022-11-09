package com.bodzify.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LogoutViewModel: ViewModel() {
    private val logoutPerformedMutableLiveData = MutableLiveData<Boolean>()
    val logoutPerformedLiveData: LiveData<Boolean> get() = logoutPerformedMutableLiveData

    fun performLogout() {
        logoutPerformedMutableLiveData.value = true
    }
}