package com.bpm.a447bpm.controller

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bpm.a447bpm.R
import com.bpm.a447bpm.adapter.SongListAdapter
import com.bpm.a447bpm.api.ApiClient
import com.bpm.a447bpm.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var searchView: SearchView
    private lateinit var songListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.search)

        searchView = findViewById(R.id.song_search_view)
        songListView = findViewById(R.id.song_list_view)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    searchSongs(query)
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }
        })
    }

    private fun searchSongs(query: String) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = ApiClient(getString(R.string.bpm_api_url))
                    .apiService.searchSongs(query)
                    if (response.isSuccessful && response.body() != null) {
                        songListView.adapter =
                            SongListAdapter(this@SearchActivity, response.body()!!)
                        if (response.body()!!.size == 0) {
                            handleSearchIssue(R.string.no_results)
                        }
                    } else {
                        handleSearchIssue(R.string.error_occurred_label)
                    }
            } catch (e: Exception) {
                handleSearchIssue(R.string.error_occurred_label)
            }
        }
    }

    private fun handleSearchIssue (issueMessageResourceInt: Int) {
        Toast.makeText(this@SearchActivity, issueMessageResourceInt, Toast.LENGTH_LONG)
            .show()
    }
}