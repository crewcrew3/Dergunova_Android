package ru.itis.application6

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import ru.itis.application6.dependency_injection.ServiceLocator
import ru.itis.application6.utils.Properties

class App : Application() {

    private val serviceLocator = ServiceLocator
    //лучше сделать шеред преф после того как у аппликейшена сработает метод onCreate()
    private var sharedPref: SharedPreferences? = null

    override fun onCreate() {
        super.onCreate()
        serviceLocator.initDataLayerDependencies(ctx = this)
        sharedPref = this.getSharedPreferences(Properties.SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    fun getSharedPreferences(): SharedPreferences? {
        return sharedPref
    }
}