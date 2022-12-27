package com.bodzify.viewmodel

import androidx.lifecycle.*
import com.bodzify.model.LibraryTrack
import com.bodzify.model.playlist.Playlist
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlayingPlaylistViewModel() : ViewModel() {
    private var _playingPlaylist = MutableLiveData<Playlist?>()
    val playingPlaylist: LiveData<Playlist?>
        get() = _playingPlaylist

    fun set(playlist: Playlist) {
        _playingPlaylist.value = playlist
    }
}
