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

        // get currently playin track info
        // when the view is created.
        getTrackInfo(view)

    }

    private fun getTrackInfo(view: View){
        // get track info
        var playing = appVM.getCurrentlyPlayingTrack(requireContext())

        // get each view
        val artistView = view.findViewById<TextView>(R.id.artist_name)
        val trackView = view.findViewById<TextView>(R.id.track_name)
        val imageView = view.findViewById<ImageView>(R.id.track_image)

        var strArray = arrayOf(playing.item.uri)

        view.findViewById<Button>(R.id.addToFav)
            .setOnClickListener { v ->
                if(appVM.addFavPlaylist(requireContext(), strArray) == 201){
                    // vibrate if successful
                    val vibrator = activity!!.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                    val vibrationEffect = VibrationEffect.createOneShot(300, DEFAULT_AMPLITUDE)
                    vibrator.vibrate(vibrationEffect)
                }
            }

        // set info to the views
        if(!playing.currently_playing_type.isNullOrEmpty()) {
            artistView.text =
                if (playing.item.artists.count() == 0) "" else playing.item.artists.first().name
            trackView.text = playing.item.name
            Glide.with(this).load(playing.item.album.images[1].url).into(imageView)
        }

    }

}
