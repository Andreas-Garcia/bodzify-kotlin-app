package com.bodzify.activity

import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bodzify.R
import com.bodzify.api.ApiClient
import com.bodzify.api.ApiManager
import com.bodzify.dto.LibrarySongUpdateDTO
import com.bodzify.dto.ResponseJSON
import com.bodzify.model.LibrarySong
import com.bodzify.session.SessionManager
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class SongEditionActivity : AppCompatActivity() {

    protected lateinit var sessionManager: SessionManager
    protected lateinit var apiManager: ApiManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_edition)

        sessionManager = SessionManager(this)
        apiManager = ApiManager(sessionManager, ApiClient(this))

        val albumArtworkImageView = findViewById<ImageView>(
            R.id.song_edition_album_artwork_imageView)
        val filenameTextView = findViewById<TextView>(R.id.song_edition_filename_textView)
        val titleEditText = findViewById<EditText>(R.id.song_edition_title_editText)
        val artistEditText = findViewById<EditText>(R.id.song_edition_artist_editText)
        val albumEditText = findViewById<EditText>(R.id.song_edition_album_editText)
        val genreEditText = findViewById<EditText>(R.id.song_edition_genre_editText)
        val ratingEditText = findViewById<EditText>(R.id.song_edition_rating_editText)
        val languageEditText = findViewById<EditText>(R.id.song_edition_language_editText)
        val durationTextView = findViewById<TextView>(R.id.song_edition_duration_textView)
        val releasedOnEditText = findViewById<EditText>(R.id.song_edition_released_on_editText)
        val addedOnTextView = findViewById<TextView>(R.id.song_edition_added_on_textView)
        val cancelButton = findViewById<TextView>(R.id.song_edition_cancel_button)
        val saveButton = findViewById<TextView>(R.id.song_edition_save_button)

        val librarySong = Json.decodeFromString<LibrarySong>(intent.getStringExtra(EXTRA_MESSAGE)!!)
        filenameTextView.text = librarySong.filename
        titleEditText.setText(librarySong.title)
        artistEditText.setText(librarySong.artist)
        albumEditText.setText(librarySong.album)
        genreEditText.setText(librarySong.genre)
        ratingEditText.setText("" + librarySong.rating)
        languageEditText.setText(librarySong.language)
        durationTextView.text = librarySong.duration
        releasedOnEditText.setText(librarySong.releasedOn)
        addedOnTextView.text = librarySong.addedOn

        saveButton.setOnClickListener {
            apiManager.updateLibrarySong(this,
                librarySong.uuid,
                LibrarySongUpdateDTO(
                    titleEditText.text.toString(),
                    artistEditText.text.toString(),
                    albumEditText.text.toString(),
                    genreEditText.text.toString(),
                    Integer.parseInt(ratingEditText.text.toString()),
                    languageEditText.text.toString()
                )
            ) {
                    responseJSON: ResponseJSON<LibrarySong>? ->
                val librarySong: LibrarySong = responseJSON!!.data
            }
        }
    }
}