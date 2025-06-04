package com.maadiran.myvision

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
@HiltAndroidApp
class MyVision : Application() {

    companion object {
        init {
            System.setProperty("jna.nosys", "true")
            System.setProperty("jna.boot.library.name", "jnidispatch")
        }
    }

    override fun onCreate() {
        super.onCreate()
        // اینجا applicationInfo قابل دسترسی است
        System.setProperty("jna.boot.library.path", applicationInfo.nativeLibraryDir)
    }
}

