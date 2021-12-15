package com.example.macetapp40

import android.app.Application
import com.example.macetapp40.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin

class MacetaApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MacetaApplication)
            loadKoinModules(
                    listOf(
                            viewModelModule
                          )
                           )
        }
    }
}