package com.example.spotifywearapp.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.spotifywearapp.models.Secrets
import com.example.spotifywearapp.models.WebAPI.CurrentlyPlayingObject
import com.example.spotifywearapp.repositories.ApiRepository
import com.example.spotifywearapp.repositories.StorageRepository
import com.example.spotifywearapp.utils.Constants
import com.example.spotifywearapp.utils.convertToExpiresInToAt
import com.example.spotifywearapp.utils.getJsonDataFromAsset
import com.github.kittinunf.fuel.core.Headers
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import java.time.LocalDateTime
import kotlin.coroutines.CoroutineContext

class FirstViewModel(val apiRepository: ApiRepository, val storageRepository: StorageRepository) : ViewModel(), CoroutineScope{

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    /**
     * Exchange the authorization code for an access token (and a refresh token)
     */
    fun exchangeCodeForAccessToken(context: Context, authCode: String, code_verifier: String) {
        runBlocking {

            launch(context = Dispatchers.IO) {

                // exchange code for token
                var accessTokenResult = apiRepository.exchangeCodeForAccessToken(context, authCode, code_verifier)

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

    // Store data to local storage
    fun storeDataToStorage(context: Context, key: String, value: String){
        storageRepository.storeDataToStorage(context, key, value)
    }

    // Read data from local storage
    fun readDataFromStorage(context: Context, key: String): String{
        return storageRepository.readDataFromStorage(context, key)
    }


}