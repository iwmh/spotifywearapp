package com.example.spotifywearapp

import android.content.Context
import java.time.LocalDateTime

interface  AuthTokenRepository{

    var accessToken: String

    var expiresAt: String

    var refreshToken: String

    // Newly obtain the access token
    fun getNewAccessToken(context: Context)

    // Refresh the access token
    fun refreshAccessToken(context: Context)

    // Store Data to Storage
    fun storeDataToStorage(context: Context, key: String, value: String)

    // Read Data from Storage
    fun readDataFromStorage(context: Context, key: String): String

    // Check if the access token is expired or not
    fun isAccessTokenValid(context: Context, now: LocalDateTime, marginSeconds: Int = 0): Boolean

    fun createAuthorizationHeader(context: Context): Map<String, String>

}