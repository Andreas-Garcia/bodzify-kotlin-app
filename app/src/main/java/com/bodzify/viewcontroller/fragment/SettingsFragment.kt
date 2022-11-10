package com.bodzify.viewcontroller.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import com.bodzify.R
import com.bodzify.viewmodel.LogoutViewModel

class SettingsFragment : BaseFragment() {
    private val logoutViewModel: LogoutViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireView().findViewById<Button>(R.id.logout_button).setOnClickListener {
            sessionManager.endSession()
            logoutViewModel.logout()
        }
    }
}