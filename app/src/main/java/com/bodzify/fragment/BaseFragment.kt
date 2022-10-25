package com.bodzify.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.bodzify.api.ApiClient
import com.bodzify.api.ApiManager
import com.bodzify.session.SessionManager

abstract class BaseFragment: Fragment() {

    protected lateinit var sessionManager: SessionManager
    protected lateinit var apiManager: ApiManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(requireContext())
        apiManager = ApiManager(sessionManager, ApiClient(requireContext()))
    }
}