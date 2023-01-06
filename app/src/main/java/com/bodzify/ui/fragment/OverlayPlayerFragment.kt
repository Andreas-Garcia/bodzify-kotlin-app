package com.bodzify.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bodzify.R

class OverlayPlayerFragment : PlayerFragment() {
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

        }
    }
}