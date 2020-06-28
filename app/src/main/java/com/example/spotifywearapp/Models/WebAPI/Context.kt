package com.example.spotifywearapp.Models.WebAPI

data class Context(
    val external_urls: ExternalUrls = ExternalUrls(),
    val href: String = "",
    val type: String = "",
    val uri: String = ""
)