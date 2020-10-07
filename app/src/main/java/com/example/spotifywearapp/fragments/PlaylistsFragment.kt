package com.example.spotifywearapp.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.wear.widget.SwipeDismissFrameLayout
import com.example.spotifywearapp.R
import com.example.spotifywearapp.fragments.recyclerviewadapters.PlaylistsRecyclerViewAdapter
import com.example.spotifywearapp.models.WebAPI.Playlist
import com.example.spotifywearapp.viewmodels.PlaylistsViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

/**
 * A fragment representing a list of Items.
 */
class PlaylistsFragment : Fragment() {

    private var columnCount = 1

    private val callback = object : SwipeDismissFrameLayout.Callback() {

        override fun onSwipeStarted(layout: SwipeDismissFrameLayout) {
            // optional
        }

        override fun onSwipeCanceled(layout: SwipeDismissFrameLayout) {
            // optional
        }

        override fun onDismissed(layout: SwipeDismissFrameLayout) {
            // Code here for custom behavior such as going up the
            // back stack and destroying the fragment but staying in the app.
            val navController = findNavController()
            navController.popBackStack()
        }
    }

    private val playlistsVM: PlaylistsViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }

        // get the list of playlists
        lifecycleScope.launch {
            playlistsVM.getListOfPlaylists(requireContext())
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = SwipeDismissFrameLayout(activity).apply  {

        val view = inflater.inflate(
            R.layout.fragment_playlist_list,
            container,
            false)
            .also { inflatedView ->
                addView(inflatedView)
            }

        addCallback(callback)

        // progress bar
        val lo = findViewById<RelativeLayout>(R.id.progressBarHolder)
        val pb = findViewById<ProgressBar>(R.id.progressBar)

        val listOfPlaylistObserver = Observer<List<Playlist>> { newListOfPlaylist->

            // remove progress bar
            lo.removeView(pb)

            // Set the adapter
            var view = findViewById<RecyclerView>(R.id.listofplaylist)
            with(view) {
                adapter = PlaylistsRecyclerViewAdapter(newListOfPlaylist, context, playlistsVM)
                layoutManager = GridLayoutManager(context, columnCount)
            }
        }

        playlistsVM.listOfPlaylists.observe(viewLifecycleOwner, listOfPlaylistObserver)

    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            PlaylistsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}