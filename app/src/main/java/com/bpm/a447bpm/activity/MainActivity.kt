package com.bpm.a447bpm.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bpm.a447bpm.R
import com.bpm.a447bpm.api.ApiClient
import com.bpm.a447bpm.api.ApiManager
import com.bpm.a447bpm.api.SessionManager
import com.bpm.a447bpm.fragment.DigFragment
import com.bpm.a447bpm.fragment.LibraryFragment
import com.bpm.a447bpm.fragment.SettingsFragment
import com.bpm.a447bpm.viewmodel.LogoutViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private val logoutViewModel: LogoutViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logoutViewModel.logoutPerformed.observe(this, Observer { logoutPerformed ->
            if(logoutPerformed) {
                startLogin(SessionManager(this))
            }
        })
        displayLoginOrMain()
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
            R.id.fragment_container,
            LibraryFragment()
        ).commit()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.itemIconTintList = null

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            var selectedFragment: Fragment? = null
            when (menuItem.itemId) {
                R.id.navigation_bar_item_library -> selectedFragment = LibraryFragment()
                R.id.navigation_bar_item_dig -> selectedFragment = DigFragment()
                R.id.navigation_bar_item_settings -> selectedFragment = SettingsFragment()
            }
            supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
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