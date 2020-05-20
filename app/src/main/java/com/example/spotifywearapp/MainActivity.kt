package com.example.spotifywearapp

import android.net.Uri
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.support.wearable.authentication.OAuthClient
import android.view.View
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.android.ext.android.inject
import java.io.BufferedReader

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

