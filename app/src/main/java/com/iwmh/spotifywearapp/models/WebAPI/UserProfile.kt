package com.iwmh.spotifywearapp.models.WebAPI

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

data class UserProfile(
    val country: String = "",
    val display_name: String = "",
    val email: String = "",
    val explicit_content: ExplicitContent = ExplicitContent(),
    val external_urls: ExternalUrls = ExternalUrls(),
    val followers: Followers = Followers(),
    val href: String = "",
    val id: String = "",
    val images: List<Any> = listOf(),
    val product: String = "",
    val type: String = "",
    val uri: String = ""
){
    // Access Token Deserializer
    class Deserializer : ResponseDeserializable<UserProfile> {
        override fun deserialize(content: String): UserProfile? {
            return Gson().fromJson(content, UserProfile::class.java)
        }
    }
}