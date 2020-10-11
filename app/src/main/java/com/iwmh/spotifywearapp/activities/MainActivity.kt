package com.iwmh.spotifywearapp.activities

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.wear.ambient.AmbientModeSupport
import com.iwmh.spotifywearapp.R

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

        }

        override fun onExitAmbient() {
            // Handle exiting ambient mode
            var currentDest = navController.currentDestination

            // TODO
            // achieve update-page-on-exit by navigating to home
            if(currentDest?.label == "fragment_home_screen"){
                navController.navigate(R.id.homeScreenFragment)
            }
        }

        override fun onUpdateAmbient() {
            // Update the content
        }
    }

}

