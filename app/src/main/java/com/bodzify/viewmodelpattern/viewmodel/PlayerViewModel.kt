package com.bodzify.viewmodelpattern.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bodzify.model.LibraryTrack
import com.bodzify.model.playlist.Playlist

class PlayerViewModel: ViewModel()  {
    private val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean>
        get() = _isPlaying

    private val _playingTrack = MutableLiveData<LibraryTrack>()
    val playingTrack: LiveData<LibraryTrack>
        get() = _playingTrack

    private var _playingPlaylist = MutableLiveData<Playlist?>()
    val playingPlaylist: LiveData<Playlist?>
        get() = _playingPlaylist

    init {
        _isPlaying.value = false
    }

    fun setPlayingTrack(libraryTrack: LibraryTrack) {
        _playingTrack.value = libraryTrack
    }

    fun setPlayingPlaylist(playlist: Playlist) {
        _playingPlaylist.value = playlist
    }

    fun playPause() {
        _isPlaying.value = !_isPlaying.value!!
    }
}