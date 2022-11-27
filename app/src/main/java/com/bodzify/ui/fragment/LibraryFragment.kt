package com.bodzify.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SearchView
import androidx.fragment.app.activityViewModels
import com.bodzify.R
import com.bodzify.ui.adapter.LibraryTrackListAdapter
import com.bodzify.ui.adapter.PlaylistListAdapter
import com.bodzify.ui.adapter.item.PlaylistAsymmetricItem
import com.bodzify.viewmodel.LibraryTrackViewModel
import com.bodzify.viewmodel.PlayerViewModel
import com.bodzify.viewmodel.PlaylistViewModel
import com.felipecsl.asymmetricgridview.library.Utils
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridView
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridViewAdapter

class LibraryFragment(private val libraryTrackViewModel: LibraryTrackViewModel,
                      private val playlistViewModel: PlaylistViewModel) : BaseFragment() {
    private val playerViewModel: PlayerViewModel by activityViewModels()

    private lateinit var libraryTracksSearchView: SearchView
    private lateinit var libraryTracksListView: ListView
    private lateinit var playlistsAsymmetricGridView: AsymmetricGridView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        libraryTrackViewModel.libraryTracksSearched.observe(viewLifecycleOwner) {
                libraryTracks ->
            libraryTracksListView.adapter = LibraryTrackListAdapter(
                requireActivity(),
                libraryTracks ?: arrayListOf(),
                playerViewModel)
        }
        playlistViewModel.playlistsSearched.observe(viewLifecycleOwner) {
                playlists ->
            if(playlists != null) {
                playlistsAsymmetricGridView.setRequestedColumnWidth(Utils.dpToPx(context, 75F))

                // Setting to true will move items up and down to better use the space
                playlistsAsymmetricGridView.isAllowReordering = true;
                val asymmetricItems: MutableList<PlaylistAsymmetricItem> = ArrayList()
                for((i, playlist) in playlists.withIndex()) {
                    asymmetricItems.add(PlaylistAsymmetricItem(playlist, i))
                }
                val adapter = PlaylistListAdapter(
                    requireActivity(),
                    sessionManager,
                    asymmetricItems,
                    playlistViewModel)
                playlistsAsymmetricGridView.adapter =
                    AsymmetricGridViewAdapter<PlaylistAsymmetricItem>(
                        context,
                        playlistsAsymmetricGridView,
                        adapter)
                /*playlistsAsymmetricGridView.adapter = LibraryTrackListAdapter(
                    requireActivity(),
                    libraryTracks ?: arrayListOf(),
                    playerViewModel)*/
            }
        }
        return inflater.inflate(R.layout.fragment_library, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        libraryTracksSearchView = requireView().findViewById(R.id.library_tracks_search_view)
        libraryTracksListView = requireView().findViewById(R.id.library_tracks_list_view)
        playlistsAsymmetricGridView = requireView()
            .findViewById(R.id.playlists_asymmetric_grid_view) as AsymmetricGridView

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