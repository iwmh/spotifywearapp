package com.example.spotifywearapp.models.WebAPI

data class Playlist(
    val collaborative: Boolean = false,
    val href: String = "",
    val id: String = "",
    val images: List<Image> = listOf(),
    val name: String = "",
    val owner: Owner = Owner(),
    val tracks: Tracks = Tracks(),
    val uri: String = "",
    // added prop
    var currentyTargeted: Boolean = false
)