package com.example.spotifywearapp

class Secrets {
    var client_id: String = ""
    var client_secret: String = ""

    constructor() : super() {}

    constructor(clientId: String, clientSecret: String):super(){
        this.client_id = clientId
        this.client_secret = clientSecret
    }
}