package com.iwmh.spotifywearapp.models.WebAPI

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

data class Playlists(
    val href: String = "",
    val items: List<Playlist> = listOf(),
    val limit: Int = 0,
    val next: String = "",
    val offset: Int = 0,
    val previous: Any = Any(),
    val total: Int = 0
){
    class Deserializer : ResponseDeserializable<Playlists> {
        override fun deserialize(content: String): Playlists? {
            return Gson().fromJson(content, Playlists::class.java)
        }
    }
}