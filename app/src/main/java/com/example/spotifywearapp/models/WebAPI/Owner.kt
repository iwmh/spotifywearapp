package com.example.spotifywearapp.models.WebAPI

data class Owner(
    val display_name: String = "",
    val external_urls: ExternalUrls = ExternalUrls(),
    val href: String = "",
    val id: String = "",
    val type: String = "",
    val uri: String = ""
)