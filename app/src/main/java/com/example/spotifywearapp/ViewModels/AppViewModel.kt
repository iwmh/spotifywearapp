package com.example.spotifywearapp.ViewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.spotifywearapp.Repositories.ApiRepository
import com.example.spotifywearapp.Repositories.StorageRepository
import com.example.spotifywearapp.Utils.Constants
import com.example.spotifywearapp.Utils.convertToExpiresInToAt
import com.github.kittinunf.fuel.core.Headers
import java.time.LocalDateTime

class AppViewModel(val apiRepository: ApiRepository, val storageRepository: StorageRepository) : ViewModel(){

    // Newly obtain the access token
    fun getNewAccessToken(context: Context) {
        // read auth code from storage
        var authCode = readDataFromStorage(context, Constants.authorization_code)

        var accessTokenResult = apiRepository.getNewAccessToken(context, authCode)

        // Store access token
        storeDataToStorage(context, Constants.access_token, accessTokenResult!!.access_token)
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
        storeDataToStorage(context, Constants.refresh_token, accessTokenResult!!.refresh_token)

    }

    // Refresh access token
    fun refreshAccessToken(context: Context) {
        // read refresh token from storage
        var refreshToken = readDataFromStorage(context, Constants.refresh_token)
        // create Authorization header
        val header = createAuthorizationHeader(context)

        val accessTokenResult = apiRepository.refreshAccessToken(context, refreshToken, header)

        // Store access token
        storeDataToStorage(context, Constants.access_token, accessTokenResult!!.access_token)
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
            Headers.AUTHORIZATION to accessToken
        )
    }




}