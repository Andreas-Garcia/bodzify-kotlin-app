package com.bpm.a447bpm.dao

import android.content.Context
import com.bpm.a447bpm.R
import com.bpm.a447bpm.model.Song
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response

class SongDao {
    companion object {
        /*fun fetchAll(context: Context, content: MutableList<Song>): Array<Song?> {
            val songsJsonArray: JSONArray = JSONObject(response.body().)
                .getJSONArray(context.getString(R.string.bpm_api_data_name))
            val songs = arrayOfNulls<Song>(songsJsonArray.length())
            for(i in 0 until songsJsonArray.length()) {
                songs[i] = Json.decodeFromString<Song>(songsJsonArray[i].toString())
            }
            return songs
        }*/
    }
}