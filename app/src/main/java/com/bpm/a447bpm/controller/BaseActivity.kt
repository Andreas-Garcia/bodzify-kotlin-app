package com.bpm.a447bpm.controller

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bpm.a447bpm.api.ApiClient
import com.bpm.a447bpm.api.ApiManager
import com.bpm.a447bpm.api.SessionManager

abstract class BaseActivity: AppCompatActivity() {

    protected lateinit var sessionManager: SessionManager
    protected lateinit var apiManager: ApiManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(this)
        apiManager = ApiManager(sessionManager, ApiClient(this))
    }
}