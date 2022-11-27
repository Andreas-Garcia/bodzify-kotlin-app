package com.bodzify.ui.activity

import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bodzify.R
import com.bodzify.application.AppApplication
import com.bodzify.datasource.repository.BaseRepository
import com.bodzify.model.LibraryTrack
import com.bodzify.session.SessionManager
import com.bodzify.ui.fragment.DigFragment
import com.bodzify.ui.fragment.LibraryFragment
import com.bodzify.ui.fragment.PlayerOverlayFragment
import com.bodzify.ui.fragment.SettingsFragment
import com.bodzify.viewmodel.*
import com.bodzify.viewmodel.util.observeOnce
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    private val sessionManager by lazy {SessionManager(this)}

    private val playerViewModel: PlayerViewModel by viewModels()
    private val playViewModel: PlayViewModel by viewModels {
        PlayViewModelFactory((application as AppApplication).playRepository)
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
        LogoutViewModelFactory(baseRepositories)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setUpBottomNavigationMenu()

        supportFragmentManager.beginTransaction().replace(
            R.id.main_fragment_container,
            LibraryFragment(libraryTrackViewModel, playlistViewModel)
        ).commit()

        playViewModel.lastPlay.observeOnce(this, Observer {
                play ->
            if(play != null) {
                libraryTrackViewModel.retrieve(play.trackUuid)
                libraryTrackViewModel.libraryTrackRetrieved.observeOnce(this) {
                    libraryTrack ->
                    if(libraryTrack != null) {
                        createPlayerOverlayFragment(libraryTrack, false)
                    }
                }
            }
        })

        playerViewModel.trackSelectedLiveData.observe(this, Observer {
            libraryTrack ->
            playViewModel.insert(libraryTrack)
            createPlayerOverlayFragment(libraryTrack, true)
        })

        logoutViewModel.observeOnceLogoutPerformed(this) {
            finish()
        }
    }

    private fun createPlayerOverlayFragment(libraryTrack: LibraryTrack, toPause: Boolean) {
        val playerOverlayFragment = PlayerOverlayFragment(libraryTrack, toPause)
        supportFragmentManager.beginTransaction().replace(
            R.id.player_overlay_fragment_container,
            playerOverlayFragment
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