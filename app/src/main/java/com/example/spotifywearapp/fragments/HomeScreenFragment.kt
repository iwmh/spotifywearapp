package com.example.spotifywearapp.fragments

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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.wear.widget.drawer.WearableNavigationDrawerView
import com.bumptech.glide.Glide
import com.example.spotifywearapp.R
import com.example.spotifywearapp.viewmodels.AppViewModel
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject


class HomeScreenFragment : Fragment(),
                           WearableNavigationDrawerView.OnItemSelectedListener {

    // Lazy injected AppViewModel
    private val appVM : AppViewModel by inject()

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

        appVM.storeTargetPlaylistId(requireContext())

        navController = findNavController(view)

        // Initialize top navigation drawer
        wearableNavigationDrawer = view.findViewById(R.id.top_navigation_drawer)
        wearableNavigationDrawer.setAdapter(NavigationAdapter(requireContext()))

        wearableNavigationDrawer.addOnItemSelectedListener(this)

        // get currently playing track info
        // when the view is created.
        getTrackInfo(view)
    }

    // on item selected
    override fun onItemSelected(pos: Int) {
        when(pos){
            0 -> ""
            1 -> navController.navigate(R.id.settingsFragment)
            2 -> ""
            else -> ""
        }
    }


    private class NavigationAdapter(private val context: Context) : WearableNavigationDrawerView.WearableNavigationDrawerAdapter() {
        override fun getItemText(pos: Int): CharSequence {
            when(pos){
                0 -> return "target playlist"
                1 -> return "shuffle"
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
            val currentTimeView = view.findViewById<TextView>(R.id.current_time)

            // set current time
            var currentTime = DateFormat.format("HH:mm", Calendar.getInstance().time)
            currentTimeView.text = currentTime

            // get shimmer views
            val shimmerImageView = view.findViewById<ImageView>(R.id.shimmer_image_view) as ShimmerFrameLayout
            val shimmerTrackView = view.findViewById<TextView>(R.id.shimmer_track_view) as ShimmerFrameLayout
            val shimmerArtistView = view.findViewById<TextView>(R.id.shimmer_artist_view) as ShimmerFrameLayout

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

                        // start confirmation activity
                        val intent = Intent(context, ConfirmationActivity::class.java).apply {
                            putExtra(
                                ConfirmationActivity.EXTRA_ANIMATION_TYPE,
                                ConfirmationActivity.SUCCESS_ANIMATION
                            )
                            putExtra(ConfirmationActivity.EXTRA_MESSAGE, "Track Added")
                        }
                        startActivity(intent)
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

            // Clear the background color
            imageView.setBackgroundColor(Color.TRANSPARENT)
            trackView.setBackgroundColor(Color.TRANSPARENT)
            artistView.setBackgroundColor(Color.TRANSPARENT)

            // Clear the shimmer
            shimmerImageView.hideShimmer()
            shimmerTrackView.hideShimmer()
            shimmerArtistView.hideShimmer()

        }

    }


}



