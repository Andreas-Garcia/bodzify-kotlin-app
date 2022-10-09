package com.bpm.a447bpm.controller

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bpm.a447bpm.R
import com.bpm.a447bpm.api.SessionManager

class ParametersActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.parameters)

        findViewById<ImageView>(R.id.previous_image_view).setOnClickListener{
            finish()
        }

        findViewById<Button>(R.id.logout_button).setOnClickListener{
            sessionManager.endSession()
            finish()
        }
    }
}