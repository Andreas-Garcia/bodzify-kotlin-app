package com.bpm.a447bpm.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SearchView
import com.bpm.a447bpm.R
import com.bpm.a447bpm.adapter.ExternalSongListAdapter
import com.bpm.a447bpm.dto.ResponseJSON
import com.bpm.a447bpm.model.MineSong

class DigFragment : BaseFragment() {

    private lateinit var digSearchView: SearchView
    private lateinit var externalSongListView: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dig, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        digSearchView = requireView().findViewById(R.id.dig_search_view)
        externalSongListView = requireView().findViewById(R.id.external_songs_list_view)

        digSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    apiManager.digSongs(requireContext(), query) {
                            responseJSON: ResponseJSON<MutableList<MineSong>>? ->
                        val externalSongs: MutableList<MineSong> = responseJSON!!.data
                        var externalSongsToDisplay: MutableList<MineSong> =
                            externalSongs ?: arrayListOf()
                        externalSongListView.adapter =
                            ExternalSongListAdapter(
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