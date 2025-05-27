package com.maadiran.myvision.data.network

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DynamicBaseUrlInterceptor @Inject constructor() : Interceptor {

    // مقدار پیش‌فرض برای زمانی که هنوز updateBaseUrl صدا زده نشده است
    @Volatile
    private var baseUrl: String = "refrigerator.local"

    fun updateBaseUrl(newBaseUrl: String) {
        baseUrl = newBaseUrl
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalUrl = original.url

        // فقط host را با baseUrl جایگزین می‌کنیم (برای mDNS یا IP)
        val newUrl = originalUrl.newBuilder()
            .host(baseUrl)
            .build()

        val newRequest = original.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}

