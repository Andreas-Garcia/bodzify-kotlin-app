package com.bodzify.viewcontroller.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.bodzify.session.SessionManager

abstract class BaseFragment: Fragment() {

    protected lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(requireContext())
    }
}