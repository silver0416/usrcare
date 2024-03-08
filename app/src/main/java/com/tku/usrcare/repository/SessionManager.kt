package com.tku.usrcare.repository

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tku.usrcare.model.AlarmItem
import com.tku.usrcare.model.BroadcastData
import com.tku.usrcare.model.HistoryStoryResponse
import com.tku.usrcare.model.MoodPuncherSave
import com.tku.usrcare.model.OAuthCheckResponse
import com.tku.usrcare.model.VocabularyResponse

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
        val editor = prefs.edit()
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

    fun saveOAuthCheck(oAuthCheckResponse: OAuthCheckResponse) {
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(oAuthCheckResponse)
        editor.putString("oAuthCheck", json)
        editor.apply()
    }

    fun getOAuthCheck(): OAuthCheckResponse {
        val gson = Gson()
        val json = prefs.getString("oAuthCheck", "")
        val type = object : TypeToken<OAuthCheckResponse>() {}.type
        return gson.fromJson(json, type)
    }

    fun saveIsOAuthCheck(isOAuthCheck: Boolean) {
        val editor = prefs.edit()
        editor.putBoolean("isOAuthCheck", isOAuthCheck)
        editor.apply()
    }

    fun getIsOAuthCheck(): Boolean {
        return prefs.getBoolean("isOAuthCheck", false)
    }


    val defaultDrugList = mutableListOf(
        "降血壓",
        "降血糖",
        "降血脂",
        "鈣補充",
        "鐵補充",
        "維他命補充",
        "胃藥",
        "抗發炎",
        "助眠藥",
        "維生素"
    )

    val defaultActivityList = mutableListOf(
        "散步",
        "吃飯",
        "打掃",
        "倒垃圾",
        "聚會",
        "買東西",
        "打電話",
        "娛樂"
    )

    val defaultSleepList = mutableListOf(
        "睡覺",
        "午睡",
        "休息"
    )

    fun getDrugReminderPresetNameList(): MutableList<String> {
        val gson = Gson()
        //default value is ("降血壓" , "降血糖" , "降血脂" , "鈣補充" , "鐵補充" , "維他命補充" , "胃藥" ,"抗發炎" ,"助眠藥" , "維生素")
        val json = prefs.getString("drugReminderPresetNameList", "")
        val type = object : TypeToken<MutableList<String>>() {}.type
        return gson.fromJson(json, type) ?: defaultDrugList
    }

    fun addDrugReminderPresetName(name: String) {
        val editor = prefs.edit()
        val gson = Gson()
        val json = prefs.getString("drugReminderPresetNameList", "")
        val type = object : TypeToken<MutableList<String>>() {}.type
        val dataList: MutableList<String> = gson.fromJson(json, type) ?: defaultDrugList
        if (dataList.contains(name)) {
            return
        }
        dataList.add(name)
        val newJson = gson.toJson(dataList)
        editor.putString("drugReminderPresetNameList", newJson)
        editor.apply()
    }

    fun getActivityReminderPresetNameList(): MutableList<String> {
        val gson = Gson()
        val json = prefs.getString("activityReminderPresetNameList", "")
        val type = object : TypeToken<MutableList<String>>() {}.type
        return gson.fromJson(json, type) ?: defaultActivityList
    }

    fun addActivityReminderPresetName(name: String) {
        val editor = prefs.edit()
        val gson = Gson()
        val json = prefs.getString("activityReminderPresetNameList", "")
        val type = object : TypeToken<MutableList<String>>() {}.type
        val dataList: MutableList<String> = gson.fromJson(json, type) ?: defaultActivityList
        if (dataList.contains(name)) {
            return
        }
        dataList.add(name)
        val newJson = gson.toJson(dataList)
        editor.putString("activityReminderPresetNameList", newJson)
        editor.apply()
    }

    fun getSleepReminderPresetNameList(): MutableList<String> {
        val gson = Gson()
        //default value is ("睡覺" , "午睡" , "休息")
        val json = prefs.getString("sleepReminderPresetNameList", "")
        val type = object : TypeToken<MutableList<String>>() {}.type
        return gson.fromJson(json, type) ?: defaultSleepList
    }

    fun addSleepReminderPresetName(name: String) {
        val editor = prefs.edit()
        val gson = Gson()
        val json = prefs.getString("sleepReminderPresetNameList", "")
        val type = object : TypeToken<MutableList<String>>() {}.type
        val dataList: MutableList<String> = gson.fromJson(json, type) ?: defaultSleepList
        if (dataList.contains(name)) {
            return
        }
        dataList.add(name)
        val newJson = gson.toJson(dataList)
        editor.putString("sleepReminderPresetNameList", newJson)
        editor.apply()
    }

    fun addReminder(alarmItem: AlarmItem) {
        val editor = prefs.edit()
        val gson = Gson()
        val json = prefs.getString("reminderList", "")
        val type = object : TypeToken<MutableList<AlarmItem>>() {}.type
        val dataList: MutableList<AlarmItem> = gson.fromJson(json, type) ?: mutableListOf()
        dataList.add(alarmItem)
        val newJson = gson.toJson(dataList)
        editor.putString("reminderList", newJson)
        editor.apply()
    }

    fun getReminderList(): MutableList<AlarmItem> {
        val gson = Gson()
        val json = prefs.getString("reminderList", "")
        val type = object : TypeToken<MutableList<AlarmItem>>() {}.type
        return gson.fromJson(json, type) ?: mutableListOf()
    }

    fun removeReminderById(id: Int) {
        val editor = prefs.edit()
        val gson = Gson()
        val json = prefs.getString("reminderList", "")
        val type = object : TypeToken<MutableList<AlarmItem>>() {}.type
        val dataList: MutableList<AlarmItem> = gson.fromJson(json, type) ?: mutableListOf()
        // 直接使用 filterNot 函數來移除指定 ID 的提醒，這樣可以避免創建額外的 mutable list
        val updatedList = dataList.filterNot { it.requestId == id }
        // 將更新後的列表轉換為 JSON 字符串
        val newJson = gson.toJson(updatedList)
        // 更新 SharedPreferences 中的數據
        editor.putString("reminderList", newJson).apply()
    }

    fun updateReminderIsActiveById(id: Int, isActive: Boolean) {
        val editor = prefs.edit()
        val gson = Gson()
        val json = prefs.getString("reminderList", "")
        val type = object : TypeToken<MutableList<AlarmItem>>() {}.type
        val dataList: MutableList<AlarmItem> = gson.fromJson(json, type) ?: mutableListOf()

        val updatedList = dataList.map {
            if (it.requestId == id) {
                AlarmItem(
                    it.type,
                    it.description,
                    it.requestId,
                    it.hour,
                    it.minute,
                    it.weekdays,
                    isActive
                )
            } else {
                it
            }
        }
        val newJson = gson.toJson(updatedList)
        editor.putString("reminderList", newJson).apply()
    }


    fun updateReminderIdById(id: Int, newId: Int) {
        val editor = prefs.edit()
        val gson = Gson()
        val json = prefs.getString("reminderList", "")
        val type = object : TypeToken<MutableList<AlarmItem>>() {}.type
        val dataList: MutableList<AlarmItem> = gson.fromJson(json, type) ?: mutableListOf()
        val updatedList = dataList.map {
            if (it.requestId == id) {
                AlarmItem(
                    it.type,
                    it.description,
                    newId,
                    it.hour,
                    it.minute,
                    it.weekdays,
                    it.isActive
                )
            } else {
                it
            }
        }
        val newJson = gson.toJson(updatedList)
        editor.putString("reminderList", newJson).apply()
    }


    fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        prefs.registerOnSharedPreferenceChangeListener(listener)
    }

    fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        prefs.unregisterOnSharedPreferenceChangeListener(listener)
    }

    fun getIsAskOauthBinding(): Boolean {
        return prefs.getBoolean("isAskOauthBinding", true)
    }

    fun saveIsAskOauthBinding(isAskOauthBinding: Boolean) {
        val editor = prefs.edit()
        editor.putBoolean("isAskOauthBinding", isAskOauthBinding)
        editor.apply()
    }

    fun saveIsInTestCast(isInTestCast: Boolean) {
        val editor = prefs.edit()
        editor.putBoolean("isInTestCast", isInTestCast)
        editor.apply()
    }

    fun getIsInTestCast(): Boolean {
        return prefs.getBoolean("isInTestCast", false)
    }

    fun getBroadcastDataList(): MutableList<BroadcastData> {
        val gson = Gson()
        val json = prefs.getString("broadcastDataList", "")
        val type = object : TypeToken<MutableList<BroadcastData>>() {}.type
        return gson.fromJson(json, type) ?: mutableListOf()
    }

    fun addBroadcastData(broadcastData: BroadcastData) {
        val editor = prefs.edit()
        val gson = Gson()
        val json = prefs.getString("broadcastDataList", "")
        val type = object : TypeToken<MutableList<BroadcastData>>() {}.type
        val dataList: MutableList<BroadcastData> = gson.fromJson(json, type) ?: mutableListOf()
        dataList.add(broadcastData)
        val newJson = gson.toJson(dataList)
        editor.putString("broadcastDataList", newJson)
        editor.apply()
    }

    fun deleteBroadcastData(broadcastData: BroadcastData) {
        val editor = prefs.edit()
        val gson = Gson()
        val json = prefs.getString("broadcastDataList", "")
        val type = object : TypeToken<MutableList<BroadcastData>>() {}.type
        val dataList: MutableList<BroadcastData> = gson.fromJson(json, type) ?: mutableListOf()
        dataList.remove(broadcastData)
        val newJson = gson.toJson(dataList)
        editor.putString("broadcastDataList", newJson)
        editor.apply()
    }

    fun getIfMakeReview(): Boolean {
        return prefs.getBoolean("ifMakeReview", false)
    }

    fun saveIfMakeReview(ifMakeReview: Boolean) {
        val editor = prefs.edit()
        editor.putBoolean("ifMakeReview", ifMakeReview)
        editor.apply()
    }

}