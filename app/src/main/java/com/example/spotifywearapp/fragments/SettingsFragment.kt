package com.example.spotifywearapp.fragments

import android.graphics.Color
import android.os.Bundle
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

            settingsVM.getCurrentPlaybacksShuffleState(requireContext())

            val shuffle = view.findViewById<ImageView>(R.id.shuffle_mode)
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
            }

            settingsVM.shuffleMode.observe(viewLifecycleOwner, shuffleModeObserver)

            shuffle.setOnClickListener { v ->
                val code = settingsVM.toggleShufflePlayback(
                    requireContext(),
                    !settingsVM.shuffleMode.value!!
                )
                if (code == 204) {
                    settingsVM.shuffleMode.value = !settingsVM.shuffleMode.value!!
                } else {
                    settingsVM.shuffleMode.value = settingsVM.shuffleMode.value!!
                }
            }

    }
}


