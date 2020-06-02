package com.example.spotifywearapp.Activities

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.example.spotifywearapp.R

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Enables Always-on
//        setAmbientEnabled()
    }

    override fun onDestroy() {
        super.onDestroy()
    }



}

