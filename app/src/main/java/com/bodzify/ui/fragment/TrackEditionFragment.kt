package com.bodzify.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.activityViewModels
import com.bodzify.R
import com.bodzify.datasource.network.api.RemoteDataSource
import com.bodzify.model.Genre
import com.bodzify.model.LibraryTrack
import com.bodzify.ui.adapter.GenreListAdapter
import com.bodzify.viewmodel.EditingTrackViewModel
import com.bodzify.viewmodel.GenreViewModel
import com.bodzify.viewmodel.LibraryTrackViewModel
import com.bodzify.viewmodel.util.observeOnce

class TrackEditionFragment : BaseFragment() {

    private val libraryTrackViewModel: LibraryTrackViewModel by activityViewModels {
        LibraryTrackViewModel.Factory
    }
    private val genreViewModel: GenreViewModel by activityViewModels {
        GenreViewModel.Factory
    }
    private val editingTrackViewModel: EditingTrackViewModel by activityViewModels ()

    private lateinit var genreSelectButton: Button
    private var track: LibraryTrack? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_track_edition, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val albumArtworkImageView = requireView().findViewById<ImageView>(
            R.id.song_edition_album_artwork_imageView)
        val filenameTextView = requireView()
            .findViewById<TextView>(R.id.track_edition_filename_textView)
        val urlTextView = requireView().findViewById<TextView>(R.id.track_edition_url_textView)
        val titleEditText = requireView()
            .findViewById<EditText>(R.id.track_edition_title_editText)
        val artistEditText = requireView()
            .findViewById<EditText>(R.id.track_edition_artist_editText)
        val albumEditText = requireView().findViewById<EditText>(R.id.track_edition_album_editText)
        val genreSelectButton = requireView()
            .findViewById<Button>(R.id.track_edition_genre_select_button)
        val ratingEditText = requireView().findViewById<EditText>(R.id.track_edition_rating_editText)
        val languageEditText = requireView()
            .findViewById<EditText>(R.id.track_edition_language_editText)
        val durationTextView = requireView()
            .findViewById<TextView>(R.id.track_edition_duration_textView)
        val releasedOnEditText = requireView()
            .findViewById<EditText>(R.id.track_edition_released_on_editText)
        val addedOnTextView = requireView()
            .findViewById<TextView>(R.id.track_edition_added_on_textView)
        val cancelButton = requireView().findViewById<TextView>(R.id.track_edition_cancel_button)
        val saveButton = requireView().findViewById<TextView>(R.id.track_edition_save_button)

        val genreLayout = requireView()
            .findViewById<LinearLayout>(R.id.track_edition_genre_selection_layout)
        val genreSelectionSearchView =
            requireView().findViewById<SearchView>(R.id.track_edition_genre_selection_searchview)
        val genreSelectionListView =
            requireView().findViewById<ListView>(R.id.track_edition_genre_selection_listview)
        val genreCancelButton =
            requireView().findViewById<Button>(R.id.track_edition_genre_selection_cancel_button)
        val genreValidateButton =
            requireView().findViewById<Button>(R.id.track_edition_genre_selection_validate_button)

        track = editingTrackViewModel.editingTrack.value

        filenameTextView.text = track?.filename ?: ""
        urlTextView.text = RemoteDataSource.BASE_URL_WITH_API_VERSION + track?.relativeUrl ?: ""
        titleEditText.setText(track?.title ?: "")
        artistEditText.setText(track?.artist ?: "")
        albumEditText.setText(track?.album ?: "")
        setGenreSelectionButtonText(track?.genre)
        ratingEditText.setText("" + track?.rating ?: "")
        languageEditText.setText(track?.language ?: "")
        durationTextView.text = track?.duration ?: ""
        releasedOnEditText.setText(track?.releasedOn ?: "")
        addedOnTextView.text = track?.addedOn ?: ""

        genreSelectButton.setOnClickListener {
            genreViewModel.search(null, null)
            genreLayout.visibility = View.VISIBLE
        }

        genreCancelButton.setOnClickListener {
            genreLayout.visibility = View.GONE
        }

        saveButton.setOnClickListener {
            if(track != null) {
                libraryTrackViewModel.update(
                    track!!.uuid,
                    titleEditText.text.toString(),
                    artistEditText.text.toString(),
                    albumEditText.text.toString(),
                    track!!.genre.uuid,
                    Integer.parseInt(ratingEditText.text.toString()),
                    languageEditText.text.toString()
                )
            }

            libraryTrackViewModel.libraryTrackUpdated.observeOnce(this){
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
                genreViewModel.search(nameFilter, null)
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