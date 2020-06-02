package com.example.spotifywearapp.ViewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.spotifywearapp.Repositories.AuthTokenRepository
import com.example.spotifywearapp.Utils.Constants

class AuthTokenViewModel(val repo: AuthTokenRepository) : ViewModel(){

    // access token
    fun getAccessToken(): String {
       return repo.accessToken
    }
    fun setAccessToken(accessToken: String){
        repo.accessToken = accessToken
    }

    // expires_at
    fun getExpiresAt(): String {
       return repo.expiresAt
    }
    fun setExpiresAt(expiresAt: String){
        repo.expiresAt = expiresAt
    }

    fun getNewAccessToken(context: Context) {
        repo.getNewAccessToken(context)
    }

    fun storeDataToStorage(context: Context, key: String, value: String){
        repo.storeDataToStorage(context, key, value)
    }

    fun readDataFromStorage(context: Context, key: String): String{
        return repo.readDataFromStorage(context, key)
    }

    // See if the device has an authorization code
    fun hasAuthorizationCode(context: Context): Boolean{
        return !repo.readDataFromStorage(context, Constants.authorization_code).isNullOrBlank()
    }



}