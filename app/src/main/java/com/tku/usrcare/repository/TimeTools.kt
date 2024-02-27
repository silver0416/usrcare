package com.tku.usrcare.repository

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

object TimeTools {

    fun howLongAgo(timestamp: Long): String {
        val date = Date(timestamp)
        return calculateTimeDifference(date)
    }

    private fun calculateTimeDifference(date: Date): String {
        val now = System.currentTimeMillis()
        val diff = now - date.time
        val second = diff / 1000
        val minute = second / 60
        val hour = minute / 60
        val day = hour / 24
        val month = day / 30
        val year = month / 12
        return when {
            year > 0 -> "$year 年前"
            month > 0 -> "$month 月前"
            day > 0 -> "$day 天前"
            hour > 0 -> "$hour 小時前"
            minute > 0 -> "$minute 分鐘前"
            else -> "$second 秒前"
        }
    }

    fun whatDate(timestamp: Long): String {
        if (timestamp == 0L) return ""
        if (timestamp < 0) return ""
        if (timestamp > System.currentTimeMillis()) return ""
        val formatter = DateTimeFormatter.ofPattern("yyyy 年 MM 月 dd 日").withZone(ZoneId.systemDefault())
        return formatter.format(Instant.ofEpochMilli(timestamp))
    }

    fun toIso8601(timestamp: Long): String {
        val formatter = DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.systemDefault())
        return formatter.format(Instant.ofEpochMilli(timestamp))
    }


}