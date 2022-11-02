package com.bodzify.fragment

import android.os.Bundle
import android.provider.AlarmClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.bodzify.R
import com.bodzify.model.LibrarySong
import com.bodzify.viewmodel.PlayerViewModel

class PlayerOverlayFragment : BaseFragment() {
    private val playerViewModel: PlayerViewModel by viewModels()

    private lateinit var artistTextView: TextView
    private lateinit var titleTextView: TextView
    private lateinit var genreTextView: TextView

    private lateinit var librarySong: LibrarySong

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_overlay_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        titleTextView = requireView().findViewById<TextView>(R.id.player_overlay_title_textview)
        artistTextView = requireView().findViewById<TextView>(R.id.player_overlay_artist_textview)
        genreTextView = requireView().findViewById<TextView>(R.id.player_overlay_genre_textview)

        val bundle = this.arguments
        if( bundle != null) {
            librarySong = bundle!!.getSerializable(AlarmClock.EXTRA_MESSAGE) as LibrarySong
            titleTextView.text = librarySong.title
            artistTextView.text = librarySong.artist
            genreTextView.text = librarySong.genre
        }
    }
}