package com.bodzify.ui.fragment

import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.AlarmClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bodzify.R
import com.bodzify.datasource.network.api.RemoteDataSource
import com.bodzify.model.LibraryTrack
import com.bodzify.ui.activity.FullScreenPlayerActivity
import com.bodzify.ui.activity.TrackEditionActivity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class OverlayPlayerFragment (
    private val track: LibraryTrack,
    private val toPlay: Boolean
) : BaseFragment() {
    private lateinit var overlayPlayerLayout: LinearLayout
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

        overlayPlayerLayout = requireView().findViewById<LinearLayout>(R.id.overlay_player_fragment_layout)
        titleTextView = requireView().findViewById(R.id.player_overlay_title_textview)
        artistTextView = requireView().findViewById(R.id.player_overlay_artist_textview)
        genreTextView = requireView().findViewById(R.id.player_overlay_genre_textview)
        playPauseImageView = requireView().findViewById(R.id.player_overlay_play_pause_imageView)

        titleTextView.text = track.title
        artistTextView.text = track.artist
        if(track.genre != null) genreTextView.text = track.genre!!.name

        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(
                RemoteDataSource.BASE_URL_WITH_API_VERSION + track.relativeUrl + "download/")
            prepare()
            start()
            pause()
        }
        playOrPause(toPlay)
        playPauseImageView.setOnClickListener {
            playOrPause(!mediaPlayer!!.isPlaying)
        }

        overlayPlayerLayout.setOnClickListener {
            val intent = Intent(this@OverlayPlayerFragment.context, FullScreenPlayerActivity::class.java)
            ContextCompat.startActivity(this.requireContext(), intent, null)
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