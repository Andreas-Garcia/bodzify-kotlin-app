package com.bpm.a447bpm.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bpm.a447bpm.R
import com.bpm.a447bpm.api.ApiManager
import com.bpm.a447bpm.api.SessionManager
import com.bpm.a447bpm.model.MineSong

class ExternalSongListAdapter(private val activity: Activity,
                              private val apiManager: ApiManager,
                              private val sessionManager: SessionManager,
                              private val songsExternal: MutableList<MineSong>)
    : ArrayAdapter<MineSong>(activity, R.layout.list_view_item_external_song, songsExternal) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val rowView = activity.layoutInflater
            .inflate(R.layout.list_view_item_external_song, null, true)

        val artistTextView = rowView.findViewById<TextView>(R.id.external_song_artist_textview)
        val titleTextView = rowView.findViewById<TextView>(R.id.external_song_title_textview)
        val downloadButton = rowView.findViewById<Button>(R.id.external_song_download_button)

        val songExternal = songsExternal[position]
        artistTextView.text = songExternal.artist
        titleTextView.text = songExternal.title

        downloadButton.setOnClickListener {
            apiManager.downloadExternalSong(activity, sessionManager.getUser()!!, songExternal)
        }

        return rowView
    }
}