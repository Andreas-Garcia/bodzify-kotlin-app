package com.bodzify.viewmodel

import androidx.lifecycle.*
import com.bodzify.model.LibraryTrack

class PlayingTrackViewModel() : ViewModel() {
    private var _playingTrack = MutableLiveData<LibraryTrack?>()
    val playingTrack: LiveData<LibraryTrack?>
        get() = _playingTrack

    fun set(libraryTrack: LibraryTrack) {
        _playingTrack.value = libraryTrack
    }
}
