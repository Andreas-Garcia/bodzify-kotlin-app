package com.bodzify.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bodzify.session.SessionManager
import com.bodzify.viewmodel.MediaPlayerViewModel

abstract class BaseFragment: Fragment() {

    protected lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(requireContext())
    }
}