package com.tku.usrcare.repository

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.tku.usrcare.R
import com.tku.usrcare.model.BroadcastData
import com.tku.usrcare.view.MainActivity
import com.tku.usrcare.view.SportsActivity

class MyFirebaseMessagingService : FirebaseMessagingService() {


    // 當從 FCM 接收到一條消息時調用
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val data = BroadcastData(
            type = remoteMessage.data["type"].toString(),
            title = remoteMessage.data["title"].toString(),
            message = remoteMessage.data["message"].toString(),
            time = remoteMessage.sentTime.toString(),
            action = remoteMessage.data["action"].toString(),
            url=remoteMessage.data["url"].toString(),
            //intent=null,
        )
        when {
            remoteMessage.data["action"].toString()=="video"-> {
                sendNotification(data)
            }
            else -> {
                Log.d("fcm", "nothing to do")
            }
        }
        val sessionManager = SessionManager(applicationContext)
        sessionManager.addBroadcastData(data)
    }

    //系統通知
    private fun sendNotification(data:BroadcastData) {
        val intentMain = Intent(this, MainActivity::class.java).apply{flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT}
        val intentSports = Intent(this, SportsActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            putExtra("action", data.action)
            putExtra("url", data.url)
              // 將要導航的資訊傳入 Intent
        }
        val intents = arrayOf(intentMain, intentSports)
        //Log.d("fcm", "正在執行sendNotification")
        val pendingIntent = PendingIntent.getActivities(this, 0, intents, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val notificationBuilder = NotificationCompat.Builder(this, "broadcast")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(data.title)
            .setContentText(data.message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        // 處理新的 token
        Log.d("fcm", "Refreshed token: $token")
    }
}
