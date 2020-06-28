package com.example.spotifywearapp.ViewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.spotifywearapp.Models.WebAPI.CurrentlyPlayingObject
import com.example.spotifywearapp.Repositories.ApiRepository
import com.example.spotifywearapp.Repositories.StorageRepository
import com.example.spotifywearapp.Utils.Constants
import com.example.spotifywearapp.Utils.convertToExpiresInToAt
import com.github.kittinunf.fuel.core.Headers
import kotlinx.coroutines.*
import java.time.LocalDateTime
import kotlin.coroutines.CoroutineContext

class AppViewModel(val apiRepository: ApiRepository, val storageRepository: StorageRepository) : ViewModel(), CoroutineScope{

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    // Newly obtain the access token
    fun getNewAccessToken(context: Context) {
        runBlocking {

            launch(context = Dispatchers.IO) {

                // read auth code from storage
                var authCode = readDataFromStorage(context, Constants.authorization_code)

                var accessTokenResult = apiRepository.getNewAccessToken(context, authCode)

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

    // Refresh access token
    fun refreshAccessToken(context: Context) {

        runBlocking {

            launch(context = Dispatchers.IO) {

                // read refresh token from storage
                var refreshToken = readDataFromStorage(context, Constants.refresh_token)

                val accessTokenResult =
                    apiRepository.refreshAccessToken(context, refreshToken)

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

    // See if the device has an authorization code
    fun hasAuthorizationCode(context: Context): Boolean{
        return !storageRepository.readDataFromStorage(context, Constants.authorization_code).isNullOrBlank()
    }

    // Check if the access token is valid or not
    fun isAccessTokenValid(context: Context, time: LocalDateTime, marginSeconds: Int): Boolean {

        // read expires_at time
        val expiresAt = LocalDateTime.parse(readDataFromStorage(context, Constants.expires_at))

        val marginedExpiresAt = expiresAt.minusSeconds(marginSeconds.toLong())

        return if (time.compareTo(marginedExpiresAt) < 0) true else false

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

    // Check the validity of access token and refresh it if expired
    fun checkAccessToken(context: Context){
        if(!isAccessTokenValid(context, LocalDateTime.now(), Constants.margin_seconds))        {
            refreshAccessToken(context)
        }
    }

    // Get the User's Currently Playing Track
    fun getCurrentlyPlayingTrack(context: Context): CurrentlyPlayingObject {
        var ret = CurrentlyPlayingObject()
        runBlocking {
            launch(context = Dispatchers.IO) {
                // check the access token
                checkAccessToken(context)
                // create authorization header
                val authHeader = createAuthorizationHeader(context)
                // call repo's function
                ret = apiRepository.getCurrentlyPlayingTrack(context, authHeader)
            }

        }
        return ret
    }





}