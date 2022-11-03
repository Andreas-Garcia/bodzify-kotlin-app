package com.bodzify.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bodzify.R
import com.bodzify.adapter.LibrarySongListAdapter
import com.bodzify.dto.PaginatedResponseDTO
import com.bodzify.model.LibrarySong
import com.bodzify.viewmodel.PlayerViewModel

class LibraryFragment : BaseFragment() {
    private val playerViewModel: PlayerViewModel by activityViewModels()

    private lateinit var librarySearchView: SearchView
    private lateinit var librarySongListView: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_library, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        librarySearchView = requireView().findViewById(R.id.library_search_view)
        librarySongListView = requireView().findViewById(R.id.library_songs_list_view)

        librarySearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchLibrarySongs()
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }
        })

        searchLibrarySongs()
    }

    fun searchLibrarySongs() {
        apiManager.searchLibrarySongs(requireContext()) {
                responseJSON: PaginatedResponseDTO<MutableList<LibrarySong>>? ->
            val librarySongs: MutableList<LibrarySong> = responseJSON!!.data
            var librarySongsToDisplay: MutableList<LibrarySong> = librarySongs ?: arrayListOf()
            librarySongListView.adapter = LibrarySongListAdapter(
                requireActivity(),
                apiManager,
                sessionManager,
                librarySongsToDisplay,
                playerViewModel)
        }
    }
}