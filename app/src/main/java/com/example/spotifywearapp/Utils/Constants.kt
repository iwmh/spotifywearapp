package com.example.spotifywearapp.Utils

class Constants {
    companion object {

        const val access_token_url = "https://accounts.spotify.com/api/token"
        const val currently_playing_object_url = "https://api.spotify.com/v1/me/player/currently-playing"
        const val current_playback= "https://api.spotify.com/v1/me/player"

        const val authorization_code = "swo_authorization_code"
        const val access_token = "swo_access_token"
        const val expires_at = "swo_expires_at"
        const val refresh_token = "swo_refresh_token"

        const val margin_seconds = 30

    }
}
