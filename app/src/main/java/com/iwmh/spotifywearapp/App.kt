package com.iwmh.spotifywearapp

import android.app.Application
import com.iwmh.spotifywearapp.repositories.ApiRepository
import com.iwmh.spotifywearapp.repositories.ApiRepositoryImpl
import com.iwmh.spotifywearapp.repositories.StorageRepository
import com.iwmh.spotifywearapp.repositories.StorageRepositoryImpl
import com.iwmh.spotifywearapp.viewmodels.FirstViewModel
import com.iwmh.spotifywearapp.viewmodels.HomeViewModel
import com.iwmh.spotifywearapp.viewmodels.PlaylistsViewModel
import com.iwmh.spotifywearapp.viewmodels.SettingsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.android.ext.koin.androidLogger

class App : Application(){

    override fun onCreate(){
        super.onCreate()
        // Start Koin
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModule)
        }
    }

    private val appModule = module {
        // single instance of ApiRepository
        single<ApiRepository> {
            ApiRepositoryImpl(
                androidApplication()
            )
        }
        // single instance of StorageRepository
        single<StorageRepository> {
            StorageRepositoryImpl(
                androidApplication()
            )
        }

        // Simple Presenter Factory
        factory { HomeViewModel(get(), get()) }
        factory { FirstViewModel(get(), get()) }
        factory { SettingsViewModel(get(), get())}
        factory { PlaylistsViewModel(get(), get())}
    }
}