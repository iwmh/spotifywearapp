package com.example.spotifywearapp

interface  AuthTokenRepository{

    var accessToken: String

    var expiresIn: String

    var expiresAt: String

    fun getNewAccessToken(authorizationCode: String): Void

    fun helloworld(): String
}