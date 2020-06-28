package com.example.spotifywearapp.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.spotifywearapp.Models.WebAPI.CurrentlyPlayingObject
import com.example.spotifywearapp.R
import com.example.spotifywearapp.ViewModels.AppViewModel
import kotlinx.android.synthetic.main.fragment_home_screen.view.*
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.android.inject
import org.w3c.dom.Text
import kotlinx.coroutines.async as async


class HomeScreenFragment : Fragment() {

    // Lazy injected AppViewModel
    private val appVM : AppViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_screen, container, false)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.get_track_button)
            .setOnClickListener { v ->
                getTrackInfo(view)
            }

    }

    private fun getTrackInfo(view: View){
        // get track info
        var playing = CurrentlyPlayingObject()

            playing = appVM.getCurrentlyPlayingTrack(requireContext())

        // get each view
        val artistView = view.findViewById<TextView>(R.id.artist_name)
        val albumView = view.findViewById<TextView>(R.id.album_name)
        val trackView = view.findViewById<TextView>(R.id.track_name)
        val imageView = view.findViewById<ImageView>(R.id.track_image)

        // set info to the views
        artistView.text = if(playing.item.artists.count() == 0) "" else playing.item.artists.first().name
        albumView.text = playing.item.album.name
        trackView.text = playing.item.name

    }

}
