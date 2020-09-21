package com.example.spotifywearapp

import android.app.Application
import com.example.spotifywearapp.repositories.ApiRepository
import com.example.spotifywearapp.repositories.ApiRepositoryImpl
import com.example.spotifywearapp.repositories.StorageRepository
import com.example.spotifywearapp.repositories.StorageRepositoryImpl
import com.example.spotifywearapp.viewmodels.FirstViewModel
import com.example.spotifywearapp.viewmodels.HomeViewModel
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
    }
}