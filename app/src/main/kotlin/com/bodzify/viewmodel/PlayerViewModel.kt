package com.bodzify.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bodzify.model.LibrarySong

class PlayerViewModel: ViewModel()  {
    private val songSelectedMutableLiveData = MutableLiveData<LibrarySong>()
    val songSelectedLiveData: LiveData<LibrarySong> get() = songSelectedMutableLiveData

    fun selectSong(librarySong: LibrarySong) {
        songSelectedMutableLiveData.value = librarySong
    }
}