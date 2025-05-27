package com.maadiran.myvision

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyVision : Application() {
    companion object {
        init {
            // Force JNA to use bundled libraries
            System.setProperty("jna.nosys", "true")
            // Set library path to app's native library directory
            System.setProperty("jna.boot.library.name", "jnidispatch")
        }
    }

    override fun onCreate() {
        super.onCreate()
        // Set the library path after context is available
        System.setProperty("jna.boot.library.path", applicationInfo.nativeLibraryDir)

    }
}
