package com.example.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MariaBackgroundAssistantService : Service() {

    companion object {
        const val CHANNEL_ID = "maria_assistant_channel"
        const val NOTIFICATION_ID = 1001

        const val ACTION_START = "com.example.service.ACTION_START"
        const val ACTION_STOP = "com.example.service.ACTION_STOP"
        const val ACTION_FIX_CODE = "com.example.service.ACTION_FIX_CODE"
        const val ACTION_SPEAK_STATUS = "com.example.service.ACTION_SPEAK_STATUS"

        private val _isRunning = MutableStateFlow(false)
        val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()

        // Callback listener for in-app trigger
        var onBackgroundCodeFixRequested: (() -> Unit)? = null
    }

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var voiceManager: MariaVoiceManager? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        voiceManager = MariaVoiceManager(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_STOP -> {
                stopBackgroundService()
                return START_NOT_STICKY
            }
            ACTION_FIX_CODE -> {
                handleFixCodeAction()
            }
            ACTION_SPEAK_STATUS -> {
                voiceManager?.speak("AK EXPLOITS Maria AI background assistant active and listening.")
            }
            else -> {
                startForegroundWithNotification("AK EXPLOITS Maria Active • Ready for Voice & Code Fix")
                _isRunning.value = true
                voiceManager?.speak("Maria AI background assistant activated by AK EXPLOITS.")
            }
        }
        return START_STICKY
    }

    private fun handleFixCodeAction() {
        serviceScope.launch {
            onBackgroundCodeFixRequested?.invoke()
            voiceManager?.speak("AK EXPLOITS: Analyzing code AST and fixing bugs in background.")
            updateNotification("Code Auto-Fix triggered by Maria Agent!")
        }
    }

    private fun startForegroundWithNotification(contentText: String) {
        val notification = buildNotification(contentText)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                startForeground(NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC)
            } else {
                startForeground(NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC)
            }
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }
    }

    private fun updateNotification(newText: String) {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, buildNotification(newText))
    }

    private fun buildNotification(contentText: String): Notification {
        val mainIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val mainPendingIntent = PendingIntent.getActivity(
            this, 0, mainIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Action: Fix Code
        val fixIntent = Intent(this, MariaBackgroundAssistantService::class.java).apply {
            action = ACTION_FIX_CODE
        }
        val fixPendingIntent = PendingIntent.getService(
            this, 1, fixIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Action: Stop
        val stopIntent = Intent(this, MariaBackgroundAssistantService::class.java).apply {
            action = ACTION_STOP
        }
        val stopPendingIntent = PendingIntent.getService(
            this, 2, stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("AK EXPLOITS • Maria AI Agent")
            .setContentText(contentText)
            .setSmallIcon(R.drawable.ic_ak_logo)
            .setContentIntent(mainPendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(R.drawable.ic_ak_logo, "⚡ Fix Code", fixPendingIntent)
            .addAction(R.drawable.ic_ak_logo, "🛑 Stop", stopPendingIntent)
            .build()
    }

    private fun stopBackgroundService() {
        _isRunning.value = false
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "AK EXPLOITS Maria Background Assistant",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Persistent background AI coding and voice assistant service"
                setShowBadge(false)
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        _isRunning.value = false
        voiceManager?.shutdown()
        serviceScope.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
