package com.bodzify.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bodzify.R
import com.bodzify.model.LibraryTrack
import com.bodzify.ui.activity.FullScreenPlayerActivity
import com.bodzify.viewmodel.PlayingTrackViewModel

class OverlayPlayerFragment(
    playingTrackViewModel: PlayingTrackViewModel, track: LibraryTrack, toPlay: Boolean)
    : PlayerFragment (playingTrackViewModel, track, toPlay) {
    override fun getPlayPauseImageView(): ImageView {
        return requireView().findViewById(R.id.player_overlay_play_pause_imageView)
    }

    override fun getGenreTextView(): TextView {
        return requireView().findViewById(R.id.overlay_player_genre_textview)
    }

    override fun getArtistTextView(): TextView {
        return requireView().findViewById(R.id.overlay_player_artist_textview)
    }

    override fun getTitleTextView(): TextView {
        return requireView().findViewById(R.id.overlay_player_title_textview)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_overlay_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val overlayPlayerLayout = requireView().findViewById<LinearLayout>(R.id.overlay_player_fragment_layout)

        overlayPlayerLayout.setOnClickListener {
            val intent = Intent(this@OverlayPlayerFragment.context, FullScreenPlayerActivity::class.java)
            ContextCompat.startActivity(this.requireContext(), intent, null)
        }
    }
}