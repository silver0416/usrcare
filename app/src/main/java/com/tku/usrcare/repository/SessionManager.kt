package com.tku.usrcare.repository

import android.content.Context

class SessionManager(context: Context) {
    private var prefs = context.getSharedPreferences("com.tku.usrcare", Context.MODE_PRIVATE)
    companion object {
        const val USER_TOKEN = "user_token"
        const val EMAIL = "email"
        const val USERNAME = "username"
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
    fun getEmail(): String? {
        return prefs.getString(EMAIL, null)
    }
    fun getUsername(): String? {
        return prefs.getString(USERNAME, null)
    }
}