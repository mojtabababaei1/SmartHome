package com.maadiran.myvision.data.di

import com.maadiran.myvision.data.local.PreferencesManager
import com.maadiran.myvision.data.repository.IoTRepository
import com.maadiran.myvision.domain.repository.IoTRepositoryInterface
import com.maadiran.myvision.domain.repository.PreferencesManagerInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindIoTRepository(
        ioTRepository: IoTRepository
    ): IoTRepositoryInterface

    @Binds
    @Singleton
    abstract fun bindPreferencesManager(
        preferencesManager: PreferencesManager
    ): PreferencesManagerInterface
}
