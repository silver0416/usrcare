package com.tku.usrcare.view.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tku.usrcare.api.ApiUSR
import com.tku.usrcare.databinding.FragmentLoginBinding
import com.tku.usrcare.model.Login
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.MainActivity
import java.security.MessageDigest


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        binding?.loginButton?.setOnClickListener() {
            if(validInput()){
                binding?.loading?.isVisible = true
                val username = binding?.accountEditText?.text.toString()
                val password = binding?.passwordEditText?.text.toString()
                this.activity?.let { it1 ->
                    ApiUSR.getSalt(it1, username, onSuccess = { it2 ->
                        val login = Login(username, hashPassword(password, it2))
                        this.activity?.let { it1 ->
                            ApiUSR.postLogin(it1, login, binding!!, onSuccess = {
                                val token = it.token
                                val name = it.name
                                SessionManager(requireContext()).saveUserToken(token)
                                SessionManager(requireContext()).saveUserName(name)
                                binding?.loading?.isVisible = false
                                val intent = Intent(activity, MainActivity::class.java)
                                startActivity(intent)
                                activity?.finish()
                            }, onError = {
                                binding?.accountEditText?.error = "帳號或密碼錯誤"
                                binding?.passwordEditText?.error = "帳號或密碼錯誤"
                                binding?.loading?.isVisible = false
                            })
                        }
                    })
                }
            }
        }
        binding?.btnBack?.setOnClickListener() {
            findNavController().navigateUp()
        }
    }

    private fun hashPassword(password: String, salt: String): String {
        // 將密碼與salt結合
        val combinedPassword = password + salt

        // 使用SHA-256進行雜湊
        val bytes = MessageDigest.getInstance("SHA-256").digest(combinedPassword.toByteArray())

        // 將雜湊後的bytes轉換為十六進制的字符串
        return bytes.joinToString("") { "%02x".format(it) }
    }

    private fun validInput(): Boolean {
        var pass = true
        val username = binding?.accountEditText?.text.toString()
        val password = binding?.passwordEditText?.text.toString()
        if (username.isEmpty()) {
            binding?.accountEditText?.error = "請輸入帳號"
            pass = false
        }
        if (password.isEmpty()) {
            binding?.passwordEditText?.error = "請輸入密碼"
            pass = false
        }
        return pass
    }
}

