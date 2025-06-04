package ru.itis.application7.core.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle

object AppLifecycleTracker {

    private var foregroundActivity = false
    private var isChangingConfigurations = false

    fun init(application: Application) {
        application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

            override fun onActivityStarted(activity: Activity) {
                if (!isChangingConfigurations) {
                    foregroundActivity = true //в рамках моего приложения где только одна активити булеана думаю будет достаточно
                }
            }

            override fun onActivityResumed(activity: Activity) {}

            override fun onActivityPaused(activity: Activity) {}

            override fun onActivityStopped(activity: Activity) {
                isChangingConfigurations = activity.isChangingConfigurations
                if (!isChangingConfigurations) {
                    foregroundActivity = false
                }
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

            override fun onActivityDestroyed(activity: Activity) {}
        })
    }

    fun isAppInForeground(): Boolean = foregroundActivity
}