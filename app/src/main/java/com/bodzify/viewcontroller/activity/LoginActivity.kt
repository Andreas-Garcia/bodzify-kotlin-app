package com.bodzify.viewcontroller.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bodzify.R
import com.bodzify.application.AppApplication
import com.bodzify.model.User
import com.bodzify.session.SessionManager
import com.bodzify.viewmodel.AuthViewModel
import com.bodzify.viewmodel.AuthViewModelFactory

class LoginActivity: AppCompatActivity() {

    private val sessionManager by lazy {SessionManager(this)}

    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory((application as AppApplication).authRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        val user = sessionManager.getUser()
        if(user != null) {
            logIn(user.username, user.password)
        }

        findViewById<Button>(R.id.login_button).setOnClickListener {

            val username = findViewById<EditText>(R.id.login_username_edit_text).text.toString()
            val password = findViewById<EditText>(R.id.login_password_edit_text).text.toString()

            if(credentialsValid()) {
                logIn(username, password)
            }
        }
    }

    private fun logIn(username: String, password: String) {
        authViewModel.login(username, password)
        authViewModel.jwtTokenGiven.observe(this) {
                jwtToken ->
            sessionManager.startSession(User(username, password, jwtToken))
            val intent = Intent(this, HomeActivity::class.java)
            ContextCompat.startActivity(this, intent, null)
        }
    }

    private fun credentialsValid(): Boolean {
        //TODO
        return true
    }
}