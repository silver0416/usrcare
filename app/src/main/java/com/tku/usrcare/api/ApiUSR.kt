package com.tku.usrcare.api

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.tku.usrcare.databinding.FragmentLoginBinding
import com.tku.usrcare.databinding.FragmentLoginVerifyBinding
import com.tku.usrcare.databinding.FragmentSignUpEmailBinding
import com.tku.usrcare.databinding.FragmentSignUpUserDetailBinding
import com.tku.usrcare.model.Authorization
import com.tku.usrcare.model.EmailCheckResponse
import com.tku.usrcare.model.EmailVerify
import com.tku.usrcare.model.Login
import com.tku.usrcare.model.RegisterAccount
import com.tku.usrcare.model.Scale
import com.tku.usrcare.model.ScaleListResponse
import com.tku.usrcare.model.UsernameCheckResponse
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.MainActivity
import com.tku.usrcare.view.ui.login.LoginFragment
import com.tku.usrcare.view.ui.login.LoginVerifyFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body

class ApiUSR {
    companion object {
        private val apiClient: ApiService? = ApiClient.client?.create(ApiService::class.java)
        var handler: Handler = Handler(Looper.getMainLooper())

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

        fun postAuthorization(
            @Body authorization: Authorization,
            activity: Activity,
            binding: FragmentLoginVerifyBinding,
        ) {
            apiClient?.postAuthorization(
                token = "Bearer ${SessionManager(activity).getPublicToken()}",
                authorization = authorization
            )
                ?.enqueue {

                    binding.loading.visibility = View.VISIBLE
                    val sessionManager = SessionManager(activity)
                    onResponse = {
                        if (it.isSuccessful) {
                            handler.post {
                                val authorizationResponse = it.body()
                                if (authorizationResponse != null) {
                                    if (authorizationResponse.token != null) {
                                        sessionManager.saveUserToken(authorizationResponse.token)
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

        fun getScaleList(
            activity: Activity,
            onSuccess: (scaleListResponse: ScaleListResponse) -> Unit,
            onError: (errorMessage: String) -> Unit,
        ) {
            apiClient?.getScaleList(
                token = "Bearer ${SessionManager(activity).getUserToken()}",
            )
                ?.enqueue {
                    onResponse = {
                        if (it.isSuccessful) {
                            handler.post {
                                val scaleListResponse = it.body()
                                if (scaleListResponse != null) {
                                    onSuccess(scaleListResponse)
                                }
                            }
                        } else {
                            handler.post {
                                Log.e("onResponse", it.message().toString())
                                //loading
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
                                    .show()
                            }
                            onError(it.message().toString())
                        }
                    }
                    onFailure = {
                        handler.post {
                            Log.e("onFailure", it!!.message.toString())
                            //loading
                            AlertDialog.Builder(activity)
                                .setTitle("網路錯誤")
                                .setMessage("請確認網路連線是否正常")
                                .setPositiveButton("確定") { _, _ -> }
                                .show()
                        }
                    }
                }
        }

        fun getScale(
            activity: Activity,
            id: Int,
            onSuccess:(scaleResponse: Scale) -> Unit,
            onError: (errorMessage: String) -> Unit,
        ) {
            apiClient?.getScale(
                id = id.toString(), token = "Bearer ${SessionManager(activity).getUserToken()}"
            )
                ?.enqueue {
                    onResponse = {
                        if (it.isSuccessful) {
                            handler.post {
                                val scaleResponse = it.body()
                                if (scaleResponse != null) {
                                    onSuccess(scaleResponse)
                                }
                            }
                        } else {
                            handler.post {
                                Log.e("onResponse", it.message().toString())
                                //loading
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
                                    .show()
                            }
                        }
                    }
                    onFailure = {
                        handler.post {
                            Log.e("onFailure", it!!.message.toString())
                            //loading
                            AlertDialog.Builder(activity)
                                .setTitle("網路錯誤")
                                .setMessage("請確認網路連線是否正常")
                                .setPositiveButton("確定") { _, _ -> }
                                .show()
                        }
                    }
                }
        }

        fun getEmailCheck(
            activity: Activity,
            email: String,
            binding: FragmentSignUpEmailBinding,
            onSuccess: (emailCheckResponse: EmailCheckResponse) -> Unit,
            onError: (errorMessage: String) -> Unit,
        ) {
            apiClient?.getEmailCheck(
                email = email,
                token = "Bearer ${SessionManager(activity).getPublicToken()}"
            )
                ?.enqueue {
                    binding.loading.isVisible = true
                    onResponse = {
                        if (it.isSuccessful) {
                            handler.post {
                                val emailCheckResponse = it.body()
                                binding.loading.isVisible = false
                                if (emailCheckResponse != null) {
                                    onSuccess(emailCheckResponse)
                                }
                            }
                        } else {
                            binding.loading.isVisible = false
                            handler.post {
                                Log.e("onResponse", it.message().toString())
                                //loading
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
                                    .show()
                            }
                            onError(it.message().toString())
                        }
                    }
                    onFailure = {
                        binding.loading.isVisible = false
                        handler.post {
                            Log.e("onFailure", it!!.message.toString())
                            //loading
                            AlertDialog.Builder(activity)
                                .setTitle("網路錯誤")
                                .setMessage("請確認網路連線是否正常")
                                .setPositiveButton("確定") { _, _ -> }
                                .show()
                        }
                    }
                }
        }

        fun postEmailVerify(
            activity: Activity,
            email: EmailVerify,
            binding: FragmentLoginVerifyBinding,
            navDirections: NavDirections,
            fragment: LoginVerifyFragment
        ) {
            apiClient?.postEmailVerify(
                emailVerify = email,
                token = "Bearer ${SessionManager(activity).getPublicToken()}"
            )
                ?.enqueue {
                    binding.loading.isVisible = true
                    onResponse = {
                        if (it.isSuccessful) {
                            handler.post {
                                val emailVerifyResponse = it.body()
                                if (emailVerifyResponse != null) {
                                    binding.loading.isVisible = false
                                    if (emailVerifyResponse.success) {
                                        navDirections.let { it1 ->
                                            fragment.findNavController().navigate(it1)
                                        }
                                    } else {
                                        AlertDialog.Builder(activity)
                                            .setTitle("驗證失敗")
                                            .setMessage("請確認驗證碼是否正確")
                                            .setPositiveButton("確定") { _, _ -> }
                                            .show()
                                    }
                                }
                            }
                        } else {
                            handler.post {
                                Log.e("onResponse", it.message().toString())
                                binding.loading.isVisible = false
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
                                    .show()
                            }
                        }
                    }
                    onFailure = {
                        handler.post {
                            Log.e("onFailure", it!!.message.toString())
                            binding.loading.isVisible = false
                            AlertDialog.Builder(activity)
                                .setTitle("網路錯誤")
                                .setMessage("請確認網路連線是否正常")
                                .setPositiveButton("確定") { _, _ -> }
                                .show()
                        }
                    }
                }
        }

        fun getUserNameCheck(
            activity: Activity,
            username: String,
            onSuccess: (usernameCheckResponse: UsernameCheckResponse) -> Unit,
        ) {
            apiClient?.getUsernameCheck(
                username = username,
                token = "Bearer ${SessionManager(activity).getPublicToken()}"
            )
                ?.enqueue {
                    onResponse = {
                        if (it.isSuccessful) {
                            handler.post {
                                val usernameCheckResponse = it.body()
                                if (usernameCheckResponse != null) {
                                    //loading
                                    onSuccess(usernameCheckResponse)
                                }
                            }
                        } else {
                            handler.post {
                                Log.e("onResponse", it.message().toString())
                                //loading
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
                                    .show()
                            }
                        }
                    }
                    onFailure = {
                        handler.post {
                            Log.e("onFailure", it!!.message.toString())
                            //loading
                            AlertDialog.Builder(activity)
                                .setTitle("網路錯誤")
                                .setMessage("請確認網路連線是否正常")
                                .setPositiveButton("確定") { _, _ -> }
                                .show()
                        }
                    }
                }
        }
        fun postRegisterAccount(
            activity : Activity,
            registerAccount: RegisterAccount,
            binding: FragmentSignUpUserDetailBinding,
            onSuccess: (token: String) -> Unit,
            onError: (errorMessage: String) -> Unit,
        ){
            apiClient?.postRegister(
                token = "Bearer ${SessionManager(activity).getPublicToken()}",
                registerAccount = registerAccount
            )
                ?.enqueue {
                    binding.loading.isVisible = true
                    onResponse = {
                        if (it.isSuccessful) {
                            handler.post {
                                val registerAccountResponse = it.body()
                                if (registerAccountResponse != null) {
                                    binding.loading.isVisible = false
                                    if (registerAccountResponse.token != null) {
                                        binding.loading.isVisible = false
                                        onSuccess(registerAccountResponse.token)
                                    } else {
                                        binding.loading.isVisible = false
                                        onError("註冊失敗:${registerAccountResponse.error}")
                                    }
                                }
                            }
                        } else {
                            binding.loading.isVisible = false
                            handler.post {
                                Log.e("onResponse", it.message().toString())
                                //loading
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
                                    .show()
                            }
                            onError(it.message().toString())
                        }
                    }
                    onFailure = {
                        binding.loading.isVisible = false
                        handler.post {
                            Log.e("onFailure", it!!.message.toString())
                            //loading
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