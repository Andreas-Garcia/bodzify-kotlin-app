package com.bodzify.ui.activity

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bodzify.R
import com.bodzify.application.AppApplication
import com.bodzify.datasource.network.api.RemoteDataSource
import com.bodzify.datasource.repository.BaseRepository
import com.bodzify.datasource.repository.LibraryTrackRepository
import com.bodzify.datasource.storage.database.PlayedTrack
import com.bodzify.model.LibraryTrack
import com.bodzify.session.SessionManager
import com.bodzify.ui.fragment.DigFragment
import com.bodzify.ui.fragment.LibraryFragment
import com.bodzify.ui.fragment.OverlayPlayerFragment
import com.bodzify.ui.fragment.SettingsFragment
import com.bodzify.viewmodel.*
import com.bodzify.viewmodel.util.observeOnce
import com.google.android.material.bottomnavigation.BottomNavigationView

const val DEFAULT_STARTUP_PLAYLIST_NAME = "All"


class HomeActivity : AppCompatActivity() {

    private val sessionManager by lazy {SessionManager(this)}

    private val playerViewModel: PlayerViewModel by viewModels()
    private val playedTrackViewModel: PlayedTrackViewModel by viewModels {
        PlayedTrackViewModelFactory((application as AppApplication).playedTrackRepository)
    }
    private val playedPlaylistViewModel: PlayedPlaylistViewModel by viewModels {
        PlayedPlaylistViewModelFactory((application as AppApplication).playedPlaylistRepository)
    }
    private val libraryTrackViewModel: LibraryTrackViewModel by viewModels {
        LibraryTrackViewModelFactory((application as AppApplication).libraryTrackRepository)
    }
    private val playlistViewModel: PlaylistViewModel by viewModels {
        PlaylistViewModelFactory((application as AppApplication).playlistRepository)
    }
    private val mineTrackViewModel: MineTrackViewModel by viewModels {
        MineTrackViewModelFactory((application as AppApplication).mineTrackRepository)
    }
    private val logoutViewModel: LogoutViewModel by viewModels {
        val baseRepositories = mutableListOf<BaseRepository>()
        baseRepositories.add((application as AppApplication).authRepository)
        baseRepositories.add((application as AppApplication).libraryTrackRepository)
        baseRepositories.add((application as AppApplication).mineTrackRepository)
        baseRepositories.add((application as AppApplication).playlistRepository)
        LogoutViewModelFactory(baseRepositories)
    }

    private var hasPlayerBeenStarted = false
    private lateinit var mediaPlayer: MediaPlayer

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setUpBottomNavigationMenu()

        supportFragmentManager.beginTransaction().replace(
            R.id.main_fragment_container,
            LibraryFragment(libraryTrackViewModel, playlistViewModel)
        ).commit()

        playerViewModel.playingTrack.observe (this) {
            playingTrack: LibraryTrack? ->
            if (playingTrack != null && !hasPlayerBeenStarted) {
                createPlayerOverlayFragment(playingTrack, true)
                setUpMediaPlayer(playingTrack)
            }
        }

        playedTrackViewModel.lastPlayedTrack.observeOnce(this, Observer {
            lastTrackPlayed ->
            loadLastPlayedTrackAndPlaylist(lastTrackPlayed)
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
        if(lastPlayedTrack != null) {
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

    private fun createPlayerOverlayFragment(initialLibraryTrack: LibraryTrack, toPause: Boolean) {
        val overlayPlayerFragment = OverlayPlayerFragment(playerViewModel, initialLibraryTrack, toPause)
        supportFragmentManager.beginTransaction().replace(
            R.id.player_overlay_fragment_container,
            overlayPlayerFragment
        ).commit()
    }

    private fun setUpBottomNavigationMenu() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // to let the menu icon drawables handle icons appearance changes depending on icons states
        bottomNavigationView.itemIconTintList = null

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            var selectedFragment: Fragment? = null
            when (menuItem.itemId) {
                R.id.navigation_bar_item_library ->
                    selectedFragment = LibraryFragment(libraryTrackViewModel, playlistViewModel)
                R.id.navigation_bar_item_dig -> selectedFragment = DigFragment(mineTrackViewModel)
                R.id.navigation_bar_item_settings -> selectedFragment = SettingsFragment()
            }

            supportFragmentManager.beginTransaction().replace(
                R.id.main_fragment_container,
                selectedFragment!!
            ).commit()
            return@setOnItemSelectedListener true
        }
    }
}