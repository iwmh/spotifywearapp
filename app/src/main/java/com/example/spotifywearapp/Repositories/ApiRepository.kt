package com.example.spotifywearapp.Repositories

import android.content.Context
import com.example.spotifywearapp.Models.AccessTokenResponse
import com.example.spotifywearapp.Models.WebAPI.CurrentlyPlayingObject

interface  ApiRepository{

    // Newly obtain the access token
    suspend fun exchangeCodeForAccessToken(context: Context, authCode: String, code_verifier: String): AccessTokenResponse?

    // Refresh the access token
    suspend fun refreshAccessToken(context: Context, refreshToken: String): AccessTokenResponse?

    // Get the User's Currently Playing Track
    suspend fun getCurrentlyPlayingTrack(context: Context, authHeader: Map<String, String>): CurrentlyPlayingObject

    /*
     temporary impl
     */
    suspend fun addTracksToPlaylist(context: Context, authHeader: Map<String, String>, playlistIdFav: String, tracks: Array<String>): Int

}