package com.maadiran.myvision.di

import android.content.Context
import com.maadiran.myvision.core.utils.GoogleSpeechRecognizer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilsModule {
    @Provides
    @Singleton
    fun provideGoogleSpeechRecognizer(
        @ApplicationContext context: Context
    ): GoogleSpeechRecognizer = GoogleSpeechRecognizer(context)
}