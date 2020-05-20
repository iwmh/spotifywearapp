package com.example.spotifywearapp

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

data class AccessTokenResponse(
    val access_token: String,
    val token_type: String,
    val scope: String,
    val expires_in: String,
    val refresh_token: String

) {
    // Access Token Deseriarizer
    class Deserializer : ResponseDeserializable<AccessTokenResponse>{
        override fun deserialize(content: String): AccessTokenResponse? {
            return Gson().fromJson(content, AccessTokenResponse::class.java)
        }
    }
}