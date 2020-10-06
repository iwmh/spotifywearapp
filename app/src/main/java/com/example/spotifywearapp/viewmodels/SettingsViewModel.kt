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
import com.example.spotifywearapp.utils.convertToExpiresInToAt
import com.github.kittinunf.fuel.core.Headers
import kotlinx.coroutines.*
import java.time.LocalDateTime
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
            // check the access token
            checkAccessToken(context)
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
            // check the access token
            checkAccessToken(context)
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