package com.example.spotifywearapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import org.koin.android.ext.android.inject

class HomeScreenFragment : Fragment() {

    // Lazy injected AuthTokenViewModel
    private val authTokenVM : AuthTokenViewModel by inject()

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

        view.findViewById<Button>(R.id.get_token)
            .setOnClickListener { v ->
                obtainAccessToken()
            }

    }

    private fun obtainAccessToken() {
        // obtain access token
        authTokenVM.getNewAccessToken(requireContext())

    }

}
