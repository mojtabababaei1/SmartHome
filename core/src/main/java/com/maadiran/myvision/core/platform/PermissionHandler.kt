package com.maadiran.myvision.core.platform

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionHandler(private val activity: Activity) {
    companion object {
        private const val PERMISSION_REQUEST_CODE = 123
        
        // Permissions that need runtime checking on Android 7+
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        
        // Permissions that need special handling for Android 13+ (API 33+)
        private val POST_NOTIFICATIONS_PERMISSION = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.POST_NOTIFICATIONS
        } else {
            null
        }
        
        // Permissions that need special handling for Android 14+ (API 34+)
        private val FOREGROUND_SERVICE_PERMISSIONS = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            arrayOf(
                Manifest.permission.FOREGROUND_SERVICE_DATA_SYNC,
                Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK
            )
        } else {
            null
        }
    }

    fun checkAndRequestPermissions(onPermissionsGranted: () -> Unit) {
        val permissionsToRequest = mutableListOf<String>()

        // Check basic permissions
        for (permission in REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(activity, permission) 
                != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission)
            }
        }

        // Check POST_NOTIFICATIONS permission for Android 13+
        POST_NOTIFICATIONS_PERMISSION?.let { permission ->
            if (ContextCompat.checkSelfPermission(activity, permission) 
                != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission)
            }
        }

        // Check FOREGROUND_SERVICE permissions for Android 14+
        FOREGROUND_SERVICE_PERMISSIONS?.forEach { permission ->
            if (ContextCompat.checkSelfPermission(activity, permission) 
                != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission)
            }
        }

        if (permissionsToRequest.isEmpty()) {
            onPermissionsGranted()
        } else {
            ActivityCompat.requestPermissions(
                activity,
                permissionsToRequest.toTypedArray(),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    fun handlePermissionResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
        onPermissionsGranted: () -> Unit
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                onPermissionsGranted()
            }
        }
    }
}
