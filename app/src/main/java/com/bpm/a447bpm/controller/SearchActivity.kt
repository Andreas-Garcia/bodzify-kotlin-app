package com.bpm.a447bpm.controller

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bpm.a447bpm.R
import com.bpm.a447bpm.adapter.SongListAdapter
import com.bpm.a447bpm.api.SessionManager
import com.bpm.a447bpm.model.SongExternal

class SearchActivity : BaseActivity() {

    private lateinit var searchView: SearchView
    private lateinit var songListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.search)

        searchView = findViewById(R.id.song_search_view)
        songListView = findViewById(R.id.song_list_view)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    apiManager.searchSongs(
                        this@SearchActivity,
                        query
                    ) {
                        songsExternal: MutableList<SongExternal>? ->
                        var songsExternalToDisplay: MutableList<SongExternal> =
                            songsExternal ?: arrayListOf()
                        songListView.adapter =
                            SongListAdapter(
                                this@SearchActivity,
                                apiManager,
                                sessionManager,
                                songsExternalToDisplay)
                    }
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }
        })
    }
}