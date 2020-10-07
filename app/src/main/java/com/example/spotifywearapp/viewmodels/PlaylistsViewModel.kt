package com.example.spotifywearapp.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifywearapp.models.WebAPI.PlaybackReqBody
import com.example.spotifywearapp.models.WebAPI.Playlist
import com.example.spotifywearapp.repositories.ApiRepository
import com.example.spotifywearapp.repositories.StorageRepository
import com.example.spotifywearapp.utils.Constants
import com.example.spotifywearapp.utils.convertToExpiresInToAt
import com.github.kittinunf.fuel.core.Headers
import kotlinx.coroutines.*
import java.time.LocalDateTime
import kotlin.coroutines.CoroutineContext

class PlaylistsViewModel(val apiRepository: ApiRepository, val storageRepository: StorageRepository) : ViewModel(), CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    // LiveData
    val listOfPlaylists: MutableLiveData<List<Playlist>> by lazy {
        MutableLiveData<List<Playlist>>()
    }

    val listOfToPlaylists: MutableLiveData<List<Playlist>> by lazy {
        MutableLiveData<List<Playlist>>()
    }

    // Get the list of playlist (including ones created by others)
    fun getListOfPlaylists(context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            // check the access token
            checkAccessToken(context)
            // create authorization header
            val authHeader = createAuthorizationHeader(context)
            // call repo's function
            val playlists = apiRepository.getListOfPlaylists(context, authHeader)

            // get currently playing playlist id
            val currentlyPlayingPlaylistId = readDataFromStorage(context, Constants.currently_playing_playlist_id)
            playlists.find {
                it.id == currentlyPlayingPlaylistId
            }?.currentlyPlaying= true

            listOfPlaylists.postValue(playlists)
        }
    }

    // Get the list of collaborative playlist
    fun getListOfCollaborativePlaylists(context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            // check the access token
            checkAccessToken(context)
            // create authorization header
            val authHeader = createAuthorizationHeader(context)
            // call repo's function
            val playlists = apiRepository.getListOfPlaylists(context, authHeader)
            val userProfile = apiRepository.getCurrentUsersProfile(context, authHeader)
            // get the collaborative playlists
            var collaborativePlaylists = playlists.filter {
                it.owner.display_name == userProfile?.display_name
            }

            // get the target playlist stored to sharedpreferences
            val targetPlaylistId = readDataFromStorage(context, Constants.add_to_playlist_id)
            collaborativePlaylists.find {
                it.id == targetPlaylistId
            }?.currentyTargeted = true

            listOfToPlaylists.postValue(collaborativePlaylists)
        }
    }

    // Get the current playback
    fun playPlaylist(context: Context, context_uri: String) {
        viewModelScope.launch(Dispatchers.IO) {
            // check the access token
            checkAccessToken(context)
            // create authorization header
            val authHeader = createAuthorizationHeader(context)
            // create request body
            val reqBody = PlaybackReqBody(context_uri = context_uri)
            // call repo's function
            apiRepository.startResumePlayback(context, authHeader, reqBody)
        }
    }

    // create authorization header from access token
    fun createAuthorizationHeader(context: Context): Map<String, String>{
        // read access token from storage
        val accessToken = readDataFromStorage(context, Constants.access_token)
        // return authorization header
        return mapOf(
            Headers.AUTHORIZATION to "Bearer $accessToken"
        )
    }

    // Read data from local storage
    fun readDataFromStorage(context: Context, key: String): String{
        return storageRepository.readDataFromStorage(context, key)
    }

    // Store data to local storage
    fun storeDataToStorage(context: Context, key: String, value: String){
        storageRepository.storeDataToStorage(context, key, value)
    }

    // Check the validity of access token and refresh it if expired
    fun checkAccessToken(context: Context){
        if(!isAccessTokenValid(context, LocalDateTime.now(), Constants.margin_seconds))        {
            refreshAccessToken(context)
        }
    }

    // Check if the access token is valid or not
    fun isAccessTokenValid(context: Context, time: LocalDateTime, marginSeconds: Int): Boolean {

        // read expires_at time
        val expiresAt = LocalDateTime.parse(readDataFromStorage(context, Constants.expires_at))

        val marginedExpiresAt = expiresAt.minusSeconds(marginSeconds.toLong())

        return if (time.compareTo(marginedExpiresAt) < 0) true else false

    }

    // Refresh access token
    fun refreshAccessToken(context: Context) {

        runBlocking {

            launch(context = Dispatchers.IO) {

                // read refresh token from storage
                var refreshToken = readDataFromStorage(context, Constants.refresh_token)

                val accessTokenResult =
                    apiRepository.refreshAccessToken(context, refreshToken)

                if(accessTokenResult != null) {

                    // Store access token
                    storeDataToStorage(
                        context,
                        Constants.access_token,
                        accessTokenResult!!.access_token
                    )
                    // Store expires_at (converted from expires_in)
                    storeDataToStorage(
                        context,
                        Constants.expires_at,
                        convertToExpiresInToAt(
                            LocalDateTime.now(),
                            accessTokenResult!!.expires_in
                        )
                    )
                    // Store refresh token
                    storeDataToStorage(
                        context,
                        Constants.refresh_token,
                        accessTokenResult!!.refresh_token
                    )
                }

            }
        }
    }




}