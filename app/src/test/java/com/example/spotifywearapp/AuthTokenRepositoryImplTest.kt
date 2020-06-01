package com.example.spotifywearapp

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.robolectric.RobolectricTestRunner
import java.time.LocalDateTime

@RunWith(RobolectricTestRunner::class)
class AuthTokenRepositoryImplTest : KoinTest{

    companion object {
        lateinit var context: Context
        lateinit var impl: AuthTokenRepositoryImpl
    }

    // Set up
    @Before
    fun before(){
        context = ApplicationProvider.getApplicationContext()
        impl = AuthTokenRepositoryImpl(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun accessTokenIsNotValid() {

        var prefs = context.getSharedPreferences("SaveData", Context.MODE_PRIVATE)

        var expires_at = LocalDateTime.parse("2019-07-28T15:32:02.754").toString()
        with(prefs.edit()) {
            putString(Constants.expires_at, expires_at)
            commit()
        }

        var now = LocalDateTime.parse("2019-07-28T15:32:02.755")

        assert(!impl.isAccessTokenValid(context, now, 0))

        stopKoin()

    }

    @Test
    fun accessTokenIsValid() {
        var prefs = context.getSharedPreferences("SaveData", Context.MODE_PRIVATE)

        var expires_at = LocalDateTime.parse("2019-07-28T15:32:02.754").toString()
        with(prefs.edit()) {
            putString(Constants.expires_at, expires_at)
            commit()
        }

        var now = LocalDateTime.parse("2019-07-28T15:32:02.753")

        assert(impl.isAccessTokenValid(context, now, 0))

        stopKoin()

    }

    @Test
    fun accessTokenIsInvalidBySomeMargin() {
        var prefs = context.getSharedPreferences("SaveData", Context.MODE_PRIVATE)

        var expires_at = LocalDateTime.parse("2019-07-28T15:32:02.754").toString()
        with(prefs.edit()) {
            putString(Constants.expires_at, expires_at)
            commit()
        }

        var now = LocalDateTime.parse("2019-07-28T15:31:32.755")

        assert(!impl.isAccessTokenValid(context, now, 30))

        stopKoin()

    }

    @Test
    fun accessTokenIsInvalidBySomeMargin2() {
        var prefs = context.getSharedPreferences("SaveData", Context.MODE_PRIVATE)

        var expires_at = LocalDateTime.parse("2019-07-28T15:32:02.754").toString()
        with(prefs.edit()) {
            putString(Constants.expires_at, expires_at)
            commit()
        }

        var now = LocalDateTime.parse("2019-07-28T15:31:32.754")

        assert(!impl.isAccessTokenValid(context, now, 30))

        stopKoin()

    }

    @Test
    fun accessTokenIsValidBySomeMargin() {
        var prefs = context.getSharedPreferences("SaveData", Context.MODE_PRIVATE)

        var expires_at = LocalDateTime.parse("2019-07-28T15:32:02.754").toString()
        with(prefs.edit()) {
            putString(Constants.expires_at, expires_at)
            commit()
        }

        var now = LocalDateTime.parse("2019-07-28T15:31:32.753")

        assert(impl.isAccessTokenValid(context, now, 30))

        stopKoin()

    }

    @After
    fun after(){
        var prefs = context.getSharedPreferences("SaveData", Context.MODE_PRIVATE)
        prefs.edit().clear().commit()
        stopKoin()
    }

}