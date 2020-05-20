package com.example.spotifywearapp

import android.content.Context

interface  AuthTokenRepository{

    var accessToken: String

    var expiresIn: String

    var expiresAt: String

    fun getNewAccessToken(context: Context)

    // Authorization Code
    fun storeAuthorizationCode(authorizationCode: String)
    fun readAuthorizationCode(): String

    // Access Token
    fun storeAccessToken(accessToken: String)
    fun readAccessToken(): String

    // Expires In
    fun storeExpiresIn(expiresIn: String)
    fun readExpiresIn(): String

    // Expires At
    fun storeExpiresAt(expiresAt: String)
    fun readExpiresAt(): String

    // Refresh Token
    fun storeRefreshToken(refreshToken: String)
    fun readRefreshToken(): String

}