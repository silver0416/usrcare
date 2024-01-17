package com.tku.usrcare.repository

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class CancelAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // 在這裡處理取消鬧鐘的事件
        val alarmServiceIntent = Intent(context, AlarmService::class.java)
        context.stopService(alarmServiceIntent)
    }
}
