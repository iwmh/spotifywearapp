package com.example.spotifywearapp.models

class Secrets {
    var client_id: String = ""
    var redirect_url: String = ""
    // temporary imp[
    var playlist_id_fav: String = ""

    constructor() : super() {}

    constructor(clientId: String, redirectUrl: String, playlist_id_fav: String):super(){
        this.client_id = clientId
        this.redirect_url = redirectUrl
        this.playlist_id_fav = playlist_id_fav
    }
}