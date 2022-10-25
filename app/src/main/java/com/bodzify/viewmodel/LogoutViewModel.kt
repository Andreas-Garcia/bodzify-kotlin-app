package com.bodzify.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LogoutViewModel: ViewModel() {
    private val mutableLogoutPerformed = MutableLiveData<Boolean>()
    val logoutPerformed: LiveData<Boolean> get() = mutableLogoutPerformed

    fun performLogout() {
        mutableLogoutPerformed.value = true
    }
}