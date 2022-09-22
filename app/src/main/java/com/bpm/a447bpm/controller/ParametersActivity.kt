package com.bpm.a447bpm.controller

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bpm.a447bpm.R

class ParametersActivity : AppCompatActivity() {

    private lateinit var previousImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.parameters)

        previousImageView = findViewById(R.id.previous_image_view)
        previousImageView.setOnClickListener{
            finish()
        }
    }
}