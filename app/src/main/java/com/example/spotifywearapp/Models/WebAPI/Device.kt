package com.example.spotifywearapp.Models.WebAPI

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

data class Device(
    val id: String = "",
    val is_active: Boolean = false,
    val is_private_session: Boolean = false,
    val is_restricted: Boolean = false,
    val name: String = "",
    val type: String = "",
    val volume_percent: Int = 0
){
    // Access Token Deserializer
    class Deserializer : ResponseDeserializable<Device> {
        override fun deserialize(content: String): Device? {
            return Gson().fromJson(content, Device::class.java)
        }
    }
}