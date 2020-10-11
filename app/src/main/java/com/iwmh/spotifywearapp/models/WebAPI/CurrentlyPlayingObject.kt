package com.iwmh.spotifywearapp.models.WebAPI

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

data class CurrentlyPlayingObject(
    val actions: Actions = Actions(),
    val context: Context = Context(),
    val currently_playing_type: String = "",
    val is_playing: Boolean = false,
    val item: Item = Item(),
    val progress_ms: Int = 0,
    val timestamp: Long = 0
) {
    // Access Token Deserializer
    class Deserializer : ResponseDeserializable<CurrentlyPlayingObject> {
        override fun deserialize(content: String): CurrentlyPlayingObject? {
            return Gson().fromJson(content, CurrentlyPlayingObject::class.java)
        }
    }
}