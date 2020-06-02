package com.example.spotifywearapp.Models

class Secrets {
    var client_id: String = ""
    var client_secret: String = ""
    var redirect_url: String = ""

    constructor() : super() {}

    constructor(clientId: String, clientSecret: String, redirectUrl: String):super(){
        this.client_id = clientId
        this.client_secret = clientSecret
        this.redirect_url = redirectUrl
    }
}