package com.bodzify.activity

import android.os.Bundle
import android.provider.AlarmClock
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bodzify.R
import com.bodzify.api.ApiClient
import com.bodzify.api.ApiManager
import com.bodzify.application.AppApplication
import com.bodzify.database.Play
import com.bodzify.fragment.DigFragment
import com.bodzify.fragment.LibraryFragment
import com.bodzify.fragment.PlayerOverlayFragment
import com.bodzify.fragment.SettingsFragment
import com.bodzify.model.LibraryTrack
import com.bodzify.session.SessionManager
import com.bodzify.viewmodel.LogoutViewModel
import com.bodzify.viewmodel.PlayViewModel
import com.bodzify.viewmodel.PlayViewModelFactory
import com.bodzify.viewmodel.PlayerViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var apiManager: ApiManager

    private val logoutViewModel: LogoutViewModel by viewModels()
    private val playerViewModel: PlayerViewModel by viewModels()

    private val playViewModel: PlayViewModel by viewModels {
        PlayViewModelFactory((application as AppApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionManager = SessionManager(this)
        apiManager = ApiManager(sessionManager, ApiClient(this))

        displayLoginOrMain()

        playViewModel.lastPlay.observe(this, Observer { play ->
            apiManager.retrieveLibraryTrack(this, play.track) {
                libraryTrack -> createFragmentForTrack(libraryTrack, false)
            }
        })

        playerViewModel.trackSelectedLiveData.observe(this, Observer {
            libraryTrack -> createFragmentForTrack(libraryTrack, true)
        })

        logoutViewModel.logoutPerformedLiveData.observe(this, Observer {
            startLogin(SessionManager(this))
        })
    }

    private fun createFragmentForTrack(libraryTrack: LibraryTrack, toPause: Boolean) {
        val bundle = Bundle()
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
                ApiManager(sessionManager, ApiClient(this))
                    .login(this, username, password) {
                    startMain()
                }
            }
        }
    }

    private fun startMain() {
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(
            R.id.main_fragment_container,
            LibraryFragment()
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
                R.id.navigation_bar_item_library -> selectedFragment = LibraryFragment()
                R.id.navigation_bar_item_dig -> selectedFragment = DigFragment()
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

    companion object {
        private const val DATABASE_NAME:String = "BODZIFY"
    }
}