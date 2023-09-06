package com.tku.usrcare.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.addCallback
import com.tku.usrcare.databinding.ActivityMainBinding
import com.tku.usrcare.repository.SessionManager


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        intent = Intent(this, LoginActivity::class.java)
        checkLogin(intent)
        createNotificationChannel()
    }
    override fun onStart() {
        super.onStart()

        this.onBackPressedDispatcher.addCallback(this) {
            val alertDialog = androidx.appcompat.app.AlertDialog.Builder(this@MainActivity)
            alertDialog.setTitle("Exit")
            alertDialog.setMessage("確定要離開APP嗎")
            alertDialog.setPositiveButton("是的") { _, _ ->
                finishAffinity()
            }
            alertDialog.setNegativeButton("取消") { _, _ ->
            }
            alertDialog.show()
        }
    }
    private fun checkLogin(intent: Intent){
        val sessionManager = SessionManager(this)
        val userToken = sessionManager.getUserToken()
        if(userToken == null){
            intent.setClass(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Alarm Channel"
            val descriptionText = "Channel for Alarm notification"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("alarm_channel_id", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}