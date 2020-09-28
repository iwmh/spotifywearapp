package com.example.spotifywearapp.repositories

import android.content.Context
import com.example.spotifywearapp.models.AccessTokenResponse
import com.example.spotifywearapp.models.WebAPI.CurrentlyPlayingObject
import com.example.spotifywearapp.models.WebAPI.Playback
import com.example.spotifywearapp.models.WebAPI.Playlists

interface  ApiRepository{

    // Newly obtain the access token
    suspend fun exchangeCodeForAccessToken(context: Context, authCode: String, code_verifier: String): AccessTokenResponse?

    // Refresh the access token
    suspend fun refreshAccessToken(context: Context, refreshToken: String): AccessTokenResponse?

    // Get the User's Currently Playing Track
    suspend fun getCurrentlyPlayingTrack(context: Context, authHeader: Map<String, String>): CurrentlyPlayingObject

    // Get the User's Current Playback
    fun getCurrentPlayback(context: Context, authHeader: Map<String, String>): Playback

    // Toggle Shuffle For User's Playback
    suspend fun toggleShufflePlayback(context: Context, authHeader: Map<String, String>, state: Boolean): Boolean

    // get the list of playlists
    suspend fun getListOfPlaylists(context: Context, authHeader: Map<String, String>): Playlists
    /*
     temporary impl
     */
    suspend fun addTracksToPlaylist(context: Context, authHeader: Map<String, String>, playlistIdFav: String, tracks: Array<String>): Int

}