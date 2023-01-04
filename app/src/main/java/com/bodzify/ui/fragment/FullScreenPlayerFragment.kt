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
import com.bodzify.viewmodel.PlayingTrackViewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class FullScreenPlayerFragment (
    playingTrackViewModel: PlayingTrackViewModel,
    track: LibraryTrack,
    toPlay: Boolean
) : PlayerFragment(playingTrackViewModel, track, toPlay) {

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