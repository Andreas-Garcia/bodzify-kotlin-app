package com.bodzify.ui.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController
import com.bodzify.R
import com.bodzify.model.LibraryTrack
import com.bodzify.ui.fragment.LibraryFragmentDirections
import com.bodzify.viewmodel.EditingTrackViewModel
import com.bodzify.viewmodel.MediaPlayerViewModel

class LibraryTrackListAdapter(
    private val activity: Activity,
    private val tracks: MutableList<LibraryTrack>,
    private val playerViewModel: MediaPlayerViewModel,
    private val editingTrackViewModel: EditingTrackViewModel
): ArrayAdapter<LibraryTrack>(activity, R.layout.list_view_item_library_track, tracks) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val rowView = activity.layoutInflater
            .inflate(R.layout.list_view_item_library_track, null, true)

        val artistTextView = rowView.findViewById<TextView>(R.id.library_track_artist_textview)
        val titleTextView = rowView.findViewById<TextView>(R.id.library_track_title_textview)
        val genreTextView = rowView.findViewById<TextView>(R.id.library_track_genre_textView)
        val ratingImageView = rowView.findViewById<ImageView>(R.id.player_overlay_playpause_imageView)
        val syncButton = rowView.findViewById<Button>(R.id.library_track_sync_button)
        val editImageView = rowView.findViewById<ImageView>(R.id.library_track_edit_imageView)
        val playableLayout = rowView.findViewById<LinearLayout>(R.id.library_track_playable_layout)

        val track: LibraryTrack = tracks[position]
        artistTextView.text = track.artist
        titleTextView.text = track.title
        if(track.genre != null) genreTextView.text = track.genre!!.name

        syncButton.setOnClickListener {
            //TODO
        }

        editImageView.setOnClickListener {
            editingTrackViewModel.set(track)
            val action = LibraryFragmentDirections.actionLibraryFragmentToTrackEditionFragment()
            it.findNavController().navigate(action)
        }

        playableLayout.setOnClickListener {
            playerViewModel.setPlayingTrack(track)
        }
        return rowView
    }
}