package com.bodzify.ui.fragment

import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bodzify.R
import com.bodzify.datasource.network.api.RemoteDataSource
import com.bodzify.model.LibraryTrack
import com.bodzify.ui.activity.FullScreenPlayerActivity
import com.bodzify.viewmodel.PlayingTrackViewModel

abstract class PlayerFragment (
    private val playingTrackViewModel: PlayingTrackViewModel,
    private val track: LibraryTrack,
    private val toPlay: Boolean
) : BaseFragment() {
    private lateinit var artistTextView: TextView
    private lateinit var titleTextView: TextView
    private lateinit var genreTextView: TextView
    private lateinit var playPauseImageView: ImageView

    private var mediaPlayer: MediaPlayer? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        titleTextView = this.getTitleTextView()
        artistTextView = this.getArtistTextView()
        genreTextView = this.getGenreTextView()
        playPauseImageView = this.getPlayPauseImageView()

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

        this.setLayoutWithNewPlayingTrack(playingTrackViewModel.playingTrack.value)
        playingTrackViewModel.playingTrack.observe(viewLifecycleOwner) {
            playingTrack: LibraryTrack? ->
            this.setLayoutWithNewPlayingTrack(playingTrack)
        }
    }

    private fun setLayoutWithNewPlayingTrack(playingTrack: LibraryTrack?) {
        if (playingTrack == null) {
            titleTextView.text = null
            artistTextView.text = null
            genreTextView.text = null
        }
        else {
            titleTextView.text = playingTrack.title
            artistTextView.text = playingTrack.artist
            if(playingTrack.genre != null) {
                genreTextView.text = playingTrack.genre!!.name
            }
        }
    }

    abstract fun getPlayPauseImageView(): ImageView

    abstract fun getGenreTextView(): TextView

    abstract fun getArtistTextView(): TextView

    abstract fun getTitleTextView(): TextView

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