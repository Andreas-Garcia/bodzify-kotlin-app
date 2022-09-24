package com.bpm.a447bpm.controller

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bpm.a447bpm.R
import com.bpm.a447bpm.adapter.SongListAdapter
import com.bpm.a447bpm.api.ApiClient
import com.bpm.a447bpm.api.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.internal.format

class SearchActivity : AppCompatActivity() {

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
        val accessToken = SessionManager(this@SearchActivity).user?.jwtToken?.access
        if(accessToken != null) {
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    val response = ApiClient(this@SearchActivity)
                        .apiService.searchSongs(
                            format(getString(R.string.bpm_api_auth_bearer_format), accessToken),
                            getString(R.string.bpm_api_songs_external_source_myfreemp_value),
                            query
                        )
                    if (response.isSuccessful && response.body() != null) {
                        songListView.adapter =
                            SongListAdapter(this@SearchActivity, response.body()!!)
                        if (response.body()!!.size == 0) {
                            handleSearchIssue(getString(R.string.no_results))
                        }
                    } else {
                        handleSearchIssue(getString(R.string.error_occurred_label)
                                + " " + response.toString())
                    }
                } catch (e: Exception) {
                    handleSearchIssue(getString(R.string.error_occurred_label)
                            + " " + e.message)
                }
            }
        }
    }

    private fun handleSearchIssue (message: String) {
        Toast.makeText(this@SearchActivity, message, Toast.LENGTH_LONG)
            .show()
    }
}