package com.tku.usrcare.repository

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.tku.usrcare.model.AlarmItem
import java.util.Calendar

class ReminderBuilder(private val context: Context) {    //請使用 applicationContext (應用程式等級)，以保持一致Context
    private var hour: Int = 0
    private var minute: Int = 0
    private val alarmManager: AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private var repeatDays: List<Int>? = null

    init {
        if (context != context.applicationContext) {
            throw IllegalArgumentException("context 必須是使用 application context!!")    // 檢查是否為 application context
        }
    }

    // 1.設定鬧鐘時間
    fun setTime(hr: Int, min: Int): ReminderBuilder {
        hour = hr
        minute = min
        return this
    }

    // 2.設定每週重複鬧鐘(單次鬧鐘不用設定)  格式為 [Calendar.MONDAY, Calendar.WEDNESDAY, Calendar.FRIDAY]
    fun setWeeklyAlarm(week: List<Int>?): ReminderBuilder {
        repeatDays = week
        return this
    }


    // 3.設定鬧鐘
    fun setAlarm(alarmItem: AlarmItem, withoutToday: Boolean = false): PendingIntent {

        val sessionManager = SessionManager(context)
        // 檢查鬧鐘清單的時間如果同一時刻有同個鬧鐘則先取消
        if (alarmItem.isActive) {
            for (i in sessionManager.getReminderList()) {
                if (i.hour == alarmItem.hour && i.minute == alarmItem.minute) {
                    cancelAlarm(i)
                    sessionManager.removeReminderById(i.requestId)
                }
            }
        } else {
            sessionManager.updateReminderIsActiveById(alarmItem.requestId, true)
        }
        val weekDaysArray = arrayListOf<Int>()
        for (i in alarmItem.weekdays) {
            weekDaysArray.add(i)
        }

        val alarmIntent = Intent(
            context,
            AlarmReceiver::class.java
        ).apply {
            putExtra("alarm_id", alarmItem.requestId)
            putExtra("name", alarmItem.type)
            putExtra("description", alarmItem.description)
            putExtra("hour", alarmItem.hour)
            putExtra("minute", alarmItem.minute)
            putIntegerArrayListExtra("weekDays", weekDaysArray )
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context, alarmItem.requestId, alarmIntent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )  // FLAG_IMMUTABLE 代表 PendingIntent 不可變更  FLAG_UPDATE_CURRENT 代表如果 PendingIntent 已經存在，則更新它 (不會重複建立)
        // FLAG_ONE_SHOT 代表 PendingIntent 只能使用一次，使用後自動取消 (不會重複建立)
        // FLAG_NO_CREATE 代表如果 PendingIntent 不存在，則返回 null

        val alarmTime = if (!withoutToday) {
            createAlarmTime()
        } else {
            createAlarmTimeWithoutToday()
        }  // 3.1.創建鬧鐘時間
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            alarmTime.time.timeInMillis,
            pendingIntent
        )

        Log.d("ReminderBuilder", "setAlarm: ${alarmItem.requestId}, ${alarmTime.time.timeInMillis}")

        return pendingIntent
    }


    // 3.1.創建鬧鐘時間(單次或每週重複)
    private fun createAlarmTime(): Alarm {
        return if (repeatDays.isNullOrEmpty()) {
            createSingleAlarm()   // 3.1(1) 創建一次性鬧鐘，距離指定時間最近
        } else {
            createWeeklyAlarm(repeatDays!!)   // 3.1(2) 創建每週重複鬧鐘
        }
    }

    private fun createAlarmTimeWithoutToday(): Alarm {
        if (repeatDays!!.size == 1) {
            // 3.2(1) 創建一次性鬧鐘，距離指定時間後一周
            val alarmTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                add(Calendar.DAY_OF_MONTH, 7)
            }
            return Alarm(alarmTime, false)
        } else {
            // 3.2(2) 創建這周除了今日以外的下一次鬧鐘
            val alarmTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                // 第一次迭代：找到最近日期
                while (!repeatDays!!.contains(get(Calendar.DAY_OF_WEEK))) {
                    add(Calendar.DAY_OF_MONTH, 1)
                }

                // 添加一天以開始尋找次之最近日期
                add(Calendar.DAY_OF_MONTH, 1)

                // 第二次迭代：找到次之最近日期
                while (!repeatDays!!.contains(get(Calendar.DAY_OF_WEEK))) {
                    add(Calendar.DAY_OF_MONTH, 1)
                }
            }
            return Alarm(alarmTime, true)
        }
    }


    private fun createSingleAlarm(): Alarm {
        // 3.2(1) 創建一次性鬧鐘，距離指定時間最近
        val alarmTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }
        return Alarm(alarmTime, false)
    }

    private fun createWeeklyAlarm(week: List<Int>): Alarm {
        // 3.2(2) 創建每週重複鬧鐘
        val alarmTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            while (!week.contains(get(Calendar.DAY_OF_WEEK))) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }
        return Alarm(alarmTime, true)  // 3.2.1 鬧鐘時間資料類別
    }

    fun cancelAlarm(alarmItem: AlarmItem) {
        // 創建一個與設置鬧鐘時相同的 PendingIntent
        val alarmIntent = Intent(
            context,
            AlarmReceiver::class.java
        ).apply {
            putExtra("alarm_id", alarmItem.requestId)
            putExtra("name", alarmItem.type)
            putExtra("description", alarmItem.description)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context, alarmItem.requestId, alarmIntent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            Log.d("ReminderBuilder", "cancelAlarm: ${alarmItem.requestId}")
        }

    }

    fun updateWeeklyAlarm(alarmItem: AlarmItem, newWeekDays: List<Int>) {
        // 首先取消現有的鬧鐘
        cancelAlarm(alarmItem)

        // 設置新的每週鬧鐘
        setWeeklyAlarm(newWeekDays)

        setTime(alarmItem.hour, alarmItem.minute)

        // 再次設置鬧鐘
        setAlarm(alarmItem)
    }

    fun updateAlarmTime(alarmItem: AlarmItem, newHour: Int, newMinute: Int) {
        // 取消現有的鬧鐘
        cancelAlarm(alarmItem)

        // 更新時間
        setTime(newHour, newMinute)

        setTime(alarmItem.hour, alarmItem.minute)

        // 重新設置鬧鐘
        setAlarm(alarmItem)
    }

    fun nextDayAlarm(alarmItem: AlarmItem) {

        // 更新時間
        setTime(alarmItem.hour, alarmItem.minute)

        setWeeklyAlarm(alarmItem.weekdays)

        // 重新設置鬧鐘
        setAlarm(alarmItem , true)
    }


}

data class Alarm(val time: Calendar, val isRepeat: Boolean)    // 3.2.1 鬧鐘時間資料類別
