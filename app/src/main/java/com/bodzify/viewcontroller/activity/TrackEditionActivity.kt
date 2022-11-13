package com.bodzify.viewcontroller.activity

import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bodzify.R
import com.bodzify.application.AppApplication
import com.bodzify.datasource.network.api.RemoteDataSource
import com.bodzify.model.Genre
import com.bodzify.model.LibraryTrack
import com.bodzify.session.SessionManager
import com.bodzify.viewcontroller.adapter.GenreListAdapter
import com.bodzify.viewmodel.GenreViewModel
import com.bodzify.viewmodel.GenreViewModelFactory
import com.bodzify.viewmodel.LibraryTrackViewModel
import com.bodzify.viewmodel.LibraryTrackViewModelFactory
import com.bodzify.viewmodel.util.observeOnce
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class TrackEditionActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    private val libraryTrackViewModel: LibraryTrackViewModel by viewModels {
        LibraryTrackViewModelFactory((application as AppApplication).libraryTrackRepository)
    }

    private val genreViewModel: GenreViewModel by viewModels {
        GenreViewModelFactory((application as AppApplication).genreRepository)
    }

    private lateinit var genreSelectButton: Button
    private lateinit var track: LibraryTrack

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track_edition)

        sessionManager = SessionManager(this)

        val albumArtworkImageView = findViewById<ImageView>(
            R.id.song_edition_album_artwork_imageView)
        val filenameTextView = findViewById<TextView>(R.id.track_edition_filename_textView)
        val urlTextView = findViewById<TextView>(R.id.track_edition_url_textView)
        val titleEditText = findViewById<EditText>(R.id.track_edition_title_editText)
        val artistEditText = findViewById<EditText>(R.id.track_edition_artist_editText)
        val albumEditText = findViewById<EditText>(R.id.track_edition_album_editText)
        genreSelectButton = findViewById<Button>(R.id.track_edition_genre_select_button)
        val ratingEditText = findViewById<EditText>(R.id.track_edition_rating_editText)
        val languageEditText = findViewById<EditText>(R.id.track_edition_language_editText)
        val durationTextView = findViewById<TextView>(R.id.track_edition_duration_textView)
        val releasedOnEditText = findViewById<EditText>(R.id.track_edition_released_on_editText)
        val addedOnTextView = findViewById<TextView>(R.id.track_edition_added_on_textView)
        val cancelButton = findViewById<TextView>(R.id.track_edition_cancel_button)
        val saveButton = findViewById<TextView>(R.id.track_edition_save_button)

        val genreLayout = findViewById<LinearLayout>(R.id.track_edition_genre_selection_layout)
        val genreSelectionSearchView =
            findViewById<SearchView>(R.id.track_edition_genre_selection_searchview)
        val genreSelectionListView =
            findViewById<ListView>(R.id.track_edition_genre_selection_listview)
        val genreCancelButton =
            findViewById<Button>(R.id.track_edition_genre_selection_cancel_button)
        val genreValidateButton =
            findViewById<Button>(R.id.track_edition_genre_selection_validate_button)

        track = Json.decodeFromString<LibraryTrack>(intent.getStringExtra(EXTRA_MESSAGE)!!)
        filenameTextView.text = track.filename
        urlTextView.text = RemoteDataSource.BASE_URL_WITH_API_VERSION + track.relativeUrl
        titleEditText.setText(track.title)
        artistEditText.setText(track.artist)
        albumEditText.setText(track.album)
        setGenreSelectionButtonText(track.genre)
        ratingEditText.setText("" + track.rating)
        languageEditText.setText(track.language)
        durationTextView.text = track.duration
        releasedOnEditText.setText(track.releasedOn)
        addedOnTextView.text = track.addedOn

        genreSelectButton.setOnClickListener {
            genreViewModel.search(null)
            genreLayout.visibility = View.VISIBLE
        }

        genreCancelButton.setOnClickListener {
            genreLayout.visibility = View.GONE
        }

        saveButton.setOnClickListener {
            var genreUuid: String?
            genreUuid =
                if(track.genre != null) track.genre!!.uuid
                else null
            libraryTrackViewModel.update(
                track.uuid,
                titleEditText.text.toString(),
                artistEditText.text.toString(),
                albumEditText.text.toString(),
                genreUuid,
                Integer.parseInt(ratingEditText.text.toString()),
                languageEditText.text.toString()
            )

            libraryTrackViewModel.libraryTrackUpdated.observeOnce(this){
                    libraryTrack: LibraryTrack ->
                finish()
            }
        }

        genreViewModel.genresSearched.observe(this) {
                genres ->
            genreSelectionListView.adapter = GenreListAdapter(
                this,
                sessionManager,
                genres?: arrayListOf(),
                genreViewModel
            )
        }

        genreViewModel.genreSelected.observe(this) {
            genre ->
            track.genre = genre
            setGenreSelectionButtonText(genre)
            genreLayout.visibility = View.GONE
        }

        genreSelectionSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(nameFilter: String?): Boolean {
                genreViewModel.search(nameFilter)
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }
        })
    }

    private fun setGenreSelectionButtonText(genre: Genre?) {
        if(track.genre == null) genreSelectButton.text = getString(R.string.Select)
        else genreSelectButton.text = track.genre!!.name
    }
}