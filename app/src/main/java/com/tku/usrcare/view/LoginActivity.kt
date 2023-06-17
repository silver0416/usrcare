package com.tku.usrcare.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tku.usrcare.databinding.ActivityLoginBinding
import com.tku.usrcare.view.ui.login.LoginStartFragment
import android.content.Intent
import androidx.activity.addCallback


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.onBackPressedDispatcher.addCallback(this) {
            val alertDialog = androidx.appcompat.app.AlertDialog.Builder(this@LoginActivity)
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
}