package ru.itis.application7.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.itis.application7.core.data.repository.CurrentWeatherRepositoryImpl
import ru.itis.application7.core.data.repository.UserRepositoryImpl
import ru.itis.application7.core.domain.repository.CurrentWeatherRepository
import ru.itis.application7.core.domain.repository.UserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataBinderModule {

    @Binds
    @Singleton
    fun bindCurrentWeatherRepositoryToImplementation(impl: CurrentWeatherRepositoryImpl): CurrentWeatherRepository

    @Binds
    @Singleton
    fun bindUserRepositoryToImplementation(impl: UserRepositoryImpl): UserRepository
}