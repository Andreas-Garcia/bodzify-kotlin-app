package com.bpm.a447bpm.controller

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.bpm.a447bpm.R

class MainActivity : BaseActivity() {

    private lateinit var parameterImageView: ImageView
    private lateinit var searchButton: Button

    override fun onResume() {
        super.onResume()
        displayLoginOrMain()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        displayLoginOrMain()
    }

    private fun displayLoginOrMain() {
        if(!sessionManager.isLoggedIn())
            startLogin()
        else
            startMain()
    }

    private fun startMain() {
        setContentView(R.layout.activity_main)

        parameterImageView = findViewById(R.id.parameter_image_view)
        parameterImageView.setOnClickListener {
            startActivity(Intent(this, ParametersActivity::class.java))
        }

        searchButton = findViewById<Button>(R.id.search_button)
        searchButton.setOnClickListener{
            startActivity(Intent(this, SearchActivity::class.java))
        }

        val addUserButton = findViewById<Button>(R.id.add_user_button)
        addUserButton.setOnClickListener {
        }

        val webViewButton = findViewById<Button>(R.id.webview_button)
        webViewButton.setOnClickListener{
            val intent = Intent(this, WebViewActivity::class.java)
            startActivity(intent)
        }
    }

    private fun startLogin() {
        setContentView(R.layout.login)
        findViewById<Button>(R.id.login_button).setOnClickListener {
            val username = findViewById<EditText>(R.id.login_username_edit_text).text.toString()
            val password = findViewById<EditText>(R.id.login_password_edit_text).text.toString()
            if(credentialsValid()) {
                apiManager.login(this, username, password) {
                    startMain()
                }
            }
        }
    }

    private fun credentialsValid(): Boolean {
        //TODO
        return true
    }
}