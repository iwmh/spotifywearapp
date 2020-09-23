package com.example.spotifywearapp.viewmodels

import android.content.Context
import android.media.Image
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifywearapp.models.WebAPI.Playback
import com.example.spotifywearapp.repositories.ApiRepository
import com.example.spotifywearapp.repositories.StorageRepository
import com.example.spotifywearapp.utils.Constants
import com.github.kittinunf.fuel.core.Headers
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class SettingsViewModel(val apiRepository: ApiRepository, val storageRepository: StorageRepository) : ViewModel(), CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    // LiveData
    val shuffleMode: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    // Get the current playback
    fun getCurrentPlaybacksShuffleState(context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            // create authorization header
            val authHeader = createAuthorizationHeader(context)
            // call repo's function
            val playback = apiRepository.getCurrentPlayback(context, authHeader)

            shuffleMode.postValue(playback.shuffle_state)
        }
    }

    // Toggle shuffle mode
    fun toggleShufflePlayback(context: Context, state: Boolean){
        viewModelScope.launch {
            // create authorization header
            val authHeader = createAuthorizationHeader(context)
            // call repo's function
            val result = apiRepository.toggleShufflePlayback(context, authHeader, state)
            if(result) {
                shuffleMode.postValue(!shuffleMode.value!!)
            }
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