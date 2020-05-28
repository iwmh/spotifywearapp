package com.example.spotifywearapp

import android.app.Application
import android.content.Context
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AuthTokenRepositoryImpl(app: Application) : AuthTokenRepository{
    override var accessToken: String = ""

    override var expiresIn: String = ""

    override var expiresAt: String = ""

    override fun getNewAccessToken(context: Context){

        // read auth code from storage
        var authCode = readDataFromStorage(context, Constants.authorization_code)

        // read redirect url from JSON
        val jsonFileString = getJsonDataFromAsset(context, "secrets.json")
        val gson = Gson()
        val secretsType = object : TypeToken<Secrets>(){}.type
        val secrets: Secrets = gson.fromJson(jsonFileString, secretsType)

        // set client id and secrets
        var redirectUrl = secrets.redirect_url

        // Request Body
        val param = listOf(
            "grant_type" to "authorization_code",
            "code" to authCode,
            "redirect_uri" to redirectUrl
        )

        // Base64 encoded "client_id : client_secret"
        val base64String = createBase64String(context)

        // Request Header
        val header = mapOf(
            Headers.AUTHORIZATION to "Basic " + base64String
        )

        // Request
        val response = Fuel.post(
                Constants.access_token_url,
                param
        )
        .header(Headers.CONTENT_TYPE, "application/x-www-form-urlencoded")
        .header(header)
        .responseObject(AccessTokenResponse.Deserializer()){
            req, res, result ->
                val(accessTokenResult, err) = result
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