package ru.itis.application7.core.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import ru.itis.application7.core.utils.QueryParamNames
import ru.itis.application7.core.utils.QueryParamValues
import javax.inject.Inject

class UnitsInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url.newBuilder()
            .addQueryParameter(QueryParamNames.UNITS, QueryParamValues.UNITS_VALUE_METRIC)

        val request = chain.request().newBuilder().url(url.build())

        return chain.proceed(request.build())
    }
}