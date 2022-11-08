package com.bodzify.viewcontroller.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SearchView
import androidx.fragment.app.activityViewModels
import com.bodzify.R
import com.bodzify.viewcontroller.adapter.LibraryTrackListAdapter
import com.bodzify.repository.network.dto.PaginatedResponseDto
import com.bodzify.model.LibraryTrack
import com.bodzify.viewmodel.PlayerViewModel

class LibraryFragment : BaseFragment() {
    private val playerViewModel: PlayerViewModel by activityViewModels()

    private lateinit var libraryTracksSearchView: SearchView
    private lateinit var libraryTracksListView: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_library, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        libraryTracksSearchView = requireView().findViewById(R.id.library_tracks_search_view)
        libraryTracksListView = requireView().findViewById(R.id.library_tracks_list_view)

        libraryTracksSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchLibraryTracks()
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }
        })
        searchLibraryTracks()
    }

    fun searchLibraryTracks() {
        apiManager.searchLibraryTracks(requireContext()) {
                responseJSON: PaginatedResponseDto<MutableList<LibraryTrack>>? ->
            libraryTracksListView.adapter = LibraryTrackListAdapter(
                requireActivity(),
                apiManager,
                sessionManager,
                responseJSON!!.results ?: arrayListOf(),
                playerViewModel)
        }
    }
}