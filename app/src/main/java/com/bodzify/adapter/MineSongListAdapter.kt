package com.bodzify.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bodzify.R
import com.bodzify.api.ApiManager
import com.bodzify.session.SessionManager
import com.bodzify.model.MineSong

class MineSongListAdapter(private val activity: Activity,
                          private val apiManager: ApiManager,
                          private val sessionManager: SessionManager,
                          private val mineSongs: MutableList<MineSong>)
    : ArrayAdapter<MineSong>(activity, R.layout.list_view_item_mine_song, mineSongs) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val rowView = activity.layoutInflater
            .inflate(R.layout.list_view_item_mine_song, null, true)

        val artistTextView = rowView.findViewById<TextView>(R.id.mine_song_artist_textview)
        val titleTextView = rowView.findViewById<TextView>(R.id.mine_song_title_textview)
        val downloadButton = rowView.findViewById<Button>(R.id.mine_song_download_button)

        val mineSong = mineSongs[position]
        artistTextView.text = mineSong.artist
        titleTextView.text = mineSong.title

        downloadButton.setOnClickListener {
            apiManager.downloadMineSong(activity, sessionManager.getUser()!!, mineSong)
        }

        return rowView
    }
}