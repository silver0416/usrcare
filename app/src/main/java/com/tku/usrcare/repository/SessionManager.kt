package com.tku.usrcare.repository

import android.content.Context

class SessionManager(context: Context) {
    private var prefs = context.getSharedPreferences("com.tku.usrcare", Context.MODE_PRIVATE)
    companion object {
        const val USER_TOKEN = "user_token"
        const val EMAIL = "email"
        const val USERNAME = "username"
    }
}