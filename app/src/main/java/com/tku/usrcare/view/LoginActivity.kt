package com.tku.usrcare.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tku.usrcare.databinding.ActivityLoginBinding
import com.tku.usrcare.view.ui.login.LoginStartFragment


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}