package com.example.spotifywearapp

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.time.LocalDateTime
import java.util.*

internal class UtilKtTest {

    @Test
    fun convertToExpiresInToAt() {
        var now = LocalDateTime.parse("2019-07-28T15:32:02.754")
        var expected = LocalDateTime.parse("2019-07-28T15:32:32.754")
        var expiredAt = com.example.spotifywearapp.convertToExpiresInToAt(now, 30)
        assertEquals(expected.toString(), expiredAt)
    }
}