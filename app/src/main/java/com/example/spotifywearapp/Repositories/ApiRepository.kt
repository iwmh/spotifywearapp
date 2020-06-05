package com.example.spotifywearapp.Repositories

import android.content.Context
import com.example.spotifywearapp.Models.AccessTokenResponse
import java.time.LocalDateTime

interface  ApiRepository{

    // Newly obtain the access token
    fun getNewAccessToken(context: Context, authCode: String): AccessTokenResponse?

    // Refresh the access token
    fun refreshAccessToken(context: Context, refreshToken: String, authHeader: Map<String, String>): AccessTokenResponse

}