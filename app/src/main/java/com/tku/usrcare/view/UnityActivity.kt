package com.tku.usrcare.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.addCallback
import com.tku.usrcare.databinding.ActivityUnityBinding
import kotlin.system.exitProcess


class UnityActivity : AppCompatActivity() {
    private var binding: ActivityUnityBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUnityBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
    }

    override fun onStart() {
        super.onStart()

        this.onBackPressedDispatcher.addCallback(this) {
            val alertDialog = androidx.appcompat.app.AlertDialog.Builder(this@UnityActivity)
            alertDialog.setTitle("Exit")
            alertDialog.setMessage("確定要退出遊戲嗎")
            alertDialog.setPositiveButton("是的") { _, _ ->
                exitProcess(0)
            }
            alertDialog.setNegativeButton("取消") { _, _ ->
            }
            alertDialog.show()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}