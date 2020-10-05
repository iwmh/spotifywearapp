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
import com.github.kittinunf.fuel.core.Headers
import kotlinx.coroutines.*
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

    // Get the current playback
    fun getListOfPlaylists(context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            // create authorization header
            val authHeader = createAuthorizationHeader(context)
            // call repo's function
            val playlists = apiRepository.getListOfPlaylists(context, authHeader)

            listOfPlaylists.postValue(playlists)
        }
    }

    // Get the current playback
    fun playPlaylist(context: Context, context_uri: String) {
        viewModelScope.launch(Dispatchers.IO) {
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



}