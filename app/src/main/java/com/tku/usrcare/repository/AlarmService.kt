package com.tku.usrcare.repository

import android.Manifest
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.tku.usrcare.R
import com.tku.usrcare.view.AlarmActivity

class AlarmService : Service() {
    private lateinit var vibrator: Vibrator
    private val pattern = longArrayOf(0, 1000, 1000)  // Vibrate for 1 second, pause for 1 second

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        // Start vibrating
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, 0))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(pattern, 0)
        }

        val channelId = getString(R.string.clock_reminder)

        val notificationId = 1
        val fullScreenIntent = Intent(this, AlarmActivity::class.java).apply {
            putExtra("title", intent?.getStringExtra("title"))
            putExtra("detail", intent?.getStringExtra("detail"))
        }
        val flags =
            PendingIntent.FLAG_UPDATE_CURRENT or if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.FLAG_IMMUTABLE
            } else {
                0
            }
        val fullScreenPendingIntent: PendingIntent = PendingIntent.getActivity(
            this, 0, fullScreenIntent, flags
        )
        val alarmServiceCloserIntent = Intent(this, AlarmServiceCloser::class.java)
        val alarmServiceCloserPendingIntent: PendingIntent = PendingIntent.getBroadcast(
            this, 0, alarmServiceCloserIntent, PendingIntent.FLAG_IMMUTABLE
        )
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_clocknotice)
            .setContentTitle("${intent?.getStringExtra("title")}!")
            .setContentText("${intent?.getStringExtra("detail")}")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(true)
            .setOnlyAlertOnce(false)
            .setFullScreenIntent(
                fullScreenPendingIntent,
                true
            )  // This line sets the full-screen intent
            .addAction(
                R.drawable.ic_clocknotice,
                "關閉鬧鐘",
                alarmServiceCloserPendingIntent
            )


        val notificationManager = NotificationManagerCompat.from(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return START_STICKY
        }
        notificationManager.notify(notificationId, notificationBuilder.build())
        startForeground(notificationId, notificationBuilder.build())

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        vibrator.cancel()  // Stop vibrating
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}
