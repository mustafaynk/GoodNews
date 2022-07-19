package com.ynk.goodnews

import android.app.Application
import android.content.Context
import com.ynk.goodnews.di.Component
import dagger.hilt.EntryPoints
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Application: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        appContext = applicationContext
    }

    val module: Component
        get() = EntryPoints.get(this, Component::class.java)


    companion object {
        @JvmStatic
        lateinit var instance: com.ynk.goodnews.Application
            private set

        @JvmStatic
        lateinit var appContext: Context
            private set

    }
}