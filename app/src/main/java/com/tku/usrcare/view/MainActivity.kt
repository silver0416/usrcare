package com.tku.usrcare.view

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.addCallback
import com.tku.usrcare.databinding.ActivityMainBinding
import com.tku.usrcare.api.ApiUSR
import com.tku.usrcare.model.UserInfo
import com.tku.usrcare.repository.SessionManager


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        intent = Intent(this, LoginActivity::class.java)
        checkLogin(intent)
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
    fun checkLogin(intent: Intent){
        val sessionManager = SessionManager(this)
        val userToken = sessionManager.getUserToken()
        if(userToken == null){
            intent.setClass(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}