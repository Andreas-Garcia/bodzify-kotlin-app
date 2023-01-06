package com.bodzify.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.bodzify.R
import com.bodzify.model.LibraryTrack
import com.bodzify.viewmodelpattern.viewmodel.PlayerViewModel

abstract class PlayerFragment : BaseFragment() {
    private val playerViewModel: PlayerViewModel by activityViewModels()

    private lateinit var artistTextView: TextView
    private lateinit var titleTextView: TextView
    private lateinit var genreTextView: TextView
    private lateinit var playPauseImageView: ImageView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        titleTextView = this.getTitleTextView()
        artistTextView = this.getArtistTextView()
        genreTextView = this.getGenreTextView()
        playPauseImageView = this.getPlayPauseImageView()
        playPauseImageView.setOnClickListener {
            playOrPause()
        }

        this.setLayoutWithNewPlayingTrack(playerViewModel.playingTrack.value)
        playerViewModel.playingTrack.observe(viewLifecycleOwner) {
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

    private fun playOrPause() {
        playerViewModel.playPause()
        if(!playerViewModel.isPlaying.value!!) {
            playPauseImageView.setImageDrawable(
                requireContext().getDrawable(R.drawable.ic_baseline_play_arrow_24))
        }
        else {
            playPauseImageView.setImageDrawable(
                requireContext().getDrawable(R.drawable.ic_baseline_pause_24))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}