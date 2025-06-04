package ru.itis.application7.core.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import ru.itis.application7.core.BuildConfig
import ru.itis.application7.core.utils.properties.QueryParamNames
import javax.inject.Inject

class ApiKeyInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url.newBuilder()
            .addQueryParameter(QueryParamNames.API_KEY, BuildConfig.openWeatherApiKey)

        val request = chain.request().newBuilder().url(url.build())

        return chain.proceed(request.build())
    }
}