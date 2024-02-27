package com.tku.usrcare.repository

import android.content.Context

object UniqueCode {
    private const val PREFS_NAME = "UniqueCodePrefs"
    private const val NEXT_CODE_KEY = "NextUniqueCode"

    // 初始化一個私有的、可變的靜態變數來追踪下一個可用的代碼。
    private var nextCode: Int = 1

    fun init(context: Context) {
        // 從SharedPreferences中獲取下一個代碼
        val sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        nextCode = sharedPrefs.getInt(NEXT_CODE_KEY, 1)  // 初始值為1
    }


    // 一個同步的方法來獲取下一個唯一的代碼。
    @Synchronized
    fun getNextCode(context: Context): Int {
        val newCode = nextCode++  // 先取得下一個代碼，再將nextCode加1

        // 更新SharedPreferences中的值
        val sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPrefs.edit()) {
            putInt(NEXT_CODE_KEY, nextCode)
            apply()
        }

        return newCode
    }
}
