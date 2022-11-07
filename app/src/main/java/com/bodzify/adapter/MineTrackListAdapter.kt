package com.bodzify.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bodzify.R
import com.bodzify.api.ApiManager
import com.bodzify.session.SessionManager
import com.bodzify.model.MineTrack

class MineTrackListAdapter(private val activity: Activity,
                           private val apiManager: ApiManager,
                           private val sessionManager: SessionManager,
                           private val mineTracks: MutableList<MineTrack>)
    : ArrayAdapter<MineTrack>(activity, R.layout.list_view_item_mine_track, mineTracks) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val rowView = activity.layoutInflater
            .inflate(R.layout.list_view_item_mine_track, null, true)

        val artistTextView = rowView.findViewById<TextView>(R.id.mine_track_artist_textview)
        val titleTextView = rowView.findViewById<TextView>(R.id.mine_track_title_textview)
        val downloadButton = rowView.findViewById<Button>(R.id.mine_track_download_button)

        val mineTrack = mineTracks[position]
        artistTextView.text = mineTrack.artist
        titleTextView.text = mineTrack.title

        downloadButton.setOnClickListener {
            apiManager.downloadMineTrack(activity, sessionManager.getUser()!!, mineTrack)
        }

        return rowView
    }
}