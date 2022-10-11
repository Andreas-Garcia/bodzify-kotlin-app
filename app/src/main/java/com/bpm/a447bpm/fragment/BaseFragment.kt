package com.bpm.a447bpm.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.bpm.a447bpm.R
import com.bpm.a447bpm.api.ApiClient
import com.bpm.a447bpm.api.ApiManager
import com.bpm.a447bpm.api.SessionManager
import com.google.android.material.bottomnavigation.BottomNavigationView

abstract class BaseFragment: Fragment() {

    protected lateinit var sessionManager: SessionManager
    protected lateinit var apiManager: ApiManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(requireContext())
        apiManager = ApiManager(sessionManager, ApiClient(requireContext()))
    }
}