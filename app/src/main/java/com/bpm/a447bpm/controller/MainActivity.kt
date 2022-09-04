package com.bpm.a447bpm.controller

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import com.bpm.a447bpm.R
import com.bpm.a447bpm.databinding.ActivityMainBinding
import com.bpm.a447bpm.model.Song
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var songListView: ListView

    private val client = OkHttpClient()
    private lateinit var apiUrl: String
    private lateinit var songsApiUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        songListView = findViewById(R.id.song_list_view)

        apiUrl = resources.getString(R.string.bpm_api_url)
        songsApiUrl = apiUrl +"/" +resources.getString(R.string.bpm_api_songs_resource_name)
        getSongs()
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

    private fun getSongs() {
        val request = Request.Builder()
            .url(songsApiUrl)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread(java.lang.Runnable {
                    //TODO
                })
            }
            override fun onResponse(call: Call, response: Response) {
                runOnUiThread(java.lang.Runnable {
                    val songList = arrayOfNulls<String>(1)
                    val song = Json.decodeFromString<Song>("""{"name": "Africa", "author": "Toto"}""")
                    //val song = Json { ignoreUnknownKeys = true }
                    //    .decodeFromString<Song>(Song.serializer()
                    //        , """{"name": "Africa", "author": "Toto"}""")
                    println("okoko ${song.name}")
                    songList[0] = song.name + " " + song.author
                    songListView.adapter = ArrayAdapter(
                        applicationContext, android.R.layout.simple_list_item_1, songList)
                })
            }
        })
    }
}