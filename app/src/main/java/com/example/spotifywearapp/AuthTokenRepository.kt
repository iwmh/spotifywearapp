package com.example.spotifywearapp

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

interface  AuthTokenRepository{

    var accessToken: String

    var expiresAt: String

    var refreshToken: String

    // Newly obtain the access token
    fun getNewAccessToken(context: Context)

    // Store Data to Storage
    fun storeDataToStorage(context: Context, key: String, value: String)

    // Read Data from Storage
    fun readDataFromStorage(context: Context, key: String): String


}