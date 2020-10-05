package com.example.spotifywearapp.models.WebAPI

import kotlinx.serialization.Serializable

@Serializable
data class Offset(
    val position: Int = 0
)