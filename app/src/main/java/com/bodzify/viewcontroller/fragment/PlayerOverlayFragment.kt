package com.bodzify.viewcontroller.fragment

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bodzify.R
import com.bodzify.model.LibraryTrack
import com.bodzify.datasource.network.api.RemoteDataSource

class PlayerOverlayFragment(
    private val libraryTrack: LibraryTrack,
    private val toPlay: Boolean
) : BaseFragment() {
    private lateinit var artistTextView: TextView
    private lateinit var titleTextView: TextView
    private lateinit var genreTextView: TextView
    private lateinit var playPauseImageView: ImageView

    private var mediaPlayer: MediaPlayer? = null

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
        playPauseImageView = requireView()
            .findViewById<ImageView>(R.id.player_overlay_play_pause_imageView)

        titleTextView.text = libraryTrack.title
        artistTextView.text = libraryTrack.artist
        genreTextView.text = libraryTrack.genre

        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(
                RemoteDataSource.BASE_URL_WITH_API_VERSION + libraryTrack.relativeUrl + "download/")
            prepare()
            start()
            pause()
        }
        playOrPause(toPlay)
        playPauseImageView.setOnClickListener {
            playOrPause(!mediaPlayer!!.isPlaying)
        }
    }

    private fun playOrPause(toPlay: Boolean) {
        if(!toPlay) {
            playPauseImageView.setImageDrawable(
                requireContext().getDrawable(R.drawable.ic_baseline_play_arrow_24))
            mediaPlayer!!.pause()
        }
        else {
            playPauseImageView.setImageDrawable(
                requireContext().getDrawable(R.drawable.ic_baseline_pause_24))
            mediaPlayer!!.start()
        }
    }

    override fun onDestroy() {
        mediaPlayer!!.stop()
        super.onDestroy()
    }
}