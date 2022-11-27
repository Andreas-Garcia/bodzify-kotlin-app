package com.bodzify.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.ListView
import android.widget.SearchView
import androidx.fragment.app.activityViewModels
import com.bodzify.R
import com.bodzify.ui.adapter.LibraryTrackListAdapter
import com.bodzify.ui.adapter.PlaylistAdapter
import com.bodzify.viewmodel.LibraryTrackViewModel
import com.bodzify.viewmodel.PlayerViewModel
import com.bodzify.viewmodel.PlaylistViewModel
import com.bodzify.viewmodel.util.observeOnce

class LibraryFragment(private val libraryTrackViewModel: LibraryTrackViewModel,
                      private val playlistViewModel: PlaylistViewModel) : BaseFragment() {
    private val playerViewModel: PlayerViewModel by activityViewModels()

    private lateinit var libraryTracksSearchView: SearchView
    private lateinit var libraryTracksListView: ListView
    private lateinit var playlistsGridView: GridView

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
        playlistsGridView = requireView().findViewById(R.id.playlist_grid_view)

        libraryTrackViewModel.libraryTracksSearched.observe(viewLifecycleOwner) {
                libraryTracks ->
            libraryTracksListView.adapter = LibraryTrackListAdapter(
                requireActivity(),
                libraryTracks ?: arrayListOf(),
                playerViewModel)
        }

        // TODO
        // Quickfix Observe should be used instead of ObserveOnce.
        // An unknown issue makes the Observer called twice instead of once for each call.
        playlistViewModel.playlistsSearched.observeOnce(viewLifecycleOwner) {
                playlists ->
            if(playlists != null) {
                playlistsGridView.adapter = PlaylistAdapter(
                    requireActivity(),
                    sessionManager,
                    playlists,
                    playlistViewModel)
            }
        }

        libraryTracksSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                libraryTrackViewModel.search()
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }
        })

        libraryTrackViewModel.search()
        playlistViewModel.search()
    }
}