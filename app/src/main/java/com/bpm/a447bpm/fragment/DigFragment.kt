package com.bpm.a447bpm.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SearchView
import com.bpm.a447bpm.R
import com.bpm.a447bpm.adapter.SongListAdapter
import com.bpm.a447bpm.model.SongExternal

class DigFragment : BaseFragment() {

    private lateinit var searchView: SearchView
    private lateinit var songListView: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dig, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchView = requireView().findViewById(R.id.dig_search_view)
        songListView = requireView().findViewById(R.id.dug_songs_list_view)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    apiManager.digSongs(requireContext(), query) {
                            externalSongs: MutableList<SongExternal>? ->
                        var externalSongsToDisplay: MutableList<SongExternal> =
                            externalSongs ?: arrayListOf()
                        songListView.adapter =
                            SongListAdapter(
                                requireActivity(),
                                apiManager,
                                sessionManager,
                                externalSongsToDisplay)
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