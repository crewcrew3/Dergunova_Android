package ru.itis.application7.core.remoteconfig

import android.widget.Toast
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.ktx.remoteConfig
import kotlinx.coroutines.tasks.await
import ru.itis.application7.core.utils.OtherProperties

object RemoteConfigManager {

    private val remoteConfig = Firebase.remoteConfig

    suspend fun fetchAndActivate(): Boolean {


        return try {
            remoteConfig.fetchAndActivate().await()
        } catch (e: Exception) {
            false
        }
    }

    fun isDetailScreenEnabled(): Boolean {
        return remoteConfig.getBoolean(OtherProperties.DETAIL_SCREEN_ENABLED)
    }
}