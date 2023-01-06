package com.bodzify.ui.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.bodzify.R
import com.bodzify.model.playlist.Playlist
import com.bodzify.session.SessionManager
import com.bodzify.ui.PlaylistGridViewItemView
import com.bodzify.viewmodelpattern.viewmodel.PlaylistViewModel


class PlaylistAdapter(private val activity: Activity,
                      private val sessionManager: SessionManager,
                      private val playlists: List<Playlist>,
                      private val playlistViewModel: PlaylistViewModel
) : BaseAdapter() {
    override fun getCount(): Int {
        return playlists.size
    }

    override fun getItem(p0: Int): Any? {
        return null
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val playlist = playlists[position]
        var playlistView = view
        if (playlistView == null) {
            playlistView = activity.layoutInflater.inflate(
                R.layout.grid_view_item_playlist,
                null,
                true) as PlaylistGridViewItemView
        }

        val nameTextView: TextView = playlistView!!.findViewById(R.id.playlist_name_textview)
        nameTextView.text = playlist.name

        playlistView.setOnClickListener {
            //TODO playlistViewModel...
        }

        return playlistView
    }
}