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
import com.bodzify.dto.LibraryTrackUpdateDto
import com.bodzify.model.LibraryTrack
import com.bodzify.session.SessionManager
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class TrackEditionActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var apiManager: ApiManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track_edition)

        sessionManager = SessionManager(this)
        apiManager = ApiManager(sessionManager, ApiClient(this))

        val albumArtworkImageView = findViewById<ImageView>(
            R.id.song_edition_album_artwork_imageView)
        val filenameTextView = findViewById<TextView>(R.id.track_edition_filename_textView)
        val urlTextView = findViewById<TextView>(R.id.track_edition_url_textView)
        val titleEditText = findViewById<EditText>(R.id.track_edition_title_editText)
        val artistEditText = findViewById<EditText>(R.id.track_edition_artist_editText)
        val albumEditText = findViewById<EditText>(R.id.track_edition_album_editText)
        val genreEditText = findViewById<EditText>(R.id.track_edition_genre_editText)
        val ratingEditText = findViewById<EditText>(R.id.track_edition_rating_editText)
        val languageEditText = findViewById<EditText>(R.id.track_edition_language_editText)
        val durationTextView = findViewById<TextView>(R.id.track_edition_duration_textView)
        val releasedOnEditText = findViewById<EditText>(R.id.track_edition_released_on_editText)
        val addedOnTextView = findViewById<TextView>(R.id.track_edition_added_on_textView)
        val cancelButton = findViewById<TextView>(R.id.track_edition_cancel_button)
        val saveButton = findViewById<TextView>(R.id.track_edition_save_button)

        val libraryTrack = Json.decodeFromString<LibraryTrack>(intent.getStringExtra(EXTRA_MESSAGE)!!)
        filenameTextView.text = libraryTrack.filename
        urlTextView.text = ApiClient.baseUrlWithVersion + libraryTrack.relativeUrl
        titleEditText.setText(libraryTrack.title)
        artistEditText.setText(libraryTrack.artist)
        albumEditText.setText(libraryTrack.album)
        genreEditText.setText(libraryTrack.genre)
        ratingEditText.setText("" + libraryTrack.rating)
        languageEditText.setText(libraryTrack.language)
        durationTextView.text = libraryTrack.duration
        releasedOnEditText.setText(libraryTrack.releasedOn)
        addedOnTextView.text = libraryTrack.addedOn

        saveButton.setOnClickListener {
            apiManager.updateLibraryTrack(this,
                libraryTrack.uuid,
                LibraryTrackUpdateDto(
                    titleEditText.text.toString(),
                    artistEditText.text.toString(),
                    albumEditText.text.toString(),
                    genreEditText.text.toString(),
                    Integer.parseInt(ratingEditText.text.toString()),
                    languageEditText.text.toString()
                )
            ) {
                    librarySong: LibraryTrack ->
                val librarySong: LibraryTrack = librarySong
            }
        }
    }
}