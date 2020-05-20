package com.example.spotifywearapp

import android.content.Context
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

    fun getNewAccessToken(context: Context) {
        repo.getNewAccessToken(context)
    }

    fun storeAuthorizationCode(authorizationCode: String){
        repo.storeAuthorizationCode(authorizationCode)
    }


}