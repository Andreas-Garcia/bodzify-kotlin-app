package com.bodzify.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bodzify.R

class FullScreenPlayerFragment : PlayerFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_full_screen_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getPlayPauseImageView(): ImageView {
        return requireView().findViewById(R.id.fullscreen_player_artist_textView)
    }

    override fun getGenreTextView(): TextView {
        TODO("Not yet implemented")
    }

    override fun getArtistTextView(): TextView {
        TODO("Not yet implemented")
    }

    override fun getTitleTextView(): TextView {
        TODO("Not yet implemented")
    }
}