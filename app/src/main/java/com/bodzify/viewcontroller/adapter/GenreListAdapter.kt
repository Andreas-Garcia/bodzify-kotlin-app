package com.bodzify.viewcontroller.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.bodzify.R
import com.bodzify.model.Genre
import com.bodzify.session.SessionManager
import com.bodzify.viewmodel.GenreViewModel

class GenreListAdapter(private val activity: Activity,
                       private val sessionManager: SessionManager,
                       private val genre: MutableList<Genre>,
                       private val genreViewModel: GenreViewModel
) : ArrayAdapter<Genre>(activity, R.layout.list_view_item_genre, genre) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val rowView = activity.layoutInflater
            .inflate(R.layout.list_view_item_genre, null, true)

        val genreTextView = rowView.findViewById<TextView>(R.id.genre_name_textview)

        val genre = genre[position]
        genreTextView.text = genre.name

        rowView.setOnClickListener {
            /*mineTrackViewModel.extract(mineTrack)
            mineTrackViewModel.mineTrackExtracted.observeOnce(activity as HomeActivity) {
                // TODO
            }*/
        }

        return rowView
    }
}