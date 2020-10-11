package com.iwmh.spotifywearapp

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime

internal class UtilKtTest {

    @Test
    fun convertToExpiresInToAt() {
        var now = LocalDateTime.parse("2019-07-28T15:32:02.754")
        var expected = LocalDateTime.parse("2019-07-28T15:32:32.754")
        var expiredAt =
            com.iwmh.spotifywearapp.utils.convertToExpiresInToAt(now, 30)
        assertEquals(expected.toString(), expiredAt)
    }
}