package com.example.spotifywearapp.Models.WebAPI

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

class SnapshotId (
    val snapshot_id: String = ""
){
    // Access Token Deserializer
    class Deserializer : ResponseDeserializable<SnapshotId> {
        override fun deserialize(content: String): SnapshotId? {
            return Gson().fromJson(content, SnapshotId::class.java)
        }
    }
}