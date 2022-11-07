package com.bodzify.fragment

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.AlarmClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.bodzify.R
import com.bodzify.api.ApiClient
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

            val mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(ApiClient.baseUrlWithVersion + libraryTrack.relativeUrl + "download/")
                prepare() // might take long! (for buffering, etc)
                start()
            }
        }
    }
}