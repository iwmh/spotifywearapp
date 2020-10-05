package com.example.spotifywearapp.models.WebAPI

import kotlinx.serialization.Serializable

@Serializable
data class PlaybackReqBody(
    val context_uri: String = "",
    val offset: Offset = Offset(),
    val position_ms: Int = 0
)