package com.maadiran.myvision.data.di

import android.content.Context
import com.maadiran.myvision.data.devices.tv.AndroidRemoteFactoryImpl
import com.maadiran.myvision.data.devices.tv.RemoteManagerImpl
import com.maadiran.myvision.data.services.VoiceServiceManager
import com.maadiran.myvision.domain.services.IVoiceServiceManager
import com.maadiran.myvision.domain.tv.AndroidRemoteFactory
import com.maadiran.myvision.domain.tv.IRemoteManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServicesModule {
    @Provides
    @Singleton
    fun provideVoiceServiceManager(
        @ApplicationContext context: Context
    ): IVoiceServiceManager {
        return VoiceServiceManager(context)
    }

    @Provides
    @Singleton
    fun provideAndroidRemoteFactory(
        androidRemoteFactoryImpl: AndroidRemoteFactoryImpl
    ): AndroidRemoteFactory = androidRemoteFactoryImpl

    @Provides
    @Singleton
    fun provideRemoteManager(
        remoteManagerImpl: RemoteManagerImpl
    ): IRemoteManager = remoteManagerImpl
}
