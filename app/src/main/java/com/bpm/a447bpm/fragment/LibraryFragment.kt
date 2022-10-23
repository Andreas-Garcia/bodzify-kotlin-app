package com.bpm.a447bpm.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SearchView
import com.bpm.a447bpm.R
import com.bpm.a447bpm.adapter.LibrarySongListAdapter
import com.bpm.a447bpm.dto.ResponseJSON
import com.bpm.a447bpm.model.LibrarySong


class LibraryFragment : BaseFragment() {

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
                responseJSON: ResponseJSON<MutableList<LibrarySong>>? ->
            val librarySongs: MutableList<LibrarySong> = responseJSON!!.data
            var librarySongsToDisplay: MutableList<LibrarySong> =
                librarySongs ?: arrayListOf()
            librarySongListView.adapter = LibrarySongListAdapter(
                requireActivity(),
                apiManager,
                sessionManager,
                librarySongsToDisplay)
        }
    }
}