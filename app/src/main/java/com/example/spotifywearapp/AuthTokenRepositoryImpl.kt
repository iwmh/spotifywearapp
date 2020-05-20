package com.example.spotifywearapp

import android.content.Context
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.HeaderName
import com.github.kittinunf.fuel.core.Headers

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

    override fun getNewAccessToken(context: Context){

        // Request Body
        val body = mapOf(
            "grant_type" to "authorization_code",
            "code" to accessToken,
            "redirect_uri" to "http://localhost/callback"
        )

        // Base64 encoded "client_id : client_secret"
        val base64String = createBase64String(context)

        // Request Header
        val header = mapOf(
            Headers.AUTHORIZATION to "Basic " + base64String
        )

        // Request
        val response = Fuel.post(Constants.authorize_access_url)
            .header(header)
            .body(body.toString())
            .responseObject(AccessTokenResponse.Deserializer()){
                req, res, result ->
                    val(accessTokenResult, err) = result
                    println(accessTokenResult!!.access_token)

            }


    }

    override fun storeAuthorizationCode(authorizationCode: String) {
        TODO("Not yet implemented")
    }

    override fun readAuthorizationCode(): String {
        TODO("Not yet implemented")
    }

    override fun storeAccessToken(accessToken: String) {
        TODO("Not yet implemented")
    }

    override fun readAccessToken(): String {
        TODO("Not yet implemented")
    }

    override fun storeExpiresIn(expiresIn: String) {
        TODO("Not yet implemented")
    }

    override fun readExpiresIn(): String {
        TODO("Not yet implemented")
    }

    override fun storeExpiresAt(expiresAt: String) {
        TODO("Not yet implemented")
    }

    override fun readExpiresAt(): String {
        TODO("Not yet implemented")
    }

    override fun storeRefreshToken(refreshToken: String) {
        TODO("Not yet implemented")
    }

    override fun readRefreshToken(): String {
        TODO("Not yet implemented")
    }


}