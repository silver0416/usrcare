package com.tku.usrcare.api

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.lifecycle.viewmodel.savedstate.R
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import com.tku.usrcare.databinding.ActivityMainBinding
import com.tku.usrcare.databinding.FragmentLoginBinding
import com.tku.usrcare.databinding.FragmentLoginVerifyBinding
import com.tku.usrcare.model.Authentication
import com.tku.usrcare.model.Login
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.MainActivity
import com.tku.usrcare.view.ui.login.LoginFragment
import com.tku.usrcare.view.ui.login.LoginFragmentDirections
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body

class ApiUSR {
    companion object {
        private val apiClient: ApiService? = ApiClient.client?.create(ApiService::class.java)
        var handler: Handler = Handler(Looper.getMainLooper())

        fun getTest(activity: MainActivity, binding: ActivityMainBinding, token: String) {
            apiClient?.getTest(token = "Bearer $token")
                ?.enqueue {
                    onResponse = {
                        if (it.isSuccessful) {
                            handler.post {

                            }
                        } else {
                            handler.post {

                            }
                        }
                    }
                    onFailure = {
                        handler.post {
                            Log.e("onFailure", it!!.message.toString())

                        }
                    }
                }
        }

        fun postLogin(
            @Body login: Login,
            activity: Activity,
            binding: FragmentLoginBinding,
            navDirections: NavDirections,
            fragment: LoginFragment
        ) {
            apiClient?.postLogin(
                token = "Bearer ${SessionManager(activity).getPublicToken()}",
                login = login
            )
                ?.enqueue {
                    binding.loginButton.isEnabled = false
                    binding.loading.visibility = View.VISIBLE
                    val sessionManager = SessionManager(activity)
                    sessionManager.clearOTP()
                    onResponse = {
                        if (it.isSuccessful) {
                            handler.post {
                                val loginResponse = it.body()
                                if (loginResponse != null) {
                                    if (loginResponse.status == "new") {
                                        sessionManager.saveOTP(loginResponse.OTP)
                                        sessionManager.saveUserStatus(loginResponse.status)
                                    } else {
                                        sessionManager.saveUserStatus(loginResponse.status)
                                    }
                                    binding.loginButton.isEnabled = true
                                    binding.loading.visibility = View.GONE
                                    navDirections.let { it1 ->
                                        findNavController(fragment).navigate(it1)
                                    }
                                }
                            }
                        } else {
                            handler.post {
                                Log.e("onResponse", it.message().toString())
                                binding.loginButton.isEnabled = true
                                binding.loading.visibility = View.GONE
                                AlertDialog.Builder(activity)
                                    .setTitle("伺服器錯誤")
                                    // show okhttp error code
                                    .setMessage("請聯繫開發人員\ntkuusrcare@gmail.com")
                                    .setPositiveButton("確定") { _, _ -> }
                                    .setNegativeButton("檢視錯誤訊息") { _, _ ->
                                        AlertDialog.Builder(activity)
                                            .setTitle("錯誤代碼:${it.code()}")
                                            .setMessage(it.message().toString())
                                            .setPositiveButton("確定") { _, _ -> }
                                            .show()
                                    }
                                    .show()
                            }
                        }
                    }
                    onFailure = {
                        handler.post {
                            Log.e("onFailure", it!!.message.toString())
                            binding.loginButton.isEnabled = true
                            binding.loading.visibility = View.GONE
                            AlertDialog.Builder(activity)
                                .setTitle("網路錯誤")
                                .setMessage("請確認網路連線是否正常")
                                .setPositiveButton("確定") { _, _ -> }
                                .show()
                        }
                    }
                }
        }

        fun postAuthentication(
            @Body authentication: Authentication,
            activity: Activity,
            binding: FragmentLoginVerifyBinding,
        ) {
            apiClient?.postAuthentication(
                token = "Bearer ${SessionManager(activity).getPublicToken()}",
                authentication = authentication
            )
                ?.enqueue {
                    Log.d("postAuthentication", "postAuthentication")
                    binding.loading.visibility = View.VISIBLE
                    val sessionManager = SessionManager(activity)
                    onResponse = {
                        if (it.isSuccessful) {
                            handler.post {
                                val authenticationResponse = it.body()
                                if (authenticationResponse != null) {
                                    if (authenticationResponse.token != null) {
                                        sessionManager.saveUserToken(authenticationResponse.token)
                                        binding.loading.visibility = View.GONE
                                        val intent = Intent(activity, MainActivity::class.java)
                                        activity.finish()
                                        activity.startActivity(intent)
                                    } else {
                                        binding.loading.visibility = View.GONE
                                    }
                                }
                            }
                        }
                        when (it.code()) {
                            403 -> {
                                handler.post {
                                    Log.e("onResponse", it.message().toString())
                                    binding.loading.visibility = View.GONE
                                    AlertDialog.Builder(activity)
                                        .setTitle("驗證碼錯誤")
                                        .setMessage("請重新輸入驗證碼")
                                        .setPositiveButton("確定") { _, _ -> }
                                        .show()
                                }
                            }
                            // 其他的狀態碼也可以在此處理
                            else -> {
                                handler.post {
                                    Log.e("onResponse", it.message().toString())
                                    binding.loading.visibility = View.GONE
                                    AlertDialog.Builder(activity)
                                        .setTitle("伺服器錯誤")
                                        // show okhttp error code
                                        .setMessage("請聯繫開發人員")
                                        .setPositiveButton("確定") { _, _ -> }
                                        .setNegativeButton("檢視錯誤訊息") { _, _ ->
                                            AlertDialog.Builder(activity)
                                                .setTitle("錯誤代碼:${it.code()}")
                                                .setMessage(it.message().toString())
                                                .setPositiveButton("確定") { _, _ -> }
                                                .show()
                                        }
                                }
                            }
                        }
                    }
                    onFailure = {
                        handler.post {
                            Log.e("onFailure", it!!.message.toString())
                            binding.loading.visibility = View.GONE
                            AlertDialog.Builder(activity)
                                .setTitle("網路錯誤")
                                .setMessage("請確認網路連線是否正常")
                                .setPositiveButton("確定") { _, _ -> }
                                .show()
                        }
                    }
                }
        }

        private fun <T> Call<T>.enqueue(callback: CallBackKt<T>.() -> Unit) {
            val callBackKt = CallBackKt<T>()
            callback.invoke(callBackKt)
            this.enqueue(callBackKt)
        }

        class CallBackKt<T> : Callback<T> {

            var onResponse: ((Response<T>) -> Unit)? = null
            var onFailure: ((t: Throwable?) -> Unit)? = null

            override fun onFailure(call: Call<T>, t: Throwable) {
                onFailure?.invoke(t)
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                onResponse?.invoke(response)
            }

        }

    }
}