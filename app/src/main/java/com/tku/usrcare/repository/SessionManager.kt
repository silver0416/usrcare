package com.tku.usrcare.repository

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tku.usrcare.model.ClockData
import com.tku.usrcare.model.HistoryStoryResponse
import com.tku.usrcare.model.MoodPuncherSave
import com.tku.usrcare.model.VocabularyResponse
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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

    fun saveUserAccountNameList(accountList: MutableList<String?>) {
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(accountList)
        editor.putString("accountNameList", json)
        editor.apply()
    }

    fun saveUserAccountTokenList(accountList: MutableList<String?>) {
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(accountList)
        editor.putString("accountTokenList", json)
        editor.apply()
    }

    fun clearUserAccountNameList() {
        val editor = prefs.edit()
        editor.remove("accountList")
        editor.apply()
    }

    fun clearUserAccountTokenList() {
        val editor = prefs.edit()
        editor.remove("accountTokenList")
        editor.apply()
    }

    fun getUserAccount(): String? {
        return prefs.getString("account", null)
    }

    fun getUserAccountNameList(): MutableList<String?> {
        val gson = Gson()
        val json = prefs.getString("accountNameList", "")
        val type = object : TypeToken<MutableList<String>>() {}.type
        return gson.fromJson(json, type) ?: mutableListOf()
    }

    fun getUserAccountTokenList(): MutableList<String> {
        val gson = Gson()
        val json = prefs.getString("accountTokenList", "")
        val type = object : TypeToken<MutableList<String>>() {}.type
        return gson.fromJson(json, type) ?: mutableListOf()
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

    fun saveUserName(name: String) {
        val editor = prefs.edit()
        editor.putString("name", name)
        editor.apply()
    }

    fun getUserName(): String? {
        return prefs.getString("name", null)
    }

    fun saveUserGender(gender: String) {
        val editor = prefs.edit()
        editor.putString("gender", gender)
        editor.apply()
    }

    fun getUserGender(): String? {
        return prefs.getString("gender", null)
    }

    fun saveUserBirthday(birthday: String) {
        val editor = prefs.edit()
        editor.putString("birthday", birthday)
        editor.apply()
    }

    fun getUserBirthday(): String? {
        return prefs.getString("birthday", null)
    }

    fun saveUserCity(city: String) {
        val editor = prefs.edit()
        editor.putString("city", city)
        editor.apply()
    }

    fun getUserCity(): String? {
        return prefs.getString("city", null)
    }

    fun saveUserDistrict(district: String) {
        val editor = prefs.edit()
        editor.putString("district", district)
        editor.apply()
    }

    fun getUserDistrict(): String? {
        return prefs.getString("district", null)
    }

    fun saveUserNeighbor(neighbor: String) {
        val editor = prefs.edit()
        editor.putString("neighbor", neighbor)
        editor.apply()
    }

    fun getUserNeighbor(): String? {
        return prefs.getString("neighbor", null)
    }

    fun saveUserAddress(address: String) {
        val editor = prefs.edit()
        editor.putString("address", address)
        editor.apply()
    }

    fun getUserAddress(): String? {
        return prefs.getString("address", null)
    }

    fun saveUserEName(eName: String) {
        val editor = prefs.edit()
        editor.putString("eName", eName)
        editor.apply()
    }

    fun getUserEName(): String? {
        return prefs.getString("eName", null)
    }

    fun saveUserEPhone(ePhone: String) {
        val editor = prefs.edit()
        editor.putString("ePhone", ePhone)
        editor.apply()
    }

    fun getUserEPhone(): String? {
        return prefs.getString("ePhone", null)
    }

    fun saveUserERelation(eRelation: String) {
        val editor = prefs.edit()
        editor.putString("eRelation", eRelation)
        editor.apply()
    }

    fun getUserERelation(): String? {
        return prefs.getString("eRelation", null)
    }

    fun clearAll(context: Context) {
        delAllClock(context)
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }


    fun saveClock(context: Context, dataList: MutableList<ClockData>) {
        val sharedPreferences = context.getSharedPreferences("clock", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        setAlarm(context, dataList)
        val gson = Gson()
        val json = gson.toJson(dataList)
        editor.putString("clock", json)
        editor.apply()
    }

    fun setAlarm(context: Context, dataList: MutableList<ClockData>) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        for (i in dataList.indices) {
            if (!dataList[i].switch) continue  // Skip if the switch is off
            Log.d("setAlarm", "setAlarm: ${dataList[i].week}")
            Log.d("setAlarm", "setAlarm: ${dataList[i].time}")
            val timeString = dataList[i].time
            val date = format.parse(timeString)
            val weekMapping = arrayOf(2, 3, 4, 5, 6, 7, 1)

            for (j in 0 until 7) {  // Iterate over every day of the week
                if (!dataList[i].week[j]) continue  // Skip if this day is not selected
                val calendar = Calendar.getInstance()
                // Set the day of week
                calendar.set(Calendar.DAY_OF_WEEK, weekMapping[j])
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
                    putExtra("id", dataList[i].id)
                    putExtra("week", dataList[i].week.toBooleanArray())
                    putExtra("title", dataList[i].title)
                    putExtra("detail", dataList[i].detail)
                }
                val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    PendingIntent.getBroadcast(
                        context,
                        dataList[i].id * (j + 1),
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                } else {
                    PendingIntent.getBroadcast(
                        context,
                        dataList[i].id * (j + 1),
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
                Log.d(
                    "setAlarm",
                    "Alarm set for: ${formatter.format(calendar.time)}\t${dataList[i].id * (j + 1)}"
                )
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
                id = dataList[position].id,
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

    fun removeClock(context: Context, alarmId: Int) {
        val sharedPreferences = context.getSharedPreferences("clock", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("clock", "")
        val type = object : TypeToken<MutableList<ClockData>>() {}.type
        val dataList: MutableList<ClockData> = gson.fromJson(json, type) ?: mutableListOf()

        for (i in dataList.indices) {
            if (dataList[i].id == alarmId) {
                dataList.removeAt(i)
                break
            }
        }
        val newJson = gson.toJson(dataList)
        val editor = sharedPreferences.edit()
        editor.putString("clock", newJson)
        editor.apply()

        for (i in 1 until 8) {
            // Cancel the alarm
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, AlarmReceiver::class.java)
            val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.getBroadcast(
                    context,
                    alarmId * i,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            } else {
                PendingIntent.getBroadcast(
                    context,
                    alarmId * i,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            }
            Log.d("removeClock", "removeClock: ${alarmId * i}")
            alarmManager.cancel(pendingIntent)
        }
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
        val clockData = getClock(context)
        for (i in clockData.indices) {
            for (j in 1 until 8) {
                // Cancel the alarm
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(context, AlarmReceiver::class.java)
                val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    PendingIntent.getBroadcast(
                        context,
                        clockData[i].id * j,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                } else {
                    PendingIntent.getBroadcast(
                        context,
                        clockData[i].id * j,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                }
                Log.d("delAllClock", "delAllClock: ${clockData[i].id * j}")
                alarmManager.cancel(pendingIntent)
            }
        }
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

    fun saveNowMainColor(color: String) {
        val editor = prefs.edit()
        editor.putString("nowMainColor", color)
        editor.apply()
    }

    fun getNowMainColor(): String? {
        return prefs.getString("nowMainColor", "#FF56B1")
    }

    fun addSignedDateTime(dateTime: String, mood: Int) {
        //save dateTime and mood
        val editor = prefs.edit()
        val gson = Gson()
        val json = prefs.getString("signedDateTime", "")
        val type = object : TypeToken<MutableList<String>>() {}.type
        val dataList: MutableList<String> = gson.fromJson(json, type) ?: mutableListOf()
        val data: MutableList<String> = mutableListOf()
        data.add(dateTime)
        data.add(mood.toString())
        dataList.add(gson.toJson(data))
        val newJson = gson.toJson(dataList)
        editor.putString("signedDateTime", newJson)
        editor.apply()
    }

    fun getSignedDateTime(): MutableList<MutableList<String>> {
        val gson = Gson()
        val json = prefs.getString("signedDateTime", "")
        val type = object : TypeToken<MutableList<String>>() {}.type
        val dataList: MutableList<String> = gson.fromJson(json, type) ?: mutableListOf()
        val result: MutableList<MutableList<String>> = mutableListOf()
        for (i in dataList.indices) {
            val data = gson.fromJson<MutableList<String>>(dataList[i], type)
            result.add(data)
        }
        return result
    }

    fun saveCheatAccess(cheatAccess: Boolean) {
        val editor = prefs.edit()
        editor.putBoolean("cheatAccess", cheatAccess)
        editor.apply()
    }

    fun getCheatAccess(): Boolean {
        return prefs.getBoolean("cheatAccess", false)
    }

    fun saveHistoryStory(historyStory: HistoryStoryResponse) {
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(historyStory)
        editor.putString("historyStory", json)
        editor.apply()
    }

    fun getHistoryStory(): HistoryStoryResponse? {
        val gson = Gson()
        val json = prefs.getString("historyStory", "")
        val type = object : TypeToken<HistoryStoryResponse>() {}.type
        return gson.fromJson(json, type)
    }

    fun saveVocabulary(vocabularyResponse: VocabularyResponse) {
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(vocabularyResponse)
        editor.putString("vocabulary", json)
        editor.apply()
    }

    fun getVocabulary(): VocabularyResponse? {
        val gson = Gson()
        val json = prefs.getString("vocabulary", "")
        val type = object : TypeToken<VocabularyResponse>() {}.type
        return gson.fromJson(json, type)
    }

    fun savePoints(points: Int) {
        val editor = prefs.edit()
        editor.putInt("points", points)
        editor.apply()
    }

    fun getPoints(): Int {
        return prefs.getInt("points", 0)
    }

    fun saveApproveAiMoodPuncher(approveAiMoodPuncher: Boolean) {
        val editor = prefs.edit()
        editor.putBoolean("approveAiMoodPuncher", approveAiMoodPuncher)
        editor.apply()
    }

    fun getApproveAiMoodPuncher(): Boolean {
        return prefs.getBoolean("approveAiMoodPuncher", false)
    }

    fun addMoodPuncher(moodPuncherSave: MoodPuncherSave) {
        val editor = prefs.edit()
        val gson = Gson()
        val json = prefs.getString("moodPuncher", "")
        val type = object : TypeToken<ArrayList<MoodPuncherSave>>() {}.type
        val dataList: ArrayList<MoodPuncherSave> = gson.fromJson(json, type) ?: arrayListOf()
        dataList.add(moodPuncherSave)
        val newJson = gson.toJson(dataList)
        editor.putString("moodPuncher", newJson)
        editor.apply()
    }

    fun getMoodPuncher(): ArrayList<MoodPuncherSave>? {
        val gson = Gson()
        val json = prefs.getString("moodPuncher", "")
        val type = object : TypeToken<ArrayList<MoodPuncherSave>>() {}.type
        return gson.fromJson(json, type)
    }


}