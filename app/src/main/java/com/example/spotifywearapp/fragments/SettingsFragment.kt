package com.example.spotifywearapp.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.wear.widget.SwipeDismissFrameLayout
import com.example.spotifywearapp.R
import com.example.spotifywearapp.viewmodels.SettingsViewModel
import org.koin.android.ext.android.inject

class SettingsFragment : Fragment() {

    private val callback = object : SwipeDismissFrameLayout.Callback() {

        override fun onSwipeStarted(layout: SwipeDismissFrameLayout) {
            // optional
            print("started")
        }

        override fun onSwipeCanceled(layout: SwipeDismissFrameLayout) {
            // optional
            print("canceled")
        }

        override fun onDismissed(layout: SwipeDismissFrameLayout) {
            // Code here for custom behavior such as going up the
            // back stack and destroying the fragment but staying in the app.
            val navController = findNavController()
            navController.popBackStack()
        }
    }

    private val settingsVM : SettingsViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = SwipeDismissFrameLayout(activity).apply {

        inflater.inflate(
            R.layout.settings_fragment,
            this,
            false
        ).also { inflatedView ->
            addView(inflatedView)
        }
        addCallback(callback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareShuffleButton(view)
    }

    private fun prepareShuffleButton(view: View) {

            // get the current playback in the background
            settingsVM.getCurrentPlaybacksShuffleState(requireContext())

            val shuffle = view.findViewById<ImageView>(R.id.shuffle_mode)
            // set shuffle drawable
            shuffle.apply {
                setImageResource(R.drawable.ic_shuffle_24px)
            }

        val shuffleModeObserver = Observer<Boolean> { newShuffleMode ->

                var tint: Int = -1
                tint = if (newShuffleMode) {
                    Color.GREEN
                } else {
                    Color.WHITE
                }
                shuffle.drawable.setTint(tint)


                // set onClickListener
                shuffle.setOnClickListener { v ->
                    val vibrator = activity!!.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                    val vibrationEffect = VibrationEffect.createOneShot(150,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                    vibrator.vibrate(vibrationEffect)
                    settingsVM.toggleShufflePlayback(
                        requireContext(),
                        !settingsVM.shuffleMode.value!!
                    )
                }

            }

            settingsVM.shuffleMode.observe(viewLifecycleOwner, shuffleModeObserver)

    }
}


