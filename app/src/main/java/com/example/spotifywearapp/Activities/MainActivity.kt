package com.example.spotifywearapp.Activities

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.wear.ambient.AmbientModeSupport
import com.example.spotifywearapp.R

class MainActivity : FragmentActivity(), AmbientModeSupport.AmbientCallbackProvider {
        /*
        * Declare an ambient mode controller, which will be used by
        * the activity to determine if the current mode is ambient.
        */
    private lateinit var ambientController: AmbientModeSupport.AmbientController

    override fun getAmbientCallback(): AmbientModeSupport.AmbientCallback = MyAmbientCallback()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        ambientController = AmbientModeSupport.attach(this)

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    inner class MyAmbientCallback : AmbientModeSupport.AmbientCallback() {

        var navController = findNavController(R.id.fragment4)

        override fun onEnterAmbient(ambientDetails: Bundle?) {
            // Handle entering ambient mode

            // If the current destination is the FirstFragment, it won't navigate.
            var currentDest = navController.currentDestination
            if(currentDest?.label != "fragment_first_screen"){
                navController.navigate(R.id.ambientScreenFragment)
            }
        }

        override fun onExitAmbient() {
            // Handle exiting ambient mode
            var currentDest = navController.currentDestination
            // If the current destination is the FirstFragment, it won't navigate.

            if(currentDest?.label != "fragment_first_screen"){
                navController.popBackStack()
            }
        }

        override fun onUpdateAmbient() {
            // Update the content
            var kjsfh = navController.currentDestination
        }
    }




}

