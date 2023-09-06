package com.tku.usrcare.repository

import android.Manifest
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
import android.content.pm.PackageManager
import android.os.IBinder
import android.util.Log
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.tku.usrcare.R
import java.util.Calendar


class AlarmReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onReceive(context: Context, intent: Intent) {
        val week = intent.getBooleanArrayExtra("week")?.toMutableList() ?: MutableList(7) { false }
        val title = intent.getStringExtra("title")
        val detail = intent.getStringExtra("detail")

        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1  // Sunday is 1 in Calendar class

        if (week[dayOfWeek]){
            val serviceIntent = Intent(context, AlarmService::class.java,).apply {
                putExtra("title", title)
                putExtra("detail", detail)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent)
            } else {
                context.startService(serviceIntent)
            }
        }
    }

}
