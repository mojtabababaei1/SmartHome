package com.maadiran.myvision.data.di

import com.maadiran.myvision.data.network.DynamicBaseUrlInterceptor
import com.maadiran.myvision.data.network.IoTApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideDynamicBaseUrlInterceptor(): DynamicBaseUrlInterceptor {
        return DynamicBaseUrlInterceptor()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        dynamicBaseUrlInterceptor: DynamicBaseUrlInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(dynamicBaseUrlInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.4.1") // base اولیه
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideIoTApiService(retrofit: Retrofit): IoTApiService {
        return retrofit.create(IoTApiService::class.java)
    }
}
