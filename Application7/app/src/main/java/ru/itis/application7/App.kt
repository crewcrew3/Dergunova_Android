package ru.itis.application7

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import ru.itis.application7.core.utils.AppLifecycleTracker

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppLifecycleTracker.init(this)
    }
}