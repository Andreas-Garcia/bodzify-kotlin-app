package com.bodzify.viewcontroller.activity

import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bodzify.R
import com.bodzify.application.AppApplication
import com.bodzify.datasource.repository.BaseRepository
import com.bodzify.model.LibraryTrack
import com.bodzify.model.User
import com.bodzify.session.SessionManager
import com.bodzify.viewcontroller.fragment.DigFragment
import com.bodzify.viewcontroller.fragment.LibraryFragment
import com.bodzify.viewcontroller.fragment.PlayerOverlayFragment
import com.bodzify.viewcontroller.fragment.SettingsFragment
import com.bodzify.viewmodel.*
import com.bodzify.viewmodel.util.observeOnce
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    private val playerViewModel: PlayerViewModel by viewModels()
    private val playViewModel: PlayViewModel by viewModels {
        PlayViewModelFactory((application as AppApplication).playRepository)
    }
    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory((application as AppApplication).authRepository)
    }
    private val libraryTrackViewModel: LibraryTrackViewModel by viewModels {
        LibraryTrackViewModelFactory((application as AppApplication).libraryTrackRepository)
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

        sessionManager = SessionManager(this)

        displayLoginOrMain()

        playViewModel.lastPlay.observeOnce(this, Observer {
                play ->
            if(play != null) {
                libraryTrackViewModel.retrieve(play.trackUuid)
                libraryTrackViewModel.libraryTrackRetrieved.observeOnce(this) {
                    libraryTrack ->
                    createFragmentForTrack(libraryTrack, false)
                }
            }
        })

        playerViewModel.trackSelectedLiveData.observe(this, Observer {
            libraryTrack ->
            playViewModel.insert(libraryTrack)
            createFragmentForTrack(libraryTrack, true)
        })

        logoutViewModel.logoutPerformedLiveData.observe(this, Observer {
            startLogin(SessionManager(this))
        })
    }

    private fun createFragmentForTrack(libraryTrack: LibraryTrack, toPause: Boolean) {
        val playerOverlayFragment = PlayerOverlayFragment(libraryTrack, toPause)
        supportFragmentManager.beginTransaction().replace(
            R.id.player_overlay_fragment_container,
            playerOverlayFragment
        ).commit()
    }

    private fun displayLoginOrMain() {
        val sessionManager = SessionManager(this)
        if(!sessionManager.isLoggedIn())
            startLogin(sessionManager)
        else
            startMain()
    }

    private fun startLogin(sessionManager: SessionManager) {
        setContentView(R.layout.activity_login)
        findViewById<Button>(R.id.login_button).setOnClickListener {
            val username = findViewById<EditText>(R.id.login_username_edit_text).text.toString()
            val password = findViewById<EditText>(R.id.login_password_edit_text).text.toString()
            if(credentialsValid()) {
                authViewModel.login(username, password)
                authViewModel.jwtTokenGiven.observe(this) {
                    jwtToken ->
                    sessionManager.startSession(User(username, password, jwtToken))
                    startMain()
                }
            }
        }
    }

    private fun startMain() {
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(
            R.id.main_fragment_container,
            LibraryFragment(libraryTrackViewModel)
        ).commit()

        setUpBottomNavigationMenu()
    }

    private fun setUpBottomNavigationMenu() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // to let the menu icon drawables handle icons appearance changes depending on icons states
        bottomNavigationView.itemIconTintList = null

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            var selectedFragment: Fragment? = null
            when (menuItem.itemId) {
                R.id.navigation_bar_item_library ->
                    selectedFragment = LibraryFragment(libraryTrackViewModel)
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

    private fun credentialsValid(): Boolean {
        //TODO
        return true
    }
}