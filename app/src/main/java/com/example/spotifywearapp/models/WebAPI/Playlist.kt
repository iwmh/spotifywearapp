package com.example.spotifywearapp.models.WebAPI

data class Playlist(
    val collaborative: Boolean = false,
    val description: String = "",
    val external_urls: ExternalUrls = ExternalUrls(),
    val href: String = "",
    val id: String = "",
    val images: List<Image> = listOf(),
    val name: String = "",
    val owner: Owner = Owner(),
    val primary_color: Any = Any(),
    val `public`: Boolean = false,
    val snapshot_id: String = "",
    val tracks: Tracks = Tracks(),
    val type: String = "",
    val uri: String = ""
)