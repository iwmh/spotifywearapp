package com.example.spotifywearapp.Fragments

import android.content.Context
import android.os.Bundle
import android.os.VibrationEffect
import android.os.VibrationEffect.DEFAULT_AMPLITUDE
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.spotifywearapp.Models.Secrets
import com.example.spotifywearapp.Models.WebAPI.CurrentlyPlayingObject
import com.example.spotifywearapp.R
import com.example.spotifywearapp.Utils.getJsonDataFromAsset
import com.example.spotifywearapp.ViewModels.AppViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_home_screen.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

        appVM.storeTargetPlaylistId(requireContext())

        // get currently playing track info
        // when the view is created.
        getTrackInfo(view)

    }

    private fun getTrackInfo(view: View){

        viewLifecycleOwner.lifecycleScope.launch {

            // get track info
            var playing = withContext(Dispatchers.Default) {
                appVM.getCurrentlyPlayingTrack(requireContext())
            }

            // get each view
            val artistView = view.findViewById<TextView>(R.id.artist_name)
            val trackView = view.findViewById<TextView>(R.id.track_name)
            val imageView = view.findViewById<ImageView>(R.id.track_image)

            var strArray = arrayOf(playing.item.uri)

            // Click Image to add a track to playlist
            view.findViewById<ImageView>(R.id.track_image)
                .setOnClickListener { v ->
                    val vibrator = activity!!.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

                    // Vibrate shortly when clicked
                    val vibrationEffect = VibrationEffect.createOneShot(80, DEFAULT_AMPLITUDE)
                    vibrator.vibrate(vibrationEffect)

                    if (appVM.addFavPlaylist(requireContext(), strArray) == 201) {
                        // vibrate (a little bit longer) if successful
                        val vibrationEffect = VibrationEffect.createOneShot(300, DEFAULT_AMPLITUDE)
                        vibrator.vibrate(vibrationEffect)
                    }
                }

            // set info to the views
            if (!playing.currently_playing_type.isNullOrEmpty()) {
                if (playing.item.artists.count() == 0) {
                    artistView.text = ""
                } else {
                    artistView.text = playing.item.artists.first().name
                }
                trackView.text = playing.item.name
                Glide.with(requireContext()).load(playing.item.album.images[1].url).into(imageView)
            } else {
                trackView.text = "No Track Playing"
            }

        }

    }

}
