package com.tku.usrcare.repository

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import com.google.gson.Gson
import com.tku.usrcare.model.ClockData
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Objects

class SessionManager(context: Context) {
    private var prefs = context.getSharedPreferences("com.tku.usrcare", Context.MODE_PRIVATE)
    companion object {
        const val USER_TOKEN = "user_token"
        const val USER_PHONE = "user_phone"
        const val PUBLIC_TOKEN = "public_token"
    }

    fun getPublicToken(): String? {
        return prefs.getString(PUBLIC_TOKEN, "PAt5WTlNqGgOMzsHAKegJ3QQWCwGBQfU3k8WRJODbo5TZQ32gaUxCVdaIeoVRLI0")
    }
    fun getUserToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }
    fun saveUserToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun  clearUserToken(){
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
    fun clearOTP(){
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
    fun clearUserAccount(){
        val editor = prefs.edit()
        editor.remove("account")
        editor.apply()
    }
    fun clearUserStatus(){
        val editor = prefs.edit()
        editor.remove("status")
        editor.apply()
    }
    fun clearUserPhone(){
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
    fun clearUserPassword(){
        val editor = prefs.edit()
        editor.remove("password")
        editor.apply()
    }

    fun clearAll(){
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }

    fun saveClock(context: Context, dataList: MutableList<ClockData>){
        val sharedPreferences = context.getSharedPreferences("clock", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(dataList)
        editor.putString("clock", json)
        editor.apply()
    }

    fun getClock(context: Context): MutableList<ClockData>{
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
    fun editClock(context: Context, dataList: MutableList<ClockData>, position: Int, newClockData: ClockData){
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

    fun delClock(context: Context, index: Int) {
        val clockDataList = getClock(context)
        if (index >= 0 && index < clockDataList.size) {
            clockDataList.removeAt(index)
        }
        saveClock(context, clockDataList)
    }



    fun saveTempWeek(context: Context, week: MutableList<Boolean>){
        val sharedPreferences = context.getSharedPreferences("tempWeek", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(week)
        editor.putString("tempWeek", json)
        editor.apply()
    }
    fun getTempWeek(context: Context): MutableList<Boolean>{
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

    fun saveTempName(context: Context, name: String){
        val sharedPreferences = context.getSharedPreferences("tempName", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("tempName", name)
        editor.apply()
    }

    fun getTempName(context: Context): String?{
        val sharedPreferences = context.getSharedPreferences("tempName", Context.MODE_PRIVATE)
        return sharedPreferences.getString("tempName", null)
    }

    fun saveTempDetail(context: Context, detail: String){
        val sharedPreferences = context.getSharedPreferences("tempDetail", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("tempDetail", detail)
        editor.apply()
    }
    fun getTempDetail(context: Context): String?{
        val sharedPreferences = context.getSharedPreferences("tempDetail", Context.MODE_PRIVATE)
        return sharedPreferences.getString("tempDetail", null)
    }
    fun saveTempTime(context: Context, time: MutableState<String>){
        val sharedPreferences = context.getSharedPreferences("tempTime", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("tempTime", time.toString())
        editor.apply()
    }
    fun getTempTime(context: Context): String?{
        val sharedPreferences = context.getSharedPreferences("tempTime", Context.MODE_PRIVATE)
        return sharedPreferences.getString("tempTime", null)
    }

    fun delAllClock(context: Context){
        val sharedPreferences = context.getSharedPreferences("clock", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }


}