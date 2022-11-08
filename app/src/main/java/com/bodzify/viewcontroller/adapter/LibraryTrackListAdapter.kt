package com.bodzify.viewcontroller.adapter

import android.app.Activity
import android.content.Intent
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import com.bodzify.R
import com.bodzify.model.LibraryTrack
import com.bodzify.viewcontroller.activity.TrackEditionActivity
import com.bodzify.viewmodel.PlayerViewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class LibraryTrackListAdapter(
    private val activity: Activity,
    private val libraryTracks: MutableList<LibraryTrack>,
    private val playerViewModel: PlayerViewModel
)
    : ArrayAdapter<LibraryTrack>(activity, R.layout.list_view_item_library_track, libraryTracks) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val rowView = activity.layoutInflater
            .inflate(R.layout.list_view_item_library_track, null, true)

        val artistTextView = rowView.findViewById<TextView>(R.id.library_track_artist_textview)
        val titleTextView = rowView.findViewById<TextView>(R.id.library_track_title_textview)
        val genreTextView = rowView.findViewById<TextView>(R.id.library_track_genre_textView)
        val ratingImageView = rowView.findViewById<ImageView>(R.id.player_overlay_play_pause_imageView)
        val syncButton = rowView.findViewById<Button>(R.id.library_track_sync_button)
        val editImageView = rowView.findViewById<ImageView>(R.id.library_track_edit_imageView)
        val playableLayout = rowView.findViewById<LinearLayout>(R.id.library_track_playable_layout)

        val libraryTrack: LibraryTrack = libraryTracks[position]
        artistTextView.text = libraryTrack.artist
        titleTextView.text = libraryTrack.title
        genreTextView.text = libraryTrack.genre

        syncButton.setOnClickListener {
            //TODO
        }

        editImageView.setOnClickListener {
            val intent = Intent(
                this@LibraryTrackListAdapter.context, TrackEditionActivity::class.java)
            intent.putExtra(EXTRA_MESSAGE, Json.encodeToString(libraryTrack))
            startActivity(this.context, intent, null)
        }

        playableLayout.setOnClickListener {
            playerViewModel.selectTrack(libraryTrack)
        }
        return rowView
    }
}