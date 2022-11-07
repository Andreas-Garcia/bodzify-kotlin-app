package com.bodzify.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SearchView
import com.bodzify.R
import com.bodzify.adapter.MineTrackListAdapter
import com.bodzify.dto.PaginatedResponseDto
import com.bodzify.model.MineTrack

class DigFragment : BaseFragment() {

    private lateinit var digSearchView: SearchView
    private lateinit var mineTrackListView: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dig, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        digSearchView = requireView().findViewById(R.id.dig_search_view)
        mineTrackListView = requireView().findViewById(R.id.mine_tracks_list_view)

        digSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    apiManager.digTracks(requireContext(), query) {
                            responseJSON: PaginatedResponseDto<MutableList<MineTrack>>? ->
                        mineTrackListView.adapter =
                            MineTrackListAdapter(
                                requireActivity(),
                                apiManager,
                                sessionManager,
                                responseJSON!!.results ?: arrayListOf())
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