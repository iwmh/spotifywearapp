package com.iwmh.spotifywearapp.fragments

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.icu.util.Calendar
import android.os.Bundle
import android.os.VibrationEffect
import android.os.VibrationEffect.DEFAULT_AMPLITUDE
import android.os.Vibrator
import android.support.wearable.activity.ConfirmationActivity
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.wear.widget.drawer.WearableNavigationDrawerView
import com.bumptech.glide.Glide
import com.iwmh.spotifywearapp.R
import com.iwmh.spotifywearapp.utils.Constants
import com.iwmh.spotifywearapp.viewmodels.HomeViewModel
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject


class HomeScreenFragment : Fragment(),
                           WearableNavigationDrawerView.OnItemSelectedListener {

    // Lazy injected AppViewModel
    private val homeVM : HomeViewModel by inject()

    private lateinit var wearableNavigationDrawer: WearableNavigationDrawerView

    private lateinit var navController : NavController

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

        navController = findNavController(view)

        // if playlist_id is not stored, navigate to ToPlaylist page.
        // ...in order for the StartPage to popBack, additionally navigate to startPageFragment.
        val playlist_id = homeVM.readDataFromStorage(requireContext(), Constants.add_to_playlist_id)
        if(playlist_id.isNullOrEmpty()){
            navController.navigate(R.id.toPlaylistFragment)
            navController.navigate(R.id.startPageFragment)
        }

        // get each view
        val currentTimeView = view?.findViewById<TextView>(R.id.current_time)
        val imageView = view?.findViewById<ImageView>(R.id.track_image)
        val trackView = view?.findViewById<TextView>(R.id.track_name)
        val artistView = view?.findViewById<TextView>(R.id.artist_name)

        // Create the observer which updates the UI.
        // current time
        val currentTimeObserver = Observer<String> { newTime ->
            currentTimeView.text = newTime
        }
        homeVM.currentTime.observe(viewLifecycleOwner, currentTimeObserver)
        // image
        val imageUrlObserver = Observer<String> { newUrl ->
            Glide.with(requireContext()).load(newUrl).into(imageView)
        }
        homeVM.imageUrl.observe(viewLifecycleOwner, imageUrlObserver)
        // track name
        val trackNameObserver= Observer<String> { newTrackName ->
            trackView.text = newTrackName
        }
        homeVM.trackName.observe(viewLifecycleOwner, trackNameObserver)
        // artist name
        val artistNameObserver = Observer<String> { newArtistName ->
            artistView.text = newArtistName
        }
        homeVM.artistName.observe(viewLifecycleOwner, artistNameObserver)
        // shimmer color
        val shimmerColorObserver = Observer<Int> { newColor ->
            imageView.setBackgroundColor(newColor)
            trackView.setBackgroundColor(newColor)
            artistView.setBackgroundColor(newColor)
        }
        homeVM.shimmerColor.observe(viewLifecycleOwner, shimmerColorObserver)

        // ...temporal implementation
        homeVM.storeTargetPlaylistId(requireContext())

        // Initialize top navigation drawer
        wearableNavigationDrawer = view.findViewById(R.id.top_navigation_drawer)
        wearableNavigationDrawer.setAdapter(NavigationAdapter(requireContext()))
        wearableNavigationDrawer.addOnItemSelectedListener(this)

        // get currently playing track info
        // when the view is created.
        getTrackInfo()
    }

    // on item selected
    override fun onItemSelected(pos: Int) {
        when(pos){
            0 -> navController.navigate(R.id.toPlaylistFragment)
            1 -> navController.navigate(R.id.settingsFragment)
            2 -> navController.navigate(R.id.playlistsFragment)
        }
    }

    private class NavigationAdapter(private val context: Context) : WearableNavigationDrawerView.WearableNavigationDrawerAdapter() {
        override fun getItemText(pos: Int): CharSequence {
            when(pos){
                0 -> return "target playlist"
                1 -> return "settings"
                2 -> return "playlists"
                else -> return ""
            }
        }

        override fun getItemDrawable(pos: Int): Drawable {
            val resources: Resources = context.getResources()
            var resourceId: Int = 0
            lateinit var item : Drawable
            when(pos) {
                0 -> {
                    resourceId = resources.getIdentifier(
                    "ic_playlist_add_check_24px", "drawable",
                    context.getPackageName())
                }
                1 -> {
                    resourceId = resources.getIdentifier(
                        "ic_settings_24px", "drawable",
                        context.getPackageName())
                }
                2 -> {
                    resourceId = resources.getIdentifier(
                        "ic_playlist_play_24px", "drawable",
                        context.getPackageName()
                    )
                }

            }

            item = resources.getDrawable(resourceId)
            item.setTint(-1)
            return item

        }

        override fun getCount(): Int {
            return 3
        }

    }

    private fun getTrackInfo(){

        var view = view

        viewLifecycleOwner.lifecycleScope.launch {

            // get track info
            var playing = withContext(Dispatchers.Default) {
                homeVM.getCurrentlyPlayingTrack(requireContext())
            }

            // set current time
            var currentTime = DateFormat.format("HH:mm", Calendar.getInstance().time)
            homeVM.currentTime.value = currentTime.toString()

            // get shimmer views
            val shimmerImageView = view?.findViewById(R.id.shimmer_image_view) as ShimmerFrameLayout
            val shimmerTrackView = view?.findViewById(R.id.shimmer_track_view) as ShimmerFrameLayout
            val shimmerArtistView = view?.findViewById(R.id.shimmer_artist_view) as ShimmerFrameLayout

            var strArray = arrayOf(playing.item.uri)

            // Click Image to add a track to playlist
            view.findViewById<ImageView>(R.id.track_image)
                .setOnClickListener { v ->
                    val vibrator = activity!!.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

                    // Vibrate shortly when clicked
                    val vibrationEffect = VibrationEffect.createOneShot(80, DEFAULT_AMPLITUDE)
                    vibrator.vibrate(vibrationEffect)

                    if (homeVM.addFavPlaylist(requireContext(), strArray) == 201) {
                        // vibrate (a little bit longer) if successful
                        val vibrationEffect = VibrationEffect.createOneShot(300, DEFAULT_AMPLITUDE)
                        vibrator.vibrate(vibrationEffect)

                        // get playlist name
                        val playlistName = homeVM.readDataFromStorage(requireContext(), Constants.add_to_playlist_name)

                        // start confirmation activity
                        val intent = Intent(context, ConfirmationActivity::class.java).apply {
                            putExtra(
                                ConfirmationActivity.EXTRA_ANIMATION_TYPE,
                                ConfirmationActivity.SUCCESS_ANIMATION
                            )
                            putExtra(ConfirmationActivity.EXTRA_MESSAGE, "Added to $playlistName")
                        }
                        startActivity(intent)
                    }
                }

            // set info to the views
            if (!playing.currently_playing_type.isNullOrEmpty()) {
                if (playing.item.artists.count() == 0) {
                    homeVM.artistName.value = ""
                } else {
                    homeVM.artistName.value = playing.item.artists.first().name
                }
                homeVM.trackName.value = playing.item.name
                homeVM.imageUrl.value = playing.item.album.images[1].url
            } else {
                homeVM.trackName.value = "No Track Playing"
            }

            // if the track is in some playlist, store the currently playing playlist id
            val uri = if(playing.context != null) playing.context.uri else ""
            val playlistId = uri.split(":").last()
            homeVM.storeDataToStorage(requireContext(), Constants.currently_playing_playlist_id, playlistId)

            // Clear the background color
            homeVM.shimmerColor.value = Color.TRANSPARENT

            // Clear the shimmer
            shimmerImageView.hideShimmer()
            shimmerTrackView.hideShimmer()
            shimmerArtistView.hideShimmer()

        }

    }


}



