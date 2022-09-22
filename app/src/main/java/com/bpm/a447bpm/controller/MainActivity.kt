package com.bpm.a447bpm.controller

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bpm.a447bpm.R
import com.bpm.a447bpm.api.ApiClient
import com.bpm.a447bpm.databinding.ActivityMainBinding
import com.bpm.a447bpm.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var parameterImageView: ImageView
    private lateinit var searchButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    val response = ApiClient(getString(R.string.bpm_api_url))
                        .apiService.createUser(User("koko", "kiki"), "{% csrf_token %}")
                    if (response.body() != null) {
                        Toast.makeText(this@MainActivity, response.toString(), Toast.LENGTH_LONG)
                            .show()
                        //sessionManager.saveAuthToken(loginResponse.authToken)
                    } else {
                        Toast.makeText(this@MainActivity, "error null", Toast.LENGTH_LONG)
                            .show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, "error" + e.message, Toast.LENGTH_LONG)
                        .show()
                }
            }
        }

        val webViewButton = findViewById<Button>(R.id.webview_button)
        webViewButton.setOnClickListener{
            val intent = Intent(this, WebViewActivity::class.java)
            startActivity(intent)
        }
    }
}