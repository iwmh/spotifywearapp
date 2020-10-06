package com.example.spotifywearapp.models

class Secrets {
    var client_id: String = ""
    var redirect_url: String = ""

    constructor() : super() {}

    constructor(clientId: String, redirectUrl: String):super(){
        this.client_id = clientId
        this.redirect_url = redirectUrl
    }
}