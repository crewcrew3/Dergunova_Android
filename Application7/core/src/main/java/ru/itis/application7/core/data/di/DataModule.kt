package ru.itis.application7.core.data.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.itis.application7.core.BuildConfig
import ru.itis.application7.core.data.local.WeatherAppDatabase
import ru.itis.application7.core.data.local.dao.UserDao
import ru.itis.application7.core.utils.OtherProperties
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(application: Application): SharedPreferences {
        return application.getSharedPreferences(OtherProperties.SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): WeatherAppDatabase {
        return Room.databaseBuilder(
            context,
            WeatherAppDatabase::class.java,
            BuildConfig.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: WeatherAppDatabase): UserDao {
        return database.userDao
    }
}