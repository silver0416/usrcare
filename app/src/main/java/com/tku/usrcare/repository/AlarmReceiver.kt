package com.tku.usrcare.repository

import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.media.MediaPlayer
import android.os.VibratorManager
import androidx.annotation.RequiresApi
import com.tku.usrcare.view.AlarmActivity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.tku.usrcare.R
import java.util.Calendar

class AlarmService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Create an explicit intent for an Activity in your app
        val activityIntent = Intent(this, AlarmActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        // Define a NotificationChannel with the desired initial settings
        val channelId = "alarm_channel_id"
        val channelName = "Alarm Channel"
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH).apply {
            description = "Alarm notifications"
            enableLights(true)
            lightColor = R.color.red
            enableVibration(true)
        }

        // Retrieve a NotificationManager and create the Notification channel
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        // Create a Notification and set its content and the PendingIntent
        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Alarm!")
            .setContentText("Your alarm is going off!")
            .setSmallIcon(R.drawable.ic_clocknotice) // Replace with your app's drawable resource
            .setContentIntent(pendingIntent)
            .build()

        // Start the foreground service with the notification
        startForeground(1, notification)

        return START_REDELIVER_INTENT
    }

}

class AlarmReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onReceive(context: Context, intent: Intent) {
        val week = intent.getBooleanArrayExtra("week")?.toMutableList() ?: MutableList(7) { false }
        val title = intent.getStringExtra("title")

        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1  // Sunday is 1 in Calendar class

        if (week[dayOfWeek]) {
            // Trigger the alarm
            // 啟動指定頁面
            val activityIntent = Intent(context, AlarmActivity::class.java)
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(activityIntent)

            // 播放鈴聲
            val ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            val mediaPlayer = MediaPlayer.create(context, ringtone)
            mediaPlayer.start()

            //震動
            val vibrator: Vibrator

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                // 對於 API 31 及以上,使用 VibratorManager
                val vibratorManager =
                    context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibrator = vibratorManager.defaultVibrator
            } else {
                // 對於 API 30 及以下,使用 Vibrator
                vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }
            val pattern = longArrayOf(0, 1000, 1000, 1000)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // API 26 及以上使用 VibrationEffect
                vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                // API 25 及以下使用
                @Suppress("DEPRECATION")
                vibrator.vibrate(pattern, -1)
            }
        }
    }

}
