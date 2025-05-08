package ru.itis.application7.core.domain.di.qualifiers

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME) //будет использоваться во время рантайма
annotation class IoDispatchers()