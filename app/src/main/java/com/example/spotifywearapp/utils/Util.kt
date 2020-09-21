package com.example.spotifywearapp.utils

import android.content.Context
import com.example.spotifywearapp.models.Secrets
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.time.LocalDateTime
import java.util.*

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

fun convertToExpiresInToAt(expiresAtFrom: LocalDateTime, expiresIn: Int): String{
    return expiresAtFrom.plusSeconds(expiresIn.toLong()).toString()
}

fun getSecrets(context: Context): Secrets {
    // read redirect url from JSON
    val jsonFileString = getJsonDataFromAsset(
        context,
        "secrets.json"
    )
    val gson = Gson()
    val secretsType = object : TypeToken<Secrets>(){}.type

    return gson.fromJson(jsonFileString, secretsType)
}

// hash with SHA256
fun hashSHA256toBytes(sourceString: String): ByteArray{
    val bytes = sourceString.toByteArray(StandardCharsets.UTF_8)
    val md = MessageDigest.getInstance("SHA-256")
    return md.digest(bytes)
}

// base64 url encode
fun base64UrlEncode(code_verifier: String): String{
    var hashed_code_verifier = hashSHA256toBytes(code_verifier)
    var code_challenge = Base64.getUrlEncoder().encodeToString(hashed_code_verifier)

    // Remove any trailing '='s
    code_challenge = code_challenge.split('=')[0]
    // 62nd char of encoding
    code_challenge = code_challenge.replace('+', '-')
    // 63rd char of encoding
    code_challenge = code_challenge.replace('/', '_')

    return code_challenge

}