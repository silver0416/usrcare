package com.tku.usrcare.repository

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.tku.usrcare.model.BroadcastData

class MyFirebaseMessagingService : FirebaseMessagingService() {


    // 當從 FCM 接收到一條消息時調用
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        if (remoteMessage.notification == null) {
            // 處理接收到的消息
            val data = BroadcastData(
                type = remoteMessage.data["type"].toString(),
                title = remoteMessage.data["title"].toString(),
                message = remoteMessage.data["message"].toString(),
                time = remoteMessage.sentTime.toString(),
                action = remoteMessage.data["action"].toString()
            )
            val sessionManager = SessionManager(applicationContext)
            sessionManager.addBroadcastData(data)
        }
    }

    override fun onNewToken(token: String) {
        // 處理新的 token
        Log.d("fcm", "Refreshed token: $token")
    }
}
