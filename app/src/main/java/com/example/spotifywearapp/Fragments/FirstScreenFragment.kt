package com.example.spotifywearapp.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.wearable.authentication.OAuthClient
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.Nullable
import androidx.navigation.fragment.findNavController
import com.example.spotifywearapp.ViewModels.AuthTokenViewModel
import com.example.spotifywearapp.Utils.Constants
import com.example.spotifywearapp.R
import com.example.spotifywearapp.Models.Secrets
import com.example.spotifywearapp.Utils.getSecrets
import org.koin.android.ext.android.inject

class FirstScreenFragment : Fragment() {

    // Lazy injected AuthTokenViewModel
    private val authTokenVM : AuthTokenViewModel by inject()

    // Note that normally the redirect URL would be your own server, which would in turn
    // redirect to this URL intercepted by the Android Wear companion app after completing the
    // auth code exchange.
    private var HTTP_REDIRECT_URL = ""

    private var CLIENT_ID = ""
    private var CLIENT_SECRET = ""

    private val SCOPES = "user-modify-playback-state user-library-modify playlist-read-private playlist-modify-public playlist-modify-private user-read-playback-state user-read-currently-playing"

    private var mOAuthClient: OAuthClient? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first_screen, container, false)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        // See if the device has an authorization code
        // If the device has an authorization code,
        // navigate to home screen.
//        if(authTokenVM.hasAuthorizationCode(requireContext())){
//            val navController = findNavController()
//            navController.navigate(R.id.homeScreenFragment)
//        }

        // start the auth flow.
        mOAuthClient = OAuthClient.create(requireContext())

        // get secrets
        val secrets: Secrets =
            getSecrets(requireContext())

        // set client id and secrets
        this.CLIENT_ID = secrets.client_id
        this.CLIENT_SECRET = secrets.client_secret
        this.HTTP_REDIRECT_URL = secrets.redirect_url

        view.findViewById<Button>(R.id.btn_next_screen)
            .setOnClickListener { v ->
                onClickStartOAuth2Flow(v)
            }
    }

    private fun redirectUrl(context: Context): String? {
        // Ensure you register the redirect URI in your Google OAuth 2.0 client configuration.
        // Normally this would be the server that would handle the token exchange after receiving
        // the authorization code.
        return HTTP_REDIRECT_URL + "/" + context.packageName
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
            url, OAuth2RequestCallback()
        )
    }

    inner class OAuth2RequestCallback() : OAuthClient.Callback() {
        override fun onAuthorizationError(errorCode: Int) {
            TODO("Not yet implemented")
        }

        override fun onAuthorizationResponse(requestUrl: Uri?, responseUrl: Uri?) {
            // get Authorization Code from URL
            var authorizationCode = responseUrl!!.getQueryParameter("code")

            // Store Authorization Code to storage
            authTokenVM.storeDataToStorage(requireContext(),
                Constants.authorization_code, authorizationCode)

            if(authTokenVM.hasAuthorizationCode(requireContext())){
                val navController = findNavController()
                navController.navigate(R.id.homeScreenFragment)
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

