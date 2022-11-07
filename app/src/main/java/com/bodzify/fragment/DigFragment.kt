package com.bodzify.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SearchView
import com.bodzify.R
import com.bodzify.adapter.MineSongListAdapter
import com.bodzify.dto.PaginatedResponseDTO
import com.bodzify.model.MineSong

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
                            responseJSON: PaginatedResponseDTO<MutableList<MineSong>>? ->
                        val externalSongs: MutableList<MineSong> = responseJSON!!.results
                        var externalSongsToDisplay: MutableList<MineSong> =
                            externalSongs ?: arrayListOf()
                        externalSongListView.adapter =
                            MineSongListAdapter(
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