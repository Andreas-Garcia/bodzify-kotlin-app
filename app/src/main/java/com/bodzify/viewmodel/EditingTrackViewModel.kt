package com.bodzify.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bodzify.model.LibraryTrack
import com.bodzify.model.playlist.Playlist

class EditingTrackViewModel: ViewModel()  {
    private val _editingTrack = MutableLiveData<LibraryTrack>()
    val editingTrack: LiveData<LibraryTrack>
        get() = _editingTrack

    fun set(editingTrack: LibraryTrack) {
        _editingTrack.value = editingTrack
    }
}