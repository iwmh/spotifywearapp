package com.example.spotifywearapp.Repositories

import android.app.Application
import android.content.Context
import com.example.spotifywearapp.Models.AccessTokenResponse
import com.example.spotifywearapp.Models.Secrets
import com.example.spotifywearapp.Models.WebAPI.CurrentlyPlayingObject
import com.example.spotifywearapp.Models.WebAPI.SnapshotId
import com.example.spotifywearapp.Utils.Constants
import com.example.spotifywearapp.Utils.getSecrets
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers
import com.github.kittinunf.fuel.core.awaitResponseResult


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
    override suspend fun exchangeCodeForAccessToken(context: Context, authCode: String, code_verifier: String): AccessTokenResponse {

        var ret =  AccessTokenResponse("","","",0,"")

        // get secrets from json file
        val secrets: Secrets = getSecrets(context)

        var client_id = secrets.client_id
        var redirectUrl = secrets.redirect_url

        // Request Body
        val param = listOf(
            "client_id" to client_id,
            "grant_type" to "authorization_code",
            "code" to authCode,
            "redirect_uri" to redirectUrl,
            "code_verifier" to code_verifier
        )

        // Request
        val response = Fuel.post(
                Constants.access_token_url,
                param
        )
        .header(Headers.CONTENT_TYPE, "application/x-www-form-urlencoded")
        .awaitResponseResult(AccessTokenResponse.Deserializer())
            var (accessTokenResult, err) = response.third

            // TODO: implementation of the flow for the err

            if (accessTokenResult != null) {
                ret = accessTokenResult
            }


        return ret
    }

    /**
     * Refresh token
     */
    override suspend fun refreshAccessToken(context: Context, refreshToken: String): AccessTokenResponse{

        var ret =  AccessTokenResponse("","","",0,"")

        // get secrets from json file
        val secrets: Secrets = getSecrets(context)

        var client_id = secrets.client_id

        // Request Body
        val param = listOf(
            "grant_type" to "refresh_token",
            "refresh_token" to refreshToken,
            "client_id" to client_id
        )

        // Request
        val response = Fuel.post(
            Constants.access_token_url,
            param
        )
        .header(Headers.CONTENT_TYPE, "application/x-www-form-urlencoded")
        .awaitResponseResult(AccessTokenResponse.Deserializer())
        var (accessTokenResult, err) = response.third

        // TODO: implementation of the flow for the err

        if (accessTokenResult != null) {
            ret = accessTokenResult
        }

        return ret
    }

    // Get the User's Currently Playing Track
    override suspend fun getCurrentlyPlayingTrack(context: Context, authHeader: Map<String, String>): CurrentlyPlayingObject {

        var ret = CurrentlyPlayingObject()

        // Request
        val response = Fuel.get(
            Constants.currently_playing_object_url
        )
        .header(Headers.CONTENT_TYPE, "application/x-www-form-urlencoded")
        .header(authHeader)
        .awaitResponseResult(CurrentlyPlayingObject.Deserializer())
        var (currentlyPlayingResult, err) = response.third

        // TODO: implementation of the flow for the err

        if (currentlyPlayingResult != null) {
            ret = currentlyPlayingResult
        }

        return ret

    }

    /*
        temporary impl
     */
    override suspend fun addTracksToPlaylist(
        context: Context,
        authHeader: Map<String, String>,
        playlistIdFav: String,
        tracks: Array<String>
    ): Int {

        var ret = 0

        var param = listOf(
            "uris" to tracks.toList().joinToString(",")
        )

        // Request
        val response = Fuel.post(
            "https://api.spotify.com/v1/playlists/$playlistIdFav/tracks",
            param
        )
            .header(Headers.CONTENT_TYPE, "application/json")
            .header(authHeader)
            .awaitResponseResult(SnapshotId.Deserializer())

        // TODO: implementation of the flow for the err

        return response.second.statusCode
    }

}