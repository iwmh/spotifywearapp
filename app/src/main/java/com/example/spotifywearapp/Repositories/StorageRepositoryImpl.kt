package com.example.spotifywearapp.Repositories

import android.app.Application
import android.content.Context

class StorageRepositoryImpl(app: Application): StorageRepository {

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