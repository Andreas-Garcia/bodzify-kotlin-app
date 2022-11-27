package com.bodzify.ui.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.bodzify.R
import com.bodzify.session.SessionManager
import com.bodzify.ui.adapter.item.PlaylistAsymmetricItem
import com.bodzify.viewmodel.PlaylistViewModel

class PlaylistListAdapter(private val activity: Activity,
                          private val sessionManager: SessionManager,
                          private val playlistAsymmetricItems: MutableList<PlaylistAsymmetricItem>,
                          private val playlistViewModel: PlaylistViewModel
) : ArrayAdapter<PlaylistAsymmetricItem>(
    activity,
    R.layout.list_view_item_playlist,
    playlistAsymmetricItems) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val rowView = activity.layoutInflater
            .inflate(R.layout.list_view_item_playlist, null, true)

        val playlistNameTextView = rowView.findViewById<TextView>(R.id.playlist_name_textview)

        val playlistAsymmetricItem = playlistAsymmetricItems[position]
        playlistNameTextView.text = playlistAsymmetricItem.playlistName

        rowView.setOnClickListener {
            //TODO playlistViewModel...
        }

        return rowView
    }
}