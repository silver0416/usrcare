package com.tku.usrcare.repository

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.tku.usrcare.model.AlarmItem

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val weekDays = intent.getIntegerArrayListExtra("weekDays")
        var isRepeat = false

        if (weekDays?.size != 0) {   // 如果weekDays不為null，則為重複鬧鐘
            isRepeat = true
            val alarmItem = AlarmItem(
                intent.getStringExtra("name")!!,
                intent.getStringExtra("description")!!,
                intent.getIntExtra("alarm_id", -1),
                intent.getIntExtra("hour", -1),
                intent.getIntExtra("minute", -1),
                weekDays!!.toList(),
                intent.getBooleanExtra("isActive", false)
            )
            ReminderBuilder(context.applicationContext).nextDayAlarm(alarmItem)
        }

        val serviceIntent = Intent(context, AlarmService::class.java)
        serviceIntent.apply {
            putExtra("alarm_id", intent.getIntExtra("alarm_id", -1))
            putExtra("name", intent.getStringExtra("name"))
            putExtra("description", intent.getStringExtra("description"))
            putExtra("isRepeat", isRepeat)
        }
        context.startForegroundService(serviceIntent)
    }
}
