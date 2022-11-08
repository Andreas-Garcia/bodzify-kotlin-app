package com.bodzify.viewcontroller.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SearchView
import com.bodzify.R
import com.bodzify.viewcontroller.adapter.MineTrackListAdapter
import com.bodzify.viewmodel.MineTrackViewModel

class DigFragment(private val mineTrackViewModel: MineTrackViewModel) : BaseFragment() {

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

        mineTrackViewModel.mineTracksDug.observe(this@DigFragment) {
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