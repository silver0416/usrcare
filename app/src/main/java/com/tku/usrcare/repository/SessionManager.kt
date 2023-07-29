package com.tku.usrcare.repository

import android.content.Context

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






}