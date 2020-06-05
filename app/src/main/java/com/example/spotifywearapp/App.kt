package com.example.spotifywearapp

import android.app.Application
import com.example.spotifywearapp.Repositories.ApiRepository
import com.example.spotifywearapp.Repositories.ApiRepositoryImpl
import com.example.spotifywearapp.Repositories.StorageRepository
import com.example.spotifywearapp.Repositories.StorageRepositoryImpl
import com.example.spotifywearapp.ViewModels.AuthTokenViewModel
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
        // single instance of AuthTokenRepository
        single<ApiRepository> {
            ApiRepositoryImpl(
                androidApplication()
            )
        }
        single<StorageRepository> {
            StorageRepositoryImpl(
                androidApplication()
            )
        }

        // Simple Presenter Factory
        factory { AuthTokenViewModel(get(), get()) }
    }
}