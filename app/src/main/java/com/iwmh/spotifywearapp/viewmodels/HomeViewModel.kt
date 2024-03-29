package com.iwmh.spotifywearapp.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iwmh.spotifywearapp.models.Secrets
import com.iwmh.spotifywearapp.models.WebAPI.CurrentlyPlayingObject
import com.iwmh.spotifywearapp.repositories.ApiRepository
import com.iwmh.spotifywearapp.repositories.StorageRepository
import com.iwmh.spotifywearapp.utils.Constants
import com.iwmh.spotifywearapp.utils.convertToExpiresInToAt
import com.iwmh.spotifywearapp.utils.getJsonDataFromAsset
import com.github.kittinunf.fuel.core.Headers
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import java.time.LocalDateTime
import kotlin.coroutines.CoroutineContext

class HomeViewModel(val apiRepository: ApiRepository, val storageRepository: StorageRepository) : ViewModel(), CoroutineScope{

    // LiveData
    val currentTime: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val artistName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val imageUrl: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val trackName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val shimmerColor: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    // Refresh access token
    fun refreshAccessToken(context: Context) {

        runBlocking {

            launch(context = Dispatchers.IO) {

                // read refresh token from storage
                var refreshToken = readDataFromStorage(context, Constants.refresh_token)

                val accessTokenResult =
                    apiRepository.refreshAccessToken(context, refreshToken)

                if(accessTokenResult != null) {

                    // Store access token
                    storeDataToStorage(
                        context,
                        Constants.access_token,
                        accessTokenResult!!.access_token
                    )
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
                    storeDataToStorage(
                        context,
                        Constants.refresh_token,
                        accessTokenResult!!.refresh_token
                    )
                }

            }
        }
    }

    // Store data to local storage
    fun storeDataToStorage(context: Context, key: String, value: String){
        storageRepository.storeDataToStorage(context, key, value)
    }

    // Read data from local storage
    fun readDataFromStorage(context: Context, key: String): String{
        return storageRepository.readDataFromStorage(context, key)
    }

    // Check if the access token is valid or not
    fun isAccessTokenValid(context: Context, time: LocalDateTime, marginSeconds: Int): Boolean {

        // read expires_at time
        val expiresAt = LocalDateTime.parse(readDataFromStorage(context, Constants.expires_at))

        val marginedExpiresAt = expiresAt.minusSeconds(marginSeconds.toLong())

        return if (time.compareTo(marginedExpiresAt) < 0) true else false

    }

    // create authorization header from access token
    fun createAuthorizationHeader(context: Context): Map<String, String>{
        // read access token from storage
        val accessToken = readDataFromStorage(context, Constants.access_token)
        // return authorization header
        return mapOf(
            Headers.AUTHORIZATION to "Bearer $accessToken"
        )
    }

    // Check the validity of access token and refresh it if expired
    fun checkAccessToken(context: Context){
        if(!isAccessTokenValid(context, LocalDateTime.now(), Constants.margin_seconds))        {
            refreshAccessToken(context)
        }
    }

    // Get the User's Currently Playing Track
    suspend fun getCurrentlyPlayingTrack(context: Context): CurrentlyPlayingObject {
        // check the access token
        checkAccessToken(context)
        // create authorization header
        val authHeader = createAuthorizationHeader(context)
        // call repo's function
        return apiRepository.getCurrentlyPlayingTrack(context, authHeader)
    }

    fun storeTargetPlaylistId(context: Context){
        // * temporary implementation
        val jsonFileString = getJsonDataFromAsset(
            context,
            "secrets.json"
        )
        val gson = Gson()
        val secretsType = object : TypeToken<Secrets>(){}.type
        val secrets: Secrets = gson.fromJson(jsonFileString, secretsType)

    }

    /*
        temporary implementaion
     */
    fun addFavPlaylist(context: Context, tracks: Array<String>): Int{

        var ret = 0
        runBlocking {
            launch(context = Dispatchers.IO) {
                // check the access token
                checkAccessToken(context)
                val playlist_id_fav = readDataFromStorage(context, Constants.add_to_playlist_id)
                // create authorization header
                val authHeader = createAuthorizationHeader(context)
                // call repo's function
                ret = apiRepository.addTracksToPlaylist(context, authHeader, playlist_id_fav, tracks)
            }

        }

        return ret
    }

}