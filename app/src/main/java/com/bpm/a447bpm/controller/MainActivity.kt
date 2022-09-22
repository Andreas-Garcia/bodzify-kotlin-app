package com.bpm.a447bpm.controller

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bpm.a447bpm.R
import com.bpm.a447bpm.api.ApiClient
import com.bpm.a447bpm.api.SessionManager
import com.bpm.a447bpm.model.JwtToken
import com.bpm.a447bpm.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class MainActivity : AppCompatActivity() {

    private lateinit var parameterImageView: ImageView
    private lateinit var searchButton: Button
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionManager = SessionManager(this)

        if(sessionManager.user == null)
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
                login(username, password)
            }
        }
    }

    private fun login(username: String, password: String) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val requestBody: RequestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(getString(R.string.bpm_api_auth_username_field), username)
                    .addFormDataPart(getString(R.string.bpm_api_auth_password_field), password)
                    .build()
                val response = ApiClient(getString(R.string.bpm_api_url))
                    .apiService.login(requestBody)
                if (response.body() != null) {
                    Toast.makeText(this@MainActivity, response.toString(), Toast.LENGTH_LONG)
                        .show()
                    sessionManager.startSession(
                        User(username, password, null, response.body()!!))
                    startMain()
                } else {
                    Toast.makeText(this@MainActivity, "error null", Toast.LENGTH_LONG)
                        .show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "error " + e.message, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun credentialsValid(): Boolean {
        return true
    }
}