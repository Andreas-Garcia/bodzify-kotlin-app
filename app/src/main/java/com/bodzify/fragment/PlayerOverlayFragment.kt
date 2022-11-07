package com.bodzify.fragment

import android.os.Bundle
import android.provider.AlarmClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.bodzify.R
import com.bodzify.model.LibraryTrack
import com.bodzify.viewmodel.PlayerViewModel

class PlayerOverlayFragment : BaseFragment() {
    private val playerViewModel: PlayerViewModel by viewModels()

    private lateinit var artistTextView: TextView
    private lateinit var titleTextView: TextView
    private lateinit var genreTextView: TextView

    private lateinit var libraryTrack: LibraryTrack

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
            libraryTrack = bundle!!.getSerializable(AlarmClock.EXTRA_MESSAGE) as LibraryTrack
            titleTextView.text = libraryTrack.title
            artistTextView.text = libraryTrack.artist
            genreTextView.text = libraryTrack.genre
        }
    }
}