package com.example.spotifywearapp

class AuthTokenRepositoryImpl() : AuthTokenRepository{
    override var accessToken: String
        get() = accessToken
        set(value) {
            accessToken = value
        }
    override var expiresIn: String
        get() = expiresIn
        set(value) {
            expiresIn = value
        }
    override var expiresAt: String
        get() = expiresAt
        set(value) {
            expiresAt = value
        }

    override fun getNewAccessToken(authorizationCode: String): Void {
        TODO("Not yet implemented")
    }

    override fun helloworld(): String {
        return "Hello World!"
    }

}