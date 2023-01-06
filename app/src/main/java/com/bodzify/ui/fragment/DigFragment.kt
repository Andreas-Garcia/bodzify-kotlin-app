package com.bodzify.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SearchView
import androidx.fragment.app.activityViewModels
import com.bodzify.R
import com.bodzify.ui.adapter.MineTrackListAdapter
import com.bodzify.viewmodelpattern.viewmodel.LibraryTrackViewModel
import com.bodzify.viewmodelpattern.viewmodel.MineTrackViewModel

class DigFragment : BaseFragment() {

    private val mineTrackViewModel: MineTrackViewModel by activityViewModels {
        MineTrackViewModel.Factory
    }
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

        mineTrackViewModel.mineTracksDug.observe(viewLifecycleOwner) {
                mineTracks ->
            mineTrackListView.adapter = MineTrackListAdapter(
                requireActivity(),
                sessionManager,
                mineTracks?: arrayListOf(),
                mineTrackViewModel
            )
        }

        digSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    mineTrackViewModel.dig(query)
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }
        })
    }
}