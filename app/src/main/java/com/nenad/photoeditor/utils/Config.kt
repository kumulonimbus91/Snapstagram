package com.nenad.photoeditor.utils

import android.app.Application
import com.nenad.photoeditor.dependencyinjection.repositoryModule
import com.nenad.photoeditor.dependencyinjection.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
@Suppress("unused")
class Config: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@Config)
            modules(listOf(repositoryModule, viewModelModule))
        }
    }
}