package com.example.spotifywearapp

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.time.LocalDateTime
import java.util.*
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
    val clientId = secrets.client_id
    val clientSecret = secrets.client_secret

    val encodedString = "$clientId:$clientSecret"
    val encodedStringBytes = encodedString.toByteArray(UTF_8)

    return Base64.getEncoder().encodeToString(encodedStringBytes)
}

fun convertToExpiresInToAt(expiresAtFrom: LocalDateTime, expiresIn: Int): String{
    return expiresAtFrom.plusSeconds(expiresIn.toLong()).toString()
}

fun getSecrets(context: Context): Secrets{
    // read redirect url from JSON
    val jsonFileString = getJsonDataFromAsset(context, "secrets.json")
    val gson = Gson()
    val secretsType = object : TypeToken<Secrets>(){}.type

    return gson.fromJson(jsonFileString, secretsType)
}
