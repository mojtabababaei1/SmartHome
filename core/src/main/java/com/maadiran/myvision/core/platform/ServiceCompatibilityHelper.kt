package com.maadiran.myvision.core.platform

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

class ServiceCompatibilityHelper(private val context: Context) {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createOreoChannels()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createOreoChannels() {
        // Foreground service channel
        val foregroundChannel = NotificationChannel(
            FOREGROUND_CHANNEL_ID,
            "Refrigerator Monitor Service",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            setShowBadge(false)
            enableLights(false)
            enableVibration(false)
        }

        // Warning channel with medium importance
        val warningChannel = NotificationChannel(
            WARNING_CHANNEL_ID,
            "Refrigerator Warnings",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            enableLights(true)
            lightColor = Color.YELLOW
            enableVibration(true)
            setSound(
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build()
            )
        }

        // Critical channel with high importance
        val criticalChannel = NotificationChannel(
            CRITICAL_CHANNEL_ID,
            "Refrigerator Critical Alerts",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            enableLights(true)
            lightColor = Color.RED
            enableVibration(true)
            setSound(
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM),
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build()
            )
        }

        notificationManager.createNotificationChannels(listOf(
            foregroundChannel,
            warningChannel,
            criticalChannel
        ))
    }

    fun getNotificationBuilder(
        channelId: String,
        priority: Int = NotificationCompat.PRIORITY_DEFAULT
    ): NotificationCompat.Builder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder(context, channelId)
        } else {
            NotificationCompat.Builder(context).setPriority(priority)
        }
    }

    fun startForegroundService(serviceClass: Class<*>) {
        val intent = android.content.Intent(context, serviceClass)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }

    companion object {
        const val FOREGROUND_CHANNEL_ID = "refrigerator_monitor_foreground"
        const val WARNING_CHANNEL_ID = "refrigerator_monitor_warning"
        const val CRITICAL_CHANNEL_ID = "refrigerator_monitor_critical"

        // Get channel ID based on Android version
        fun getChannelId(type: NotificationType): String {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                when (type) {
                    NotificationType.FOREGROUND -> FOREGROUND_CHANNEL_ID
                    NotificationType.WARNING -> WARNING_CHANNEL_ID
                    NotificationType.CRITICAL -> CRITICAL_CHANNEL_ID
                }
            } else {
                "default"
            }
        }
    }

    enum class NotificationType {
        FOREGROUND,
        WARNING,
        CRITICAL
    }
}