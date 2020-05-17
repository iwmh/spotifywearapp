package com.example.spotifywearapp

import androidx.lifecycle.ViewModel

class AuthTokenViewModel(val repo: AuthTokenRepository) : ViewModel(){

    // access token
    fun getAccessToken(): String {
       return repo.accessToken
    }

    // expires_in
    fun getExpiresIn(): String {
       return repo.expiresIn
    }

    // expires_at
    fun getExpiresAt(): String {
       return repo.expiresAt
    }

    fun getNewAccessToken(authorizationCode: String) {
        repo.getNewAccessToken(authorizationCode)
    }

    fun helloworld(): String{
        return repo.helloworld()
    }

}