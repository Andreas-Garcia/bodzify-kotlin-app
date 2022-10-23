package com.bpm.a447bpm.activity

import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bpm.a447bpm.R
import com.bpm.a447bpm.model.LibrarySong
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class SongEditionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_edition)

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

        val librarySong = Json.decodeFromString<LibrarySong>(intent.getStringExtra(EXTRA_MESSAGE)!!)
        filenameTextView.text = librarySong!!.filename
        titleEditText.setText(librarySong!!.title)
        artistEditText.setText(librarySong!!.artist)
        albumEditText.setText(librarySong!!.album)
        genreEditText.setText(librarySong!!.genre)
        ratingEditText.setText(librarySong!!.rating)
        languageEditText.setText(librarySong!!.language)
        durationTextView.text = librarySong!!.duration
        releasedOnEditText.setText(librarySong!!.releasedOn)
        addedOnTextView.text = librarySong!!.addedOn
    }
}