package com.iwmh.spotifywearapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import com.iwmh.spotifywearapp.R



class StartPageFragment : Fragment() {

    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get navController
        navController = findNavController(view)
        // get ok button
        var okButton = view.findViewById<Button>(R.id.okButton)
        okButton.setOnClickListener{
            navController.popBackStack()
        }
    }

}