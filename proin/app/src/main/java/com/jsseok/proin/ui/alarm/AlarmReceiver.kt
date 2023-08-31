package com.jsseok.proin.ui.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.jsseok.proin.R

class AlarmReceiver : BroadcastReceiver() {
    // 상수
    companion object {
        const val NOTIFICATION_CHANNEL_ID = "1000"
        const val NOTIFICATION_ID = 100
    }
    
    override fun onReceive(context: Context, intent: Intent) {
        createNotificationChannel(context)
        notifyNotification(context)
    }

    // 채널 생성
    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "PROIN ALARM",
                NotificationManager.IMPORTANCE_HIGH
            )
            NotificationManagerCompat.from(context)
                .createNotificationChannel(notificationChannel)
        }
    }

    // 알림 설정
    private fun notifyNotification(context: Context) {
        with(NotificationManagerCompat.from(context)) {
            val build = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setContentTitle("PROIN")
                .setContentText("LET'S SEE IT NEWS!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.smallicon_preview)

            notify(NOTIFICATION_ID, build.build())
        }
    }
}