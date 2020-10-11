package com.iwmh.spotifywearapp.models.WebAPI

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

data class Playback(
    val actions: Actions = Actions(),
    val context: Context = Context(),
    val currently_playing_type: String = "",
    val device: Device = Device(),
    val is_playing: Boolean = false,
    val item: Item = Item(),
    val progress_ms: Int = 0,
    val repeat_state: String = "",
    val shuffle_state: Boolean = false,
    val timestamp: Long = 0
){
    // Access Token Deserializer
    class Deserializer : ResponseDeserializable<Playback> {
        override fun deserialize(content: String): Playback? {
            return Gson().fromJson(content, Playback::class.java)
        }
    }
}