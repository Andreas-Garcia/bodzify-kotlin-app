package com.bpm.a447bpm.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bpm.a447bpm.R
import com.bpm.a447bpm.api.ApiManager
import com.bpm.a447bpm.api.SessionManager
import com.bpm.a447bpm.model.SongExternal

class SongListAdapter(private val activity: Activity,
                      private val apiManager: ApiManager,
                      private val sessionManager: SessionManager,
                      private val songsExternal: MutableList<SongExternal>)
    : ArrayAdapter<SongExternal>(activity, R.layout.songs_list_view_item, songsExternal) {

    private lateinit var songTitleArtistTextView: TextView
    private lateinit var titleTextView: TextView
    private lateinit var addButton: Button

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val rowView = activity.layoutInflater
            .inflate(R.layout.songs_list_view_item, null, true)

        val artistTextView = rowView.findViewById<TextView>(R.id.song_artist_textview)
        val titleTextView = rowView.findViewById<TextView>(R.id.song_title_textview)
        val downloadButton = rowView.findViewById<Button>(R.id.add_button)

        val songExternal = songsExternal[position]
        artistTextView.text = songExternal.artist
        titleTextView.text = songExternal.title

        downloadButton.setOnClickListener {
            apiManager.downloadExternalSong(activity, sessionManager.getUser()!!, songExternal)
        }

        return rowView
    }
}