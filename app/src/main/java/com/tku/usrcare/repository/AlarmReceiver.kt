package com.tku.usrcare.repository

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
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
