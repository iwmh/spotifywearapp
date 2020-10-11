package com.iwmh.spotifywearapp.repositories

import android.content.Context

interface StorageRepository {

    // Store Data to Storage
    fun storeDataToStorage(context: Context, key: String, value: String)

    // Read Data from Storage
    fun readDataFromStorage(context: Context, key: String): String
}