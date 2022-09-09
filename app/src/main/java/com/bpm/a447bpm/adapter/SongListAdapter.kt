package com.bpm.a447bpm.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bpm.a447bpm.R
import com.bpm.a447bpm.model.Song

class SongListAdapter(private val activity: Activity, private val songs: MutableList<Song>)
    : ArrayAdapter<Song>(activity, R.layout.songs_list_view_item, songs) {

    private lateinit var songTitleArtistTextView: TextView
    private lateinit var titleTextView: TextView
    private lateinit var addButton: Button

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = activity.layoutInflater
        val rowView = inflater.inflate(R.layout.songs_list_view_item, null, true)

        val artistTextView = rowView.findViewById(R.id.song_artist_textview) as TextView
        val titleTextView = rowView.findViewById(R.id.song_title_textview) as TextView

        val song = songs[position]
        artistTextView.text = song.artist
        titleTextView.text = song.title

        return rowView
    }
}