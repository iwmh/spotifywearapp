package com.iwmh.spotifywearapp.models.WebAPI

data class Artist(
    val external_urls: ExternalUrls = ExternalUrls(),
    val href: String = "",
    val id: String = "",
    val name: String = "",
    val type: String = "",
    val uri: String = ""
)