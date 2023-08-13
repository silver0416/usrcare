package com.tku.usrcare.repository

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tku.usrcare.model.ClockData
import kotlinx.coroutines.flow.MutableStateFlow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Objects

class SessionManager(context: Context) {
    private var prefs = context.getSharedPreferences("com.tku.usrcare", Context.MODE_PRIVATE)

    companion object {
        const val USER_TOKEN = "user_token"
        const val USER_PHONE = "user_phone"
        const val PUBLIC_TOKEN = "public_token"
    }

    fun getPublicToken(): String? {
        return prefs.getString(
            PUBLIC_TOKEN,
            "PAt5WTlNqGgOMzsHAKegJ3QQWCwGBQfU3k8WRJODbo5TZQ32gaUxCVdaIeoVRLI0"
        )
    }

    fun getUserToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun saveUserToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun clearUserToken() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }

    fun getUserPhone(): String? {
        return prefs.getString(USER_PHONE, null)
    }

    fun saveUserPhone(phone: String) {
        val editor = prefs.edit()
        editor.putString(USER_PHONE, phone)
        editor.apply()
    }

    fun saveOTP(otp: String) {
        val editor = prefs.edit()
        editor.putString("otp", otp)
        editor.apply()
    }

    fun getOTP(): String? {
        return prefs.getString("otp", null)
    }

    fun clearOTP() {
        val editor = prefs.edit()
        editor.remove("otp")
        editor.apply()
    }

    fun saveUserStatus(status: String) {
        val editor = prefs.edit()
        editor.putString("status", status)
        editor.apply()
    }

    fun getUserStatus(): String? {
        return prefs.getString("status", null)
    }

    fun saveUserAccount(account: String) {
        val editor = prefs.edit()
        editor.putString("account", account)
        editor.apply()
    }

    fun getUserAccount(): String? {
        return prefs.getString("account", null)
    }

    fun clearUserAccount() {
        val editor = prefs.edit()
        editor.remove("account")
        editor.apply()
    }

    fun clearUserStatus() {
        val editor = prefs.edit()
        editor.remove("status")
        editor.apply()
    }

    fun clearUserPhone() {
        val editor = prefs.edit()
        editor.remove(USER_PHONE)
        editor.apply()
    }

    fun saveUserPassword(password: String) {
        val editor = prefs.edit()
        editor.putString("password", password)
        editor.apply()
    }

    fun getUserPassword(): String? {
        return prefs.getString("password", null)
    }

    fun clearUserPassword() {
        val editor = prefs.edit()
        editor.remove("password")
        editor.apply()
    }

    fun clearAll() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun saveClock(context: Context, dataList: MutableList<ClockData>) {
        val sharedPreferences = context.getSharedPreferences("clock", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        setAlarm(context, dataList)
        val gson = Gson()
        val json = gson.toJson(dataList)
        editor.putString("clock", json)
        editor.apply()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun setAlarm(context: Context, dataList: MutableList<ClockData>) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())

        for (i in dataList.indices) {
            if (!dataList[i].switch) continue  // Skip if the switch is off
            Log.d("setAlarm", "setAlarm: ${dataList[i].week}")
            Log.d("setAlarm", "setAlarm: ${dataList[i].time}")
            val timeString = dataList[i].time
            val date = format.parse(timeString)

            for (j in 0 until 7) {  // Iterate over every day of the week
                if (!dataList[i].week[j]) continue  // Skip if this day is not selected

                val calendar = Calendar.getInstance()

                // Set the day of week
                calendar.set(Calendar.DAY_OF_WEEK, j + 1)

                // Set the time
                if (date != null) {
                    calendar.set(Calendar.HOUR_OF_DAY, date.hours)
                    calendar.set(Calendar.MINUTE, date.minutes)
                    calendar.set(Calendar.SECOND, 0)
                    calendar.set(Calendar.MILLISECOND, 0)
                }

                // If the set day is before the current time, skip to the next week
                val now = Calendar.getInstance()
                if (calendar.timeInMillis <= now.timeInMillis) {
                    calendar.add(Calendar.WEEK_OF_YEAR, 1)
                }

                val intent = Intent(context, AlarmReceiver::class.java).apply {
                    putExtra("week", dataList[i].week.toBooleanArray())
                    putExtra("title", dataList[i].title)
                }
                val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    PendingIntent.getBroadcast(
                        context,
                        i * 7 + j,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                } else {
                    PendingIntent.getBroadcast(
                        context,
                        i * 7 + j,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                }

                //check if > api 31
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (alarmManager.canScheduleExactAlarms()) {
                        alarmManager.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            calendar.timeInMillis,
                            pendingIntent
                        )
                    } else {
                        alarmManager.setExact(
                            AlarmManager.RTC_WAKEUP,
                            calendar.timeInMillis,
                            pendingIntent
                        )
                    }
                } else {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                }
                val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                Log.d("setAlarm", "Alarm set for: ${formatter.format(calendar.time)}")
            }
        }
    }


    fun getClock(context: Context): MutableList<ClockData> {
        val sharedPreferences = context.getSharedPreferences("clock", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("clock", null)
        return if (json != null) {
            val type = object : com.google.gson.reflect.TypeToken<List<ClockData>>() {}.type
            gson.fromJson(json, type)
        } else {
            mutableListOf()
        }
    }

    fun editClock(
        context: Context,
        dataList: MutableList<ClockData>,
        position: Int,
        newClockData: ClockData
    ) {
        // Replace the ClockData at the specified position
        Log.d("editClock", "editClock: $position")
        if (position in 0 until dataList.size) {
            dataList[position] = newClockData
        }

        val sharedPreferences = context.getSharedPreferences("clock", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(dataList)
        editor.putString("clock", json)
        editor.apply()
    }

    fun editClockSwitch(context: Context, position: Int, newSwitchState: Boolean) {
        val sharedPreferences = context.getSharedPreferences("clock", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("clock", "")
        val type = object : TypeToken<MutableList<ClockData>>() {}.type
        val dataList: MutableList<ClockData> = gson.fromJson(json, type) ?: mutableListOf()

        if (position in dataList.indices) {
            val newClockData = ClockData(
                title = dataList[position].title,
                detail = dataList[position].detail,
                time = dataList[position].time,
                week = dataList[position].week,
                switch = newSwitchState
            )
            dataList[position] = newClockData
        }

        val newJson = gson.toJson(dataList)
        val editor = sharedPreferences.edit()
        editor.putString("clock", newJson)
        editor.apply()
    }


    @RequiresApi(Build.VERSION_CODES.S)
    fun delClock(context: Context, index: Int) {
        val clockDataList = getClock(context)
        if (index >= 0 && index < clockDataList.size) {
            clockDataList.removeAt(index)
        }
        saveClock(context, clockDataList)
    }

    fun saveTempWeek(context: Context, week: MutableList<Boolean>) {
        val sharedPreferences = context.getSharedPreferences("tempWeek", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(week)
        editor.putString("tempWeek", json)
        editor.apply()
    }

    fun getTempWeek(context: Context): MutableList<Boolean> {
        val sharedPreferences = context.getSharedPreferences("tempWeek", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("tempWeek", null)
        return if (json != null) {
            val type = object : com.google.gson.reflect.TypeToken<List<Boolean>>() {}.type
            gson.fromJson(json, type)
        } else {
            mutableListOf()
        }
    }

    fun delAllClock(context: Context) {
        val sharedPreferences = context.getSharedPreferences("clock", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    fun saveUserEmail(email: String) {
        val editor = prefs.edit()
        editor.putString("userEmail", email)
        editor.apply()
    }

    fun getUserEmail(): String? {
        return prefs.getString("userEmail", null)
    }
}