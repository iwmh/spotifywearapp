package com.iwmh.spotifywearapp.models.WebAPI

data class Album(
    val album_type: String = "",
    val artists: List<Artist> = listOf(),
    val available_markets: List<String> = listOf(),
    val external_urls: ExternalUrls = ExternalUrls(),
    val href: String = "",
    val id: String = "",
    val images: List<Image> = listOf(),
    val name: String = "",
    val release_date: String = "",
    val release_date_precision: String = "",
    val total_tracks: Int = 0,
    val type: String = "",
    val uri: String = ""
)