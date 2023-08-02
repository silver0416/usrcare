package com.tku.usrcare.repository

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

class AlarmReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onReceive(context: Context, intent: Intent) {
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
