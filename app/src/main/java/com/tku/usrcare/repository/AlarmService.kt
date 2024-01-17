package com.tku.usrcare.repository

import android.Manifest
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.tku.usrcare.R

class AlarmService : Service() {

    private lateinit var alarmSound: Uri
    private lateinit var ringtone: Ringtone
    private lateinit var vibrator: Vibrator

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        // 從 Intent 中獲取鬧鐘 ID
        val alarmId = intent.getIntExtra("alarm_id", -1)
        val isRepeat = intent.getBooleanExtra("isRepeat", false)

        val sessionManager = SessionManager(this)
        sessionManager.updateReminderIsActiveById(alarmId, isRepeat)

        alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtone = RingtoneManager.getRingtone(this, alarmSound)
        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        // 在這裡處理鬧鐘觸發的事件
        startForeground(alarmId, showNotification(this, intent))
        playAlarmSound()
        playVibrate()


        return START_STICKY
    }


    private fun showNotification(context: Context, intent: Intent): Notification {
        val notificationManager = NotificationManagerCompat.from(context)

        val alarmId = intent.getIntExtra("alarm_id", -1)
        val name = intent.getStringExtra("name")
        val description = intent.getStringExtra("description")


        // 創建用於取消鬧鐘的 PendingIntent
        val cancelIntent = Intent(
            context,
            CancelAlarmReceiver::class.java
        )
        val cancelPendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId,
            cancelIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 創建通知
        val notificationBuilder =
            NotificationCompat.Builder(
                context,
                ContextCompat.getString(context, R.string.clock_reminder)
            )
                .setContentTitle(name).setContentText(description)
                .setSmallIcon(R.drawable.ic_clocknotice) // 更換為您的圖示
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setAutoCancel(true)
                .setOnlyAlertOnce(false)
                .setOngoing(true)
                .setColorized(true)
                .setColor(ContextCompat.getColor(context, R.color.btnInClockColor))
                .addAction(R.drawable.ic_notification, "取消鬧鐘", cancelPendingIntent) // 添加取消鬧鐘的動作

        // 顯示通知
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return notificationBuilder.build()
        }

        notificationManager.notify(alarmId, notificationBuilder.build())
        return notificationBuilder.build()
    }

    private fun playAlarmSound() {
        // 這裡是播放鬧鈴聲音的代碼。您可以使用 RingtoneManager 或 MediaPlayer。
        // 例如，使用系統預設鬧鈴聲音：
        ringtone.play()
    }

    private fun playVibrate() {
        // 這裡控制震動
        // 震動1.5秒，停止0.5秒，無限循環
        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && vibrator.hasAmplitudeControl()
        ) {
            vibrator.vibrate(
                VibrationEffect.createWaveform(
                    longArrayOf(1500, 500),
                    intArrayOf(255, 0),
                    0
                )
            )
        } else {
            vibrator.vibrate(longArrayOf(1500, 500), 0)
        }
    }

    override fun onDestroy() {
        ringtone.stop()
        vibrator.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
