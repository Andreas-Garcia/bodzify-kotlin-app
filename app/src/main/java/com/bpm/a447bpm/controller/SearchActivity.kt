package com.bpm.a447bpm.controller

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bpm.a447bpm.R
import com.bpm.a447bpm.dao.SongDao
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun searchSongs(query: String) {
        print("kiki")
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = ApiClient.apiService.searchSongs(query)
                if (response.isSuccessful && response.body() != null) {
                    print("kiki")

                    var songArray = arrayOfNulls<String>(response.body()!!.size)
                    for ((i, song) in response.body()!!.withIndex()) {
                        songArray[i] = song.artist + " " + song.title
                        print(i)
                    }
                    songListView.adapter = ArrayAdapter<String>(applicationContext
                        , android.R.layout.simple_list_item_1, songArray)
                } else {
                    Toast.makeText(
                        this@SearchActivity,
                        "Error Occurred: ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } catch (e: Exception) {
                Toast.makeText(
                    this@SearchActivity,
                    "Error Occurred: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}