package com.example.spotifywearapp

import android.content.Context
import android.util.Base64.encode
import android.util.Base64.encodeToString
import com.github.kittinunf.fuel.util.encodeBase64
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import kotlin.text.Charsets.UTF_8

// get JSON string from json file
fun getJsonDataFromAsset(context: Context, fileName: String): String? {
    val jsonString: String
    try {
        jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
    } catch (ioException: IOException) {
        ioException.printStackTrace()
        return null
    }
    return jsonString
}

fun createBase64String(context: Context): String {
    val jsonFileString = getJsonDataFromAsset(context, "secrets.json")
    val gson = Gson()
    val secretsType = object : TypeToken<Secrets>(){}.type
    val secrets: Secrets = gson.fromJson(jsonFileString, secretsType)

    // get client id and secrets
    val CLIENT_ID = secrets.client_id
    val CLIENT_SECRET = secrets.client_secret

    val charset = Charsets.UTF_8
    val byteArray = (CLIENT_ID + ":" + CLIENT_SECRET).toByteArray(charset)

    // flag → DEFAULT
    return encodeToString(byteArray, 0)
}