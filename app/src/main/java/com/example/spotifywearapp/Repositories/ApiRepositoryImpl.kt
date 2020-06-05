package com.example.spotifywearapp.Repositories

import android.app.Application
import android.content.Context
import com.example.spotifywearapp.Models.AccessTokenResponse
import com.example.spotifywearapp.Models.Secrets
import com.example.spotifywearapp.Utils.Constants
import com.example.spotifywearapp.Utils.createAuthorizationHeaderForNewAccessToken
import com.example.spotifywearapp.Utils.getSecrets
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers

class ApiRepositoryImpl(app: Application) : ApiRepository {

    /*
        Get access token from the authentication code


        TODO: the testing below when you finish the implementations:
        ① getting an authorization code
        ↓
        ② getting an access token from the authorization code
        ↓
        ③ getting an access token from the refresh token
     */
    override fun getNewAccessToken(context: Context, authCode: String): AccessTokenResponse {

        var ret =  AccessTokenResponse("","","",0,"")

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
        val header = createAuthorizationHeaderForNewAccessToken(context)

        // Request
        val response = Fuel.post(
                Constants.access_token_url,
                param
        )
        .header(Headers.CONTENT_TYPE, "application/x-www-form-urlencoded")
        .header(header)
        .responseObject(AccessTokenResponse.Deserializer()){
            req, res, result ->
                var (accessTokenResult, err) = result

                // TODO: implementation of the flow for the err

            if (accessTokenResult != null) {
                ret = accessTokenResult
            }
        }

        return ret
    }

    override fun refreshAccessToken(context: Context, refreshToken: String, authHeader: Map<String, String>): AccessTokenResponse{

        var ret =  AccessTokenResponse("","","",0,"")

        // Request Body
        val param = listOf(
            "grant_type" to "refresh_token",
            "refresh_token" to refreshToken
        )


        // Request
        val response = Fuel.post(
            Constants.access_token_url,
            param
        )
            .header(Headers.CONTENT_TYPE, "application/x-www-form-urlencoded")
            .header(authHeader)
            .responseObject(AccessTokenResponse.Deserializer()){
                    req, res, result ->
                val(accessTokenResult, err) = result

                // TODO: implementation of the flow for the err

                if (accessTokenResult != null) {
                    ret = accessTokenResult
                }

            }

        return ret
    }



}