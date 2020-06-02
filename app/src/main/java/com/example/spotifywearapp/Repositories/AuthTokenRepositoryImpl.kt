package com.example.spotifywearapp.Repositories

import android.app.Application
import android.content.Context
import com.example.spotifywearapp.Models.AccessTokenResponse
import com.example.spotifywearapp.Models.Secrets
import com.example.spotifywearapp.Utils.Constants
import com.example.spotifywearapp.Utils.convertToExpiresInToAt
import com.example.spotifywearapp.Utils.getSecrets
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers
import java.time.LocalDateTime

class AuthTokenRepositoryImpl(app: Application) :
    AuthTokenRepository {
    override var accessToken: String = ""

    override var expiresAt: String = ""

    override var refreshToken: String = ""

    /*
        Get access token from the authentication code


        TODO: the testing below when you finish the implementations:
        ① getting an authorization code
        ↓
        ② getting an access token from the authorization code
        ↓
        ③ getting an access token from the refresh token
     */
    override fun getNewAccessToken(context: Context){

        // read auth code from storage
        var authCode = readDataFromStorage(context, Constants.authorization_code)

        // get secrets
        val secrets: Secrets =
            getSecrets(context)

        // set client id and secrets
        var redirectUrl = secrets.redirect_url

        // Request Body
        val param = listOf(
            "grant_type" to "authorization_code",
            "code" to authCode,
            "redirect_uri" to redirectUrl
        )

        // Request Header
        val header = createAuthorizationHeader(context)

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

                // TODO: implementation of the flow for the err

                // Store access token
                storeDataToStorage(context, Constants.access_token, accessTokenResult!!.access_token)
                // Store expires_at (converted from expires_in)
                storeDataToStorage(
                    context,
                    Constants.expires_at,
                    convertToExpiresInToAt(
                        LocalDateTime.now(),
                        accessTokenResult!!.expires_in
                    )
                )
                // Store refresh token
                storeDataToStorage(context, Constants.refresh_token, accessTokenResult!!.refresh_token)

        }


    }

    override fun refreshAccessToken(context: Context){

        // read refresh token from storage
        var refreshToken = readDataFromStorage(context, Constants.refresh_token)

        // Request Body
        val param = listOf(
            "grant_type" to "refresh_token",
            "refresh_token" to refreshToken
        )

        val header = createAuthorizationHeader(context)

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

                // TODO: implementation of the flow for the err

                // Store access token
                storeDataToStorage(context, Constants.access_token, accessTokenResult!!.access_token)
                // Store expires_at (converted from expires_in)
                storeDataToStorage(
                    context,
                    Constants.expires_at,
                    convertToExpiresInToAt(
                        LocalDateTime.now(),
                        accessTokenResult!!.expires_in
                    )
                )

            }
    }

    // Check if the access token is valid or not
    override fun isAccessTokenValid(context: Context, time: LocalDateTime, marginSeconds: Int): Boolean {

        // read expires_at time
        val expiresAt = LocalDateTime.parse(readDataFromStorage(context, Constants.expires_at))

        val marginedExpiresAt = expiresAt.minusSeconds(marginSeconds.toLong())

        return if (time.compareTo(marginedExpiresAt) < 0) true else false

    }

    // create authorization header from access token
    override fun createAuthorizationHeader(context: Context): Map<String, String>{
        // read access token from storage
        val accessToken = readDataFromStorage(context, Constants.access_token)
        // return authorization header
        return mapOf(
                Headers.AUTHORIZATION to accessToken
                )
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