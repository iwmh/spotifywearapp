package com.example.spotifywearapp

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers

class AuthTokenRepositoryImpl(app: Application) : AuthTokenRepository{
    override var accessToken: String = ""

    override var expiresIn: String = ""

    override var expiresAt: String = ""

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

    // Store Data to Storage
    override fun storeDataToStorage(context: Context, key: String, value: String) {
        var prefs = context.getSharedPreferences("SaveData", Context.MODE_PRIVATE)
        with(prefs.edit()){
            putString(key, value)
            commit()
        }
    }

    // Read Data from Storage
    override fun readDataFromStorage(context: Context, key: String): String {
        var prefs = context.getSharedPreferences("SaveData", Context.MODE_PRIVATE)
        return prefs.getString(key, "")
    }

}