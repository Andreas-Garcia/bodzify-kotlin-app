package com.bodzify.ui.activity

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.bodzify.R
import com.bodzify.datasource.network.api.RemoteDataSource
import com.bodzify.datasource.storage.database.PlayedTrack
import com.bodzify.model.LibraryTrack
import com.bodzify.session.SessionManager
import com.bodzify.ui.fragment.OverlayPlayerFragment
import com.bodzify.viewmodel.*
import com.bodzify.viewmodel.util.observeOnce
import com.google.android.material.bottomnavigation.BottomNavigationView


const val DEFAULT_STARTUP_PLAYLIST_NAME = "All"


class HomeActivity : AppCompatActivity() {

    private val sessionManager by lazy {SessionManager(this)}

    private val libraryTrackViewModel: LibraryTrackViewModel by viewModels {
        LibraryTrackViewModel.Factory
    }
    private val playlistViewModel: PlaylistViewModel by viewModels {
        PlaylistViewModel.Factory
    }
    private val playerViewModel: PlayerViewModel by viewModels()
    private val playedTrackViewModel: PlayedTrackViewModel by viewModels {
        PlayedTrackViewModel.Factory
    }
    private val playedPlaylistViewModel: PlayedPlaylistViewModel by viewModels {
        PlayedPlaylistViewModel.Factory
    }
    private val logoutViewModel: LogoutViewModel by viewModels {
        LogoutViewModel.Factory
    }

    private lateinit var navHostFragment: View
    private lateinit var overlayPlayer: View

    private var hasPlayerBeenStarted = false
    private lateinit var mediaPlayer: MediaPlayer

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)
        setUpBottomNavigationMenu()

        navHostFragment = findViewById(R.id.home_nav_host_fragment)

        playerViewModel.playingTrack.observe (this) {
            playingTrack: LibraryTrack? ->
            if (playingTrack != null && !hasPlayerBeenStarted) {
                createPlayerOverlayFragment()
                setUpMediaPlayer(playingTrack)
            }
        }

        playedTrackViewModel.lastPlayedTrack.observeOnce(this, Observer {
            lastPlayedTrack ->
            if(lastPlayedTrack != null) {
                loadLastPlayedTrackAndPlaylist(lastPlayedTrack)
            }
        })

        playerViewModel.playingTrack.observe(this, Observer {
            libraryTrack ->
            playedTrackViewModel.insert(libraryTrack)
        })

        logoutViewModel.observeOnceLogoutPerformed(this) {
            finish()
        }
    }

    private fun loadLastPlayedTrackAndPlaylist(lastPlayedTrack: PlayedTrack) {
        libraryTrackViewModel.retrieve(lastPlayedTrack.trackUuid)
        libraryTrackViewModel.libraryTrackRetrieved.observeOnce(this) {
                libraryTrack ->
            if(libraryTrack != null) {
                playerViewModel.setPlayingTrack(libraryTrack)
                playedPlaylistViewModel.lastPlayedPlaylist.observeOnce(this, Observer {
                        playlistPlayed ->
                    if (playlistPlayed == null) {
                        retrieveDefaultPlaylist()
                    }
                    else {
                        playlistViewModel.retrieve(playlistPlayed.playlistUuid)
                        playlistViewModel.playlistRetrieved.observeOnce(this) {
                            if (it == null) {
                                retrieveDefaultPlaylist()
                            } else {
                                playerViewModel.setPlayingPlaylist(it)
                            }
                        }
                    }
                })
            }
        }
    }

    private fun setUpMediaPlayer(playingTrack: LibraryTrack) {
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(
                RemoteDataSource.BASE_URL_WITH_API_VERSION + playingTrack.relativeUrl + "download/")
            prepare()
            start()
            pause()
        }
    }

    private fun retrieveDefaultPlaylist() {
        playlistViewModel.search(nameFilter = DEFAULT_STARTUP_PLAYLIST_NAME)
        playlistViewModel.playlistsSearched.observeOnce(this) { playlists ->
            playerViewModel.setPlayingPlaylist(playlists!![0])
        }
    }

    private fun createPlayerOverlayFragment() {
        supportFragmentManager.beginTransaction().replace(
            R.id.overlay_player_fragment_container, OverlayPlayerFragment()
        ).commit()
        var navHostLayoutParam = navHostFragment.layoutParams as ViewGroup.MarginLayoutParams
        navHostLayoutParam.setMargins(0, 0, 0, 150)
        navHostFragment.layoutParams = navHostLayoutParam
    }

    private fun setUpBottomNavigationMenu() {
        val navController: NavController = Navigation.findNavController(
            this, R.id.home_nav_host_fragment)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        setupWithNavController(bottomNavigationView, navController)
    }
}