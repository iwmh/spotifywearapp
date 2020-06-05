package com.example.spotifywearapp.ViewModels

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.spotifywearapp.Repositories.ApiRepositoryImpl
import com.example.spotifywearapp.Repositories.StorageRepositoryImpl
import com.example.spotifywearapp.Utils.Constants
import com.github.kittinunf.fuel.core.Headers
import org.junit.After
import org.junit.Before

import org.junit.jupiter.api.Assertions.*
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.robolectric.RobolectricTestRunner
import java.time.LocalDateTime

@RunWith(RobolectricTestRunner::class)
internal class AppViewModelTest: KoinTest {

    companion object {
        lateinit var context: Context
        lateinit var apiRepository: ApiRepositoryImpl
        lateinit var storageRepository: StorageRepositoryImpl
        lateinit var viewModel: AppViewModel
    }

    // Set up
    @Before
    fun before(){
        context = ApplicationProvider.getApplicationContext()
        apiRepository = ApiRepositoryImpl(
            ApplicationProvider.getApplicationContext()
        )
        storageRepository = StorageRepositoryImpl(
            ApplicationProvider.getApplicationContext()
        )
        viewModel = AppViewModel(apiRepository, storageRepository)
    }

    @org.junit.Test
    fun accessTokenIsNotValid() {

        var prefs = context.getSharedPreferences("SaveData", Context.MODE_PRIVATE)

        var expires_at = LocalDateTime.parse("2019-07-28T15:32:02.754").toString()
        with(prefs.edit()) {
            putString(Constants.expires_at, expires_at)
            commit()
        }

        var now = LocalDateTime.parse("2019-07-28T15:32:02.755")

        assert(!viewModel.isAccessTokenValid(context, now, 0))

        stopKoin()

    }

    @org.junit.Test
    fun accessTokenIsValid() {
        var prefs = context.getSharedPreferences("SaveData", Context.MODE_PRIVATE)

        var expires_at = LocalDateTime.parse("2019-07-28T15:32:02.754").toString()
        with(prefs.edit()) {
            putString(Constants.expires_at, expires_at)
            commit()
        }

        var now = LocalDateTime.parse("2019-07-28T15:32:02.753")

        assert(viewModel.isAccessTokenValid(context, now, 0))

        stopKoin()

    }

    @org.junit.Test
    fun accessTokenIsInvalidBySomeMargin() {
        var prefs = context.getSharedPreferences("SaveData", Context.MODE_PRIVATE)

        var expires_at = LocalDateTime.parse("2019-07-28T15:32:02.754").toString()
        with(prefs.edit()) {
            putString(Constants.expires_at, expires_at)
            commit()
        }

        var now = LocalDateTime.parse("2019-07-28T15:31:32.755")

        assert(!viewModel.isAccessTokenValid(context, now, 30))

        stopKoin()

    }

    @org.junit.Test
    fun accessTokenIsInvalidBySomeMargin2() {
        var prefs = context.getSharedPreferences("SaveData", Context.MODE_PRIVATE)

        var expires_at = LocalDateTime.parse("2019-07-28T15:32:02.754").toString()
        with(prefs.edit()) {
            putString(Constants.expires_at, expires_at)
            commit()
        }

        var now = LocalDateTime.parse("2019-07-28T15:31:32.754")

        assert(!viewModel.isAccessTokenValid(context, now, 30))

        stopKoin()

    }

    @org.junit.Test
    fun accessTokenIsValidBySomeMargin() {
        var prefs = context.getSharedPreferences("SaveData", Context.MODE_PRIVATE)

        var expires_at = LocalDateTime.parse("2019-07-28T15:32:02.754").toString()
        with(prefs.edit()) {
            putString(Constants.expires_at, expires_at)
            commit()
        }

        var now = LocalDateTime.parse("2019-07-28T15:31:32.753")

        assert(viewModel.isAccessTokenValid(context, now, 30))

        stopKoin()

    }

    @org.junit.Test
    fun createAuthorizationHeader() {
        var prefs = context.getSharedPreferences("SaveData", Context.MODE_PRIVATE)

        var accessToken = "access_token_2020_06_02"
        with(prefs.edit()) {
            putString(Constants.access_token, accessToken)
            commit()
        }

        var expected = mapOf( Headers.AUTHORIZATION to "access_token_2020_06_02")

        assertEquals(expected, viewModel.createAuthorizationHeader(context))

        stopKoin()

    }

    @After
    fun after(){
        var prefs = context.getSharedPreferences("SaveData", Context.MODE_PRIVATE)
        prefs.edit().clear().commit()
        stopKoin()
    }

}