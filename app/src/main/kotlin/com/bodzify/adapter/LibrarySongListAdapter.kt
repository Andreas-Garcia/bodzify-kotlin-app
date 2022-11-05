package com.bodzify.adapter

import android.app.Activity
import android.content.Intent
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import com.bodzify.R
import com.bodzify.activity.SongEditionActivity
import com.bodzify.api.ApiManager
import com.bodzify.session.SessionManager
import com.bodzify.model.LibrarySong
import com.bodzify.viewmodel.PlayerViewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class LibrarySongListAdapter(
    private val activity: Activity,
    private val apiManager: ApiManager,
    private val sessionManager: SessionManager,
    private val librarySongs: MutableList<LibrarySong>,
    private val playerViewModel: PlayerViewModel
)
    : ArrayAdapter<LibrarySong>(activity, R.layout.list_view_item_library_song, librarySongs) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val rowView = activity.layoutInflater
            .inflate(R.layout.list_view_item_library_song, null, true)

        val artistTextView = rowView.findViewById<TextView>(R.id.library_song_artist_textview)
        val titleTextView = rowView.findViewById<TextView>(R.id.library_song_title_textview)
        val genreTextView = rowView.findViewById<TextView>(R.id.library_song_genre_textView)
        val ratingImageView = rowView.findViewById<ImageView>(R.id.player_overlay_play_pause_imageView)
        val syncButton = rowView.findViewById<Button>(R.id.library_song_sync_button)
        val editImageView = rowView.findViewById<ImageView>(R.id.library_song_edit_imageView)
        val playableLayout = rowView.findViewById<LinearLayout>(R.id.library_song_playable_layout)

        val librarySong: LibrarySong = librarySongs[position]
        artistTextView.text = librarySong.artist
        titleTextView.text = librarySong.title
        genreTextView.text = librarySong.genre

        syncButton.setOnClickListener {
            //TODO
        }

        editImageView.setOnClickListener {
            val intent = Intent(
                this@LibrarySongListAdapter.context, SongEditionActivity::class.java)
            intent.putExtra(EXTRA_MESSAGE, Json.encodeToString(librarySong))
            startActivity(this.context, intent, null)
        }

        playableLayout.setOnClickListener {
            playerViewModel.selectSong(librarySong)
            /*val mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(context.getString(R.string.api_base_url) + librarySong.relativeUrl
                        + "download/")
                prepare() // might take long! (for buffering, etc)
                start()
            }*/
        }

        return rowView
    }
}