package com.example.spotifywearapp

import android.net.Uri
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.support.wearable.authentication.OAuthClient
import android.view.View
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.android.ext.android.inject
import java.io.BufferedReader


class MainActivity : FragmentActivity() {

    // Lazy injected AuthTokenViewModel
    //private val authTokenVM: AuthTokenViewModel by inject()
    private val authTokenVM : AuthTokenViewModel by inject()

    // Note that normally the redirect URL would be your own server, which would in turn
    // redirect to this URL intercepted by the Android Wear companion app after completing the
    // auth code exchange.
    private val HTTP_REDIRECT_URL = "http://localhost/callback"

    private var CLIENT_ID = ""
    private var CLIENT_SECRET = ""

    private val SCOPES = "user-modify-playback-state user-library-modify playlist-read-private playlist-modify-public playlist-modify-private user-read-playback-state user-read-currently-playing"

    private var mOAuthClient: OAuthClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mOAuthClient = OAuthClient.create(this)

        // read secrets.json
        val jsonFileString = getJsonDataFromAsset(applicationContext, "secrets.json")
        val gson = Gson()
        val secretsType = object : TypeToken<Secrets>(){}.type
        val secrets: Secrets = gson.fromJson(jsonFileString, secretsType)
        // set client id and secrets
        this.CLIENT_ID = secrets.client_id
        this.CLIENT_SECRET = secrets.client_secret

        val sample = authTokenVM.helloworld()

        setContentView(R.layout.activity_main)

        // Enables Always-on
//        setAmbientEnabled()
    }

    override fun onDestroy() {
        super.onDestroy()
        mOAuthClient!!.destroy()
    }

    private fun redirectUrl(): String? {
        // Ensure you register the redirect URI in your Google OAuth 2.0 client configuration.
        // Normally this would be the server that would handle the token exchange after receiving
        // the authorization code.
        return HTTP_REDIRECT_URL + "/" + applicationContext.packageName
    }

    fun onClickStartOAuth2Flow(view: View?) {
        val url: String

        val builder = Uri.Builder()
        builder.scheme("https")
            .authority("accounts.spotify.com")
            .appendPath("authorize")
            .appendQueryParameter("response_type", "code")
            .appendQueryParameter("client_id", CLIENT_ID)
            .appendQueryParameter("scope", Uri.encode(SCOPES))
            .appendQueryParameter("redirect_uri", HTTP_REDIRECT_URL)

        url = builder.toString()

        performRequest(
            url, OAuth2RequestCallback(this)
        )
    }

    class OAuth2RequestCallback(mainActivity: MainActivity) : OAuthClient.Callback() {
        override fun onAuthorizationError(errorCode: Int) {
            TODO("Not yet implemented")
        }

        override fun onAuthorizationResponse(requestUrl: Uri?, responseUrl: Uri?) {
            Runnable {

            }
        }


    }


    /**
     * This method should be called with any OAuth 2.0 URL scheme and for any provider. The callback
     * object is called after the user provides consent on the authorization screen on the Android
     * Wear companion app.
     */
    private fun performRequest(
        url: String,
        @Nullable callback: OAuthClient.Callback
    ) {
        mOAuthClient!!.sendAuthorizationRequest(Uri.parse(url), callback)
    }


}

