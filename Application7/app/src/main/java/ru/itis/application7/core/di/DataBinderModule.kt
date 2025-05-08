package ru.itis.application7.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.itis.application7.core.data.repository.CurrentWeatherRepositoryImpl
import ru.itis.application7.core.domain.repository.CurrentWeatherRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataBinderModule {

    @Binds
    @Singleton
    fun bindCurrentWeatherRepositoryToImplementation(impl: CurrentWeatherRepositoryImpl): CurrentWeatherRepository
}