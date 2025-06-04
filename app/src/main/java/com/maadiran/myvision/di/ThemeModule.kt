package com.maadiran.myvision.di



import android.content.Context
import com.maadiran.myvision.data.theme.datastore.ThemePreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ThemeModule {

    @Provides
    @Singleton
    fun provideThemePreferenceManager(
        @ApplicationContext context: Context
    ): ThemePreferenceManager {
        return ThemePreferenceManager(context)
    }
}
