package com.bodzify.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bodzify.model.LibraryTrack

class PlayerViewModel: ViewModel()  {
    private val trackSelectedMutableLiveData = MutableLiveData<LibraryTrack>()
    val trackSelectedLiveData: LiveData<LibraryTrack> get() = trackSelectedMutableLiveData

    fun selectTrack(libraryTrack: LibraryTrack) {
        trackSelectedMutableLiveData.value = libraryTrack
    }
}