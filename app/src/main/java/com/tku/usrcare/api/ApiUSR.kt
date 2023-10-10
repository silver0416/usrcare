package com.tku.usrcare.api

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.tku.usrcare.databinding.FragmentLoginBinding
import com.tku.usrcare.databinding.FragmentLoginVerifyBinding
import com.tku.usrcare.databinding.FragmentSignUpEmailBinding
import com.tku.usrcare.databinding.FragmentSignUpUserDetailBinding
import com.tku.usrcare.model.EmailCheckResponse
import com.tku.usrcare.model.EmailVerify
import com.tku.usrcare.model.Login
import com.tku.usrcare.model.LoginResponse
import com.tku.usrcare.model.MoodTime
import com.tku.usrcare.model.MultipleAccountsListResponse
import com.tku.usrcare.model.PointsDeduction
import com.tku.usrcare.model.RegisterAccount
import com.tku.usrcare.model.ReturnSheet
import com.tku.usrcare.model.Scale
import com.tku.usrcare.model.ScaleListResponse
import com.tku.usrcare.model.SimpleUserObject
import com.tku.usrcare.model.SingleAccountsListResponse
import com.tku.usrcare.model.UsernameCheckResponse
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.LoginActivity
import com.tku.usrcare.view.ui.login.LoginVerifyFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiUSR {
    companion object {
        private val apiClient: ApiService? = ApiClient.client?.create(ApiService::class.java)
        private var handler: Handler = Handler(Looper.getMainLooper())

        fun getTest(activity: Activity, onError: (errorMessage: String) -> Unit) {
            apiClient?.getTest(
                token = "Bearer ${SessionManager(activity).getUserToken()}"
            )
                ?.enqueue {
                    onResponse = {
                        if (it.isSuccessful) {
                            handler.post {
                                Log.e("onResponse", it.message().toString())
                            }
                        } else {
                            handler.post {
                                Log.e("onResponse", it.message().toString())
                                onError(it.code().toString())
                            }
                        }
                    }
                    onFailure = {
                        handler.post {
                            Log.e("onFailure", it!!.message.toString())
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
                                if (it.code() == 403) {
                                    SessionManager(activity).clearAll(context = activity)
                                    androidx.appcompat.app.AlertDialog.Builder(activity)
                                        .setTitle("您已被登出")
                                        .setMessage("即將重新登入")
                                        .setPositiveButton("確定") { _, _ ->
                                            startActivity(
                                                activity,
                                                Intent(activity, LoginActivity::class.java),
                                                null
                                            )
                                        }
                                        .setOnDismissListener {
                                            startActivity(
                                                activity,
                                                Intent(activity, LoginActivity::class.java),
                                                null
                                            )
                                        }
                                        .show()
                                } else {
                                    //loading
                                    AlertDialog.Builder(activity)
                                        .setTitle("伺服器錯誤")
                                        // show okhttp error code
                                        .setMessage("請聯繫開發人員")
                                        .setPositiveButton("確定") { _, _ -> activity.finish() }
                                        .setNegativeButton("檢視錯誤訊息") { _, _ ->
                                            AlertDialog.Builder(activity)
                                                .setTitle("錯誤代碼:${it.code()}")
                                                .setMessage(it.message().toString())
                                                .setPositiveButton("確定") { _, _ ->
                                                    activity.finish()
                                                }
                                                .show()
                                        }
                                        .setOnDismissListener {
                                            activity.finish()
                                        }
                                        .show()
                                }
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

        fun getScale(
            activity: Activity,
            id: Int,
            onSuccess: (scaleResponse: Scale) -> Unit,
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
            activity: Activity,
            registerAccount: RegisterAccount,
            binding: FragmentSignUpUserDetailBinding,
            onSuccess: (token: String) -> Unit,
            onError: (errorMessage: String) -> Unit,
        ) {
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

        fun postSheetResult(
            activity: Activity, id: String,
            returnSheet: ReturnSheet
        ) {
            apiClient?.postSheetResult(
                token = "Bearer ${SessionManager(activity).getUserToken()}",
                id = id,
                returnSheet = returnSheet
            )
                ?.enqueue {
                    onResponse = {
                        if (it.isSuccessful) {
                            handler.post {
                                val returnSheetResponse = it.body()
                                if (returnSheetResponse != null) {
                                    Log.e("onResponse", returnSheetResponse.toString())
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

        fun getSalt(
            activity: Activity,
            username: String,
            onSuccess: (salt: String) -> Unit
        ) {
            apiClient?.getSalt(
                username = username,
                token = "Bearer ${SessionManager(activity).getPublicToken()}"
            )
                ?.enqueue {
                    onResponse = {
                        if (it.isSuccessful) {
                            handler.post {
                                val saltResponse = it.body()
                                if (saltResponse != null) {
                                    //loading
                                    onSuccess(saltResponse.salt)
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

        fun postLogin(
            activity: Activity,
            login: Login,
            binding: FragmentLoginBinding,
            onSuccess: (LoginResponse) -> Unit,
            onError: (errorMessage: String) -> Unit,
        ) {
            apiClient?.postLogin(
                token = "Bearer ${SessionManager(activity).getPublicToken()}",
                login = login
            )
                ?.enqueue {
                    onResponse = {
                        if (it.isSuccessful) {
                            handler.post {
                                val loginResponse = it.body()
                                if (loginResponse != null) {
                                    onSuccess(loginResponse)
                                }
                            }
                        } else {
                            handler.post {
                                Log.e("onResponse", it.message().toString())
                                onError(it.message().toString())
                            }
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

        fun getEmailAccountList(
            activity: Activity,
            email: String,
            onSuccess: (emailAccountList: List<Any>) -> Unit,
        ) {
            apiClient?.getEmailAccountList(
                token = "Bearer ${SessionManager(activity).getUserToken()}",
                email = email
            )
                ?.enqueue {
                    onResponse = {
                        if (it.isSuccessful) {
                            handler.post {
                                val emailAccountList = it.body()
                                if (emailAccountList != null) {
                                    when (emailAccountList) {
                                        is SingleAccountsListResponse -> {
                                            onSuccess(listOf(emailAccountList.userID))
                                        }

                                        is MultipleAccountsListResponse -> {
                                            val list = mutableListOf<List<SimpleUserObject>>()
                                            for (i in emailAccountList.users) {
                                                list.add(listOf(i))
                                            }
                                            onSuccess(
                                                list
                                            )
                                        }
                                    }
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

        fun postMood(
            sessionManager: SessionManager,
            mood: String,
            moodTime: MoodTime,
            onSuccess: (success: Boolean) -> Unit,
            onError: (errorMessage: String) -> Unit,
            onInternetError: (errorMessage: String) -> Unit
        ) {
            apiClient?.postMood(
                token = "Bearer ${sessionManager.getUserToken()}",
                mood = mood,
                moodTime = moodTime
            )
                ?.enqueue {
                    onResponse = {
                        if (it.isSuccessful) {
                            handler.post {
                                onSuccess(true)
                            }
                        } else {
                            handler.post {
                                Log.e("onResponse", it.message().toString())
                                onError(it.message().toString())
                            }
                        }
                    }
                    onFailure = {
                        handler.post {
                            Log.e("onFailure", it!!.message.toString())
                            onInternetError("網路錯誤，請確認網路連線是否正常")
                        }
                    }
                }
        }

        fun getPoints(
            userToken: String,
            onSuccess: (points: Int) -> Unit,
            onError: (errorCode: String) -> Unit,
            onInternetError: (errorMessage: String) -> Unit
        ) {
            apiClient?.getpoints(
                token = "Bearer $userToken"
            )
                ?.enqueue {
                    onResponse = {
                        if (it.isSuccessful) {
                            handler.post {
                                val pointsResponse = it.body()
                                if (pointsResponse != null) {
                                    onSuccess(pointsResponse.points)
                                }
                            }
                        } else {
                            handler.post {
                                Log.e("onResponse", it.message().toString())
                                onError("${it.code()}")
                            }
                        }
                    }
                    onFailure = {
                        handler.post {
                            Log.e("onFailure", it!!.message.toString())
                            onInternetError("網路錯誤，請確認網路連線是否正常")
                        }
                    }
                }
        }

        fun postPointDeduction(
            activity: Activity,
            pointsDeduction: PointsDeduction,
            onSuccess: () -> Unit,
            onError: (errorMessage: String) -> Unit
        ) {
            apiClient?.postPointsDeduction(
                token = "Bearer ${SessionManager(activity).getUserToken()}",
                pointsDeduction = pointsDeduction
            )
                ?.enqueue {
                    onResponse = {
                        if (it.isSuccessful) {
                            handler.post {
                                onSuccess()
                            }
                        } else {
                            handler.post {
                                Log.e("onResponse", it.message().toString())
                                //loading
                                AlertDialog.Builder(activity)
                                    .setTitle("伺服器錯誤")
                                    .setMessage("請聯繫開發人員")
                                    .setPositiveButton("確定") { _, _ ->
                                    }
                                    .setNegativeButton("檢視錯誤訊息") { _, _ ->
                                        AlertDialog.Builder(activity)
                                            .setTitle("錯誤代碼:${it.code()}")
                                            .setMessage(it.message().toString())
                                            .setPositiveButton("確定") { _, _ ->
                                            }
                                            .show()
                                    }
                                    .show()
                                onError(it.message().toString())
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
                                .setPositiveButton("確定") { _, _ ->
                                }
                                .show()
                            onError(it.message.toString())
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