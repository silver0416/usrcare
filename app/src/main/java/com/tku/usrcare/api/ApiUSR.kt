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
import com.tku.usrcare.model.AccountOtp
import com.tku.usrcare.model.BindingResponse
import com.tku.usrcare.model.EmailCheckResponse
import com.tku.usrcare.model.EmailVerify
import com.tku.usrcare.model.JwtToken
import com.tku.usrcare.model.JwtTokenResponse
import com.tku.usrcare.model.Login
import com.tku.usrcare.model.LoginResponse
import com.tku.usrcare.model.MoodPuncher
import com.tku.usrcare.model.MoodPuncherResponse
import com.tku.usrcare.model.MoodTime
import com.tku.usrcare.model.OAuthCheckResponse
import com.tku.usrcare.model.OAuthUnbindResponse
import com.tku.usrcare.model.PointsDeduction
import com.tku.usrcare.model.ReBinding
import com.tku.usrcare.model.ReBindingResponse
import com.tku.usrcare.model.RegisterAccount
import com.tku.usrcare.model.RegistrationToken
import com.tku.usrcare.model.RegistrationTokenResponse
import com.tku.usrcare.model.ResetPassword
import com.tku.usrcare.model.ReturnSheet
import com.tku.usrcare.model.Scale
import com.tku.usrcare.model.ScaleListResponse
import com.tku.usrcare.model.ShoppingImformations
import com.tku.usrcare.model.ShoppingResponse
import com.tku.usrcare.model.SimpleUserObject
import com.tku.usrcare.model.SportVideoUploadResponse
import com.tku.usrcare.model.UsernameCheckResponse
import com.tku.usrcare.model.Version
import com.tku.usrcare.model.VideoListResponse
import com.tku.usrcare.model.getItemsPriceResponse
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.LoginActivity
import com.tku.usrcare.view.ui.login.LoginVerifyFragment
import com.unity3d.player.E
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ApiUSR {
    companion object {
        private val apiClient: ApiService? = ApiClient.client?.create(ApiService::class.java)
        private var handler: Handler = Handler(Looper.getMainLooper())

        fun postTest(
            activity: Activity, version: Version, onError: (errorMessage: String) -> Unit
        ) {
            apiClient?.postTest(
                token = "Bearer ${SessionManager(activity).getUserToken()}", version = version
            )?.enqueue {
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
                        AlertDialog.Builder(activity).setTitle("網路錯誤")
                            .setMessage("請確認網路連線是否正常")
                            .setPositiveButton("確定") { _, _ -> }.show()
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
            )?.enqueue {
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
                                    .setTitle("您已被登出").setMessage("即將重新登入")
                                    .setPositiveButton("確定") { _, _ ->
                                        startActivity(
                                            activity,
                                            Intent(activity, LoginActivity::class.java),
                                            null
                                        )
                                    }.setOnDismissListener {
                                        startActivity(
                                            activity,
                                            Intent(activity, LoginActivity::class.java),
                                            null
                                        )
                                    }.show()
                            } else {
                                //loading
                                AlertDialog.Builder(activity).setTitle("伺服器錯誤")
                                    // show okhttp error code
                                    .setMessage("請聯繫開發人員")
                                    .setPositiveButton("確定") { _, _ -> activity.finish() }
                                    .setNegativeButton("檢視錯誤訊息") { _, _ ->
                                        AlertDialog.Builder(activity)
                                            .setTitle("錯誤代碼:${it.code()}")
                                            .setMessage(it.message().toString())
                                            .setPositiveButton("確定") { _, _ ->
                                                activity.finish()
                                            }.show()
                                    }.setOnDismissListener {
                                        activity.finish()
                                    }.show()
                            }
                        }
                    }
                }
                onFailure = {
                    handler.post {
                        Log.e("onFailure", it!!.message.toString())
                        //loading
                        AlertDialog.Builder(activity).setTitle("網路錯誤")
                            .setMessage("請確認網路連線是否正常")
                            .setPositiveButton("確定") { _, _ -> }.show()
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
            )?.enqueue {
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
                            AlertDialog.Builder(activity).setTitle("伺服器錯誤")
                                // show okhttp error code
                                .setMessage("請聯繫開發人員")
                                .setPositiveButton("確定") { _, _ -> }
                                .setNegativeButton("檢視錯誤訊息") { _, _ ->
                                    AlertDialog.Builder(activity)
                                        .setTitle("錯誤代碼:${it.code()}")
                                        .setMessage(it.message().toString())
                                        .setPositiveButton("確定") { _, _ -> }.show()
                                }.show()
                        }
                    }
                }
                onFailure = {
                    handler.post {
                        Log.e("onFailure", it!!.message.toString())
                        //loading
                        AlertDialog.Builder(activity).setTitle("網路錯誤")
                            .setMessage("請確認網路連線是否正常")
                            .setPositiveButton("確定") { _, _ -> }.show()
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
                email = email, token = "Bearer ${SessionManager(activity).getPublicToken()}"
            )?.enqueue {
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
                            AlertDialog.Builder(activity).setTitle("伺服器錯誤")
                                // show okhttp error code
                                .setMessage("請聯繫開發人員")
                                .setPositiveButton("確定") { _, _ -> }
                                .setNegativeButton("檢視錯誤訊息") { _, _ ->
                                    AlertDialog.Builder(activity)
                                        .setTitle("錯誤代碼:${it.code()}")
                                        .setMessage(it.message().toString())
                                        .setPositiveButton("確定") { _, _ -> }.show()
                                }.show()
                        }
                        onError(it.message().toString())
                    }
                }
                onFailure = {
                    binding.loading.isVisible = false
                    handler.post {
                        Log.e("onFailure", it!!.message.toString())
                        //loading
                        AlertDialog.Builder(activity).setTitle("網路錯誤")
                            .setMessage("請確認網路連線是否正常")
                            .setPositiveButton("確定") { _, _ -> }.show()
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
                emailVerify = email, token = "Bearer ${SessionManager(activity).getPublicToken()}"
            )?.enqueue {
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
                                    AlertDialog.Builder(activity).setTitle("驗證失敗")
                                        .setMessage("請確認驗證碼是否正確")
                                        .setPositiveButton("確定") { _, _ -> }.show()
                                }
                            }
                        }
                    } else {
                        handler.post {
                            Log.e("onResponse", it.message().toString())
                            binding.loading.isVisible = false
                            AlertDialog.Builder(activity).setTitle("伺服器錯誤")
                                // show okhttp error code
                                .setMessage("請聯繫開發人員")
                                .setPositiveButton("確定") { _, _ -> }
                                .setNegativeButton("檢視錯誤訊息") { _, _ ->
                                    AlertDialog.Builder(activity)
                                        .setTitle("錯誤代碼:${it.code()}")
                                        .setMessage(it.message().toString())
                                        .setPositiveButton("確定") { _, _ -> }.show()
                                }.show()
                        }
                    }
                }
                onFailure = {
                    handler.post {
                        Log.e("onFailure", it!!.message.toString())
                        binding.loading.isVisible = false
                        AlertDialog.Builder(activity).setTitle("網路錯誤")
                            .setMessage("請確認網路連線是否正常")
                            .setPositiveButton("確定") { _, _ -> }.show()
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
                username = username, token = "Bearer ${SessionManager(activity).getPublicToken()}"
            )?.enqueue {
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
                            AlertDialog.Builder(activity).setTitle("伺服器錯誤")
                                // show okhttp error code
                                .setMessage("請聯繫開發人員")
                                .setPositiveButton("確定") { _, _ -> }
                                .setNegativeButton("檢視錯誤訊息") { _, _ ->
                                    AlertDialog.Builder(activity)
                                        .setTitle("錯誤代碼:${it.code()}")
                                        .setMessage(it.message().toString())
                                        .setPositiveButton("確定") { _, _ -> }.show()
                                }.show()
                        }
                    }
                }
                onFailure = {
                    handler.post {
                        Log.e("onFailure", it!!.message.toString())
                        //loading
                        AlertDialog.Builder(activity).setTitle("網路錯誤")
                            .setMessage("請確認網路連線是否正常")
                            .setPositiveButton("確定") { _, _ -> }.show()
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
            )?.enqueue {
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
                            AlertDialog.Builder(activity).setTitle("伺服器錯誤")
                                // show okhttp error code
                                .setMessage("請聯繫開發人員")
                                .setPositiveButton("確定") { _, _ -> }
                                .setNegativeButton("檢視錯誤訊息") { _, _ ->
                                    AlertDialog.Builder(activity)
                                        .setTitle("錯誤代碼:${it.code()}")
                                        .setMessage(it.message().toString())
                                        .setPositiveButton("確定") { _, _ -> }.show()
                                }.show()
                        }
                        onError(it.message().toString())
                    }
                }
                onFailure = {
                    binding.loading.isVisible = false
                    handler.post {
                        Log.e("onFailure", it!!.message.toString())
                        //loading
                        AlertDialog.Builder(activity).setTitle("網路錯誤")
                            .setMessage("請確認網路連線是否正常")
                            .setPositiveButton("確定") { _, _ -> }.show()
                    }
                }
            }
        }

        fun postGoogleOAuthRegisterAccount(
            activity: Activity,
            registerAccount: RegisterAccount,
            binding: FragmentSignUpUserDetailBinding,
            onSuccess: (token: String) -> Unit,
            onError: (errorMessage: String) -> Unit,
        ) {
            apiClient?.postGoogleOAuthRegister(
                token = "Bearer ${SessionManager(activity).getPublicToken()}",
                registerAccount = registerAccount
            )?.enqueue {
                binding.loading.isVisible = true
                onResponse = {
                    if (it.isSuccessful) {
                        handler.post {
                            val registerAccountResponse = it.body()
                            if (registerAccountResponse != null) {
                                binding.loading.isVisible = false
                                onSuccess(registerAccountResponse.token)
                            }
                        }
                    } else {
                        binding.loading.isVisible = false
                        handler.post {
                            Log.e("onResponse", it.message().toString())
                            //loading
                            AlertDialog.Builder(activity).setTitle("伺服器錯誤")
                                // show okhttp error code
                                .setMessage("請聯繫開發人員")
                                .setPositiveButton("確定") { _, _ -> }
                                .setNegativeButton("檢視錯誤訊息") { _, _ ->
                                    AlertDialog.Builder(activity)
                                        .setTitle("錯誤代碼:${it.code()}")
                                        .setMessage(it.message().toString())
                                        .setPositiveButton("確定") { _, _ -> }.show()
                                }.show()
                        }
                        onError(it.message().toString())
                    }
                }
                onFailure = {
                    binding.loading.isVisible = false
                    handler.post {
                        Log.e("onFailure", it!!.message.toString())
                        //loading
                        AlertDialog.Builder(activity).setTitle("網路錯誤")
                            .setMessage("請確認網路連線是否正常")
                            .setPositiveButton("確定") { _, _ -> }.show()
                    }
                }
            }
        }

        fun postLineOAuthRegisterAccount(
            activity: Activity,
            registerAccount: RegisterAccount,
            binding: FragmentSignUpUserDetailBinding,
            onSuccess: (token: String) -> Unit,
            onError: (errorMessage: String) -> Unit,
        ) {
            apiClient?.postLineOAuthRegister(
                token = "Bearer ${SessionManager(activity).getPublicToken()}",
                registerAccount = registerAccount
            )?.enqueue {
                binding.loading.isVisible = true
                onResponse = {
                    if (it.isSuccessful) {
                        handler.post {
                            val registerAccountResponse = it.body()
                            if (registerAccountResponse != null) {
                                binding.loading.isVisible = false
                                onSuccess(registerAccountResponse.token)
                            }
                        }
                    } else {
                        binding.loading.isVisible = false
                        handler.post {
                            Log.e("onResponse", it.message().toString())
                            //loading
                            AlertDialog.Builder(activity).setTitle("伺服器錯誤")
                                // show okhttp error code
                                .setMessage("請聯繫開發人員")
                                .setPositiveButton("確定") { _, _ -> }
                                .setNegativeButton("檢視錯誤訊息") { _, _ ->
                                    AlertDialog.Builder(activity)
                                        .setTitle("錯誤代碼:${it.code()}")
                                        .setMessage(it.message().toString())
                                        .setPositiveButton("確定") { _, _ -> }.show()
                                }.show()
                        }
                        onError(it.message().toString())
                    }
                }
                onFailure = {
                    binding.loading.isVisible = false
                    handler.post {
                        Log.e("onFailure", it!!.message.toString())
                        //loading
                        AlertDialog.Builder(activity).setTitle("網路錯誤")
                            .setMessage("請確認網路連線是否正常")
                            .setPositiveButton("確定") { _, _ -> }.show()
                    }
                }
            }
        }

        fun postSheetResult(
            activity: Activity,
            id: String,
            returnSheet: ReturnSheet,
            onSuccess: (consultation: Boolean) -> Unit
        ) {
            apiClient?.postSheetResult(
                token = "Bearer ${SessionManager(activity).getUserToken()}",
                id = id,
                returnSheet = returnSheet
            )?.enqueue {
                onResponse = {
                    if (it.isSuccessful) {
                        handler.post {
                            val returnSheetResponse = it.body()
                            if (returnSheetResponse != null) {
                                Log.d(
                                    "postSheetResult",
                                    returnSheetResponse.consultation.toString()
                                )
                                onSuccess(returnSheetResponse.consultation)
                            }
                        }
                    } else {
                        handler.post {
                            Log.e("onResponse", it.message().toString())
                            //loading
                            AlertDialog.Builder(activity).setTitle("伺服器錯誤")
                                // show okhttp error code
                                .setMessage("請聯繫開發人員")
                                .setPositiveButton("確定") { _, _ -> }
                                .setNegativeButton("檢視錯誤訊息") { _, _ ->
                                    AlertDialog.Builder(activity)
                                        .setTitle("錯誤代碼:${it.code()}")
                                        .setMessage(it.message().toString())
                                        .setPositiveButton("確定") { _, _ -> }.show()
                                }.show()
                        }
                    }
                }
                onFailure = {
                    handler.post {
                        Log.e("onFailure", it!!.message.toString())
                        //loading
                        AlertDialog.Builder(activity).setTitle("網路錯誤")
                            .setMessage("請確認網路連線是否正常")
                            .setPositiveButton("確定") { _, _ -> }.show()
                    }
                }
            }
        }

        fun getSalt(
            activity: Activity, username: String, onSuccess: (salt: String?) -> Unit
        ) {
            apiClient?.getSalt(
                username = username, token = "Bearer ${SessionManager(activity).getPublicToken()}"
            )?.enqueue {
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
                            AlertDialog.Builder(activity).setTitle("伺服器錯誤")
                                // show okhttp error code
                                .setMessage("請聯繫開發人員")
                                .setPositiveButton("確定") { _, _ -> }
                                .setNegativeButton("檢視錯誤訊息") { _, _ ->
                                    AlertDialog.Builder(activity)
                                        .setTitle("錯誤代碼:${it.code()}")
                                        .setMessage(it.message().toString())
                                        .setPositiveButton("確定") { _, _ -> }.show()
                                }.show()
                        }
                    }
                }
                onFailure = {
                    handler.post {
                        Log.e("onFailure", it!!.message.toString())
                        //loading
                        AlertDialog.Builder(activity).setTitle("網路錯誤")
                            .setMessage("請確認網路連線是否正常")
                            .setPositiveButton("確定") { _, _ -> }.show()
                    }

                }
            }
        }

        fun postLogin(
            activity: Activity,
            login: Login,
            binding: FragmentLoginBinding,
            onSuccessLoginNormal: (LoginResponse) -> Unit,
            onChangePassword: (LoginResponse) -> Unit,
            onError: (errorMessage: String) -> Unit,
        ) {
            apiClient?.postLogin(
                token = "Bearer ${SessionManager(activity).getPublicToken()}", login = login
            )?.enqueue {
                onResponse = {
                    if (it.isSuccessful) {
                        handler.post {
                            val loginResponse = it.body()
                            if (loginResponse != null) {
                                if (loginResponse.otp == null) {
                                    onSuccessLoginNormal(loginResponse)
                                } else {
                                    onChangePassword(loginResponse)
                                }
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
                        AlertDialog.Builder(activity).setTitle("網路錯誤")
                            .setMessage("請確認網路連線是否正常")
                            .setPositiveButton("確定") { _, _ -> }.show()
                    }
                }
            }
        }

        fun getEmailAccountList(
            activity: Activity,
            email: String,
            onSuccess: (emailAccountList: List<String?>, emailUserIdList: List<String?>) -> Unit,
        ) {
            apiClient?.getEmailAccountList(
                token = "Bearer ${SessionManager(activity).getPublicToken()}", email = email
            )?.enqueue {
                onResponse = {
                    if (it.isSuccessful) {
                        handler.post {
                            val emailAccountList = it.body()
                            if (emailAccountList != null) {
                                if (emailAccountList.status == "single") {
                                    onSuccess(
                                        listOf(emailAccountList.username),
                                        listOf(emailAccountList.userToken)
                                    )
                                } else if (emailAccountList.status == "multiple") {
                                    val list = mutableListOf<List<SimpleUserObject>>()
                                    for (i in emailAccountList.users!!) {
                                        list.add(listOf(i))
                                    }
                                    onSuccess(list.flatten().map { it.username },
                                        list.flatten().map { it.userToken })
                                }
                            }
                        }
                    } else {
                        handler.post {
                            Log.e("onResponse", it.message().toString())
                            //loading
                            AlertDialog.Builder(activity).setTitle("伺服器錯誤")
                                // show okhttp error code
                                .setMessage("請聯繫開發人員")
                                .setPositiveButton("確定") { _, _ -> }
                                .setNegativeButton("檢視錯誤訊息") { _, _ ->
                                    AlertDialog.Builder(activity)
                                        .setTitle("錯誤代碼:${it.code()}")
                                        .setMessage(it.message().toString())
                                        .setPositiveButton("確定") { _, _ -> }.show()
                                }.show()
                        }
                    }
                }
                onFailure = {
                    handler.post {
                        Log.e("onFailure", it!!.message.toString())
                        //loading
                        AlertDialog.Builder(activity).setTitle("網路錯誤")
                            .setMessage("請確認網路連線是否正常")
                            .setPositiveButton("確定") { _, _ -> }.show()
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
                token = "Bearer ${sessionManager.getUserToken()}", mood = mood, moodTime = moodTime
            )?.enqueue {
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
            )?.enqueue {
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
            )?.enqueue {
                onResponse = {
                    if (it.isSuccessful) {
                        handler.post {
                            onSuccess()
                        }
                    } else {
                        handler.post {
                            Log.e("onResponse", it.message().toString())
                            //loading
                            AlertDialog.Builder(activity).setTitle("伺服器錯誤")
                                .setMessage("請聯繫開發人員")
                                .setPositiveButton("確定") { _, _ ->
                                }.setNegativeButton("檢視錯誤訊息") { _, _ ->
                                    AlertDialog.Builder(activity)
                                        .setTitle("錯誤代碼:${it.code()}")
                                        .setMessage(it.message().toString())
                                        .setPositiveButton("確定") { _, _ ->
                                        }.show()
                                }.show()
                            onError(it.message().toString())
                        }
                    }
                }
                onFailure = {
                    handler.post {
                        Log.e("onFailure", it!!.message.toString())
                        //loading
                        AlertDialog.Builder(activity).setTitle("網路錯誤")
                            .setMessage("請確認網路連線是否正常")
                            .setPositiveButton("確定") { _, _ ->
                            }.show()
                        onError(it.message.toString())
                    }

                }
            }
        }

        fun postResetPassword(
            activity: Activity,
            resetPassword: ResetPassword,
            token: String,
            onSuccess: () -> Unit,
            onFail: () -> Unit
        ) {
            apiClient?.postResetPassword(
                token = "Bearer $token", resetPassword = resetPassword
            )?.enqueue {
                onResponse = {
                    if (it.isSuccessful) {
                        handler.post {
                            onSuccess()
                        }
                    } else {
                        handler.post {
                            Log.e("onResponse", it.message().toString())
                            //loading
                            if (it.code() == 401) {
                                AlertDialog.Builder(activity).setTitle("驗證失敗")
                                    .setMessage("請確認驗證碼是否正確")
                                    .setPositiveButton("確定") { _, _ -> }.show()
                            } else {
                                AlertDialog.Builder(activity).setTitle("伺服器錯誤")
                                    .setMessage("請聯繫開發人員")
                                    .setPositiveButton("確定") { _, _ ->
                                    }.setNegativeButton("檢視錯誤訊息") { _, _ ->
                                        AlertDialog.Builder(activity)
                                            .setTitle("錯誤代碼:${it.code()}")
                                            .setMessage(it.message().toString())
                                            .setPositiveButton("確定") { _, _ ->
                                            }.show()
                                    }.show()
                            }
                            onFail()
                        }
                    }
                }
                onFailure = {
                    handler.post {
                        Log.e("onFailure", it!!.message.toString())
                        //loading
                        AlertDialog.Builder(activity).setTitle("網路錯誤")
                            .setMessage("請確認網路連線是否正常")
                            .setPositiveButton("確定") { _, _ ->
                            }.show()
                        onFail()
                    }

                }
            }
        }

        fun postAccountOtpCheck(
            activity: Activity,
            accountOtp: AccountOtp,
            onSuccess: () -> Unit,
            onFail: () -> Unit
        ) {
            apiClient?.postAccountOtpCheck(
                token = "Bearer ${SessionManager(activity).getPublicToken()}",
                accountOtp = accountOtp
            )?.enqueue {
                onResponse = {
                    if (it.isSuccessful) {
                        handler.post {
                            onSuccess()
                        }
                    } else {
                        handler.post {
                            Log.e("onResponse", it.message().toString())
                            //loading
                            AlertDialog.Builder(activity).setTitle("驗證失敗")
                                .setMessage("請確認驗證碼是否正確")
                                .setPositiveButton("確定") { _, _ -> }.show()
                            onFail()
                        }
                    }
                }
                onFailure = {
                    handler.post {
                        Log.e("onFailure", it!!.message.toString())
                        //loading
                        AlertDialog.Builder(activity).setTitle("網路錯誤")
                            .setMessage("請確認網路連線是否正常")
                            .setPositiveButton("確定") { _, _ ->
                            }.show()
                        onFail()
                    }

                }
            }
        }

        fun getCheckInRecord(
            activity: Activity,
            sessionManager: SessionManager,
            onSuccess: () -> Unit,
            onFail: () -> Unit
        ) {
            apiClient?.getCheckInRecord(
                token = "Bearer ${SessionManager(activity).getUserToken()}"
            )?.enqueue {
                onResponse = {
                    if (it.isSuccessful) {
                        handler.post {
                            val checkInRecordResponse = it.body()
                            if (checkInRecordResponse != null) {
                                for (i in checkInRecordResponse.checkInTime) {
                                    val dateTime = LocalDateTime.parse(i)
                                    // 定義新的日期格式
                                    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                    // 將日期時間格式化為新的格式
                                    val formattedDateString = dateTime.format(dateFormatter)
                                    sessionManager.addSignedDateTime(formattedDateString, 0)
                                }
                                onSuccess()
                            }
                        }
                    } else {
                        handler.post {
                            Log.e("onResponse", it.message().toString())
                            AlertDialog.Builder(activity).setTitle("伺服器錯誤")
                                .setMessage("請聯繫開發人員")
                                .setPositiveButton("確定") { _, _ ->
                                }.setNegativeButton("檢視錯誤訊息") { _, _ ->
                                    AlertDialog.Builder(activity)
                                        .setTitle("錯誤代碼:${it.code()}")
                                        .setMessage(it.message().toString())
                                        .setPositiveButton("確定") { _, _ ->
                                        }.show()
                                }.show()
                            onFail()
                        }
                    }
                }
                onFailure = {
                    handler.post {
                        Log.e("onFailure", it!!.message.toString())
                        AlertDialog.Builder(activity).setTitle("網路錯誤")
                            .setMessage("請確認網路連線是否正常")
                            .setPositiveButton("確定") { _, _ ->
                            }.show()
                        onFail()
                    }
                }
            }
        }

        fun getHistoryStory(
            sessionManager: SessionManager,
            onSuccess: () -> Unit,
            onFail: (errorMessage: String) -> Unit
        ) {
            apiClient?.getHistoryStory(
                token = "Bearer ${sessionManager.getUserToken()}"
            )?.enqueue {
                onResponse = {
                    if (it.isSuccessful) {
                        handler.post {
                            val historyStoryResponse = it.body()
                            if (historyStoryResponse != null) {
                                sessionManager.saveHistoryStory(historyStoryResponse)
                                onSuccess()
                            }
                        }
                    } else {
                        handler.post {
                            Log.e("onResponse", it.message().toString())
                            onFail(it.message())
                        }
                    }
                }
                onFailure = {
                    handler.post {
                        Log.e("onFailure", it!!.message.toString())
                        onFail("網路錯誤，請確認網路連線是否正常")
                    }
                }
            }
        }

        fun getVocabulary(
            sessionManager: SessionManager,
            onSuccess: () -> Unit,
            onFail: (errorMessage: String) -> Unit
        ) {
            apiClient?.getVocabulary(
                token = "Bearer ${sessionManager.getUserToken()}"
            )?.enqueue {
                onResponse = {
                    if (it.isSuccessful) {
                        handler.post {
                            val vocabularyResponse = it.body()
                            if (vocabularyResponse != null) {
                                sessionManager.saveVocabulary(vocabularyResponse)
                                onSuccess()
                            }
                        }
                    } else {
                        handler.post {
                            Log.e("onResponse", it.message().toString())
                            onFail(it.message())
                        }
                    }
                }
                onFailure = {
                    handler.post {
                        Log.e("onFailure", it!!.message.toString())
                        onFail("網路錯誤，請確認網路連線是否正常")
                    }
                }
            }
        }

        fun getCheat(
            sessionManager: SessionManager,
            onSuccess: (points: Int) -> Unit,
            onFail: (errorMessage: String) -> Unit
        ) {
            apiClient?.getCheat(
                token = "Bearer ${sessionManager.getUserToken()}"
            )?.enqueue {
                onResponse = {
                    if (it.isSuccessful) {
                        handler.post {
                            val cheatResponse = it.body()
                            if (cheatResponse != null) {
                                onSuccess(cheatResponse.points)
                            }
                        }
                    } else {
                        handler.post {
                            Log.e("onResponse", it.message().toString())
                            onFail(it.message())
                        }
                    }
                }
                onFailure = {
                    handler.post {
                        Log.e("onFailure", it!!.message.toString())
                        onFail("網路錯誤，請確認網路連線是否正常")
                    }
                }
            }
        }

        fun postMoodPuncher(
            sessionManager: SessionManager,
            moodPuncher: MoodPuncher,
            onSuccess: (MoodPuncherResponse) -> Unit,
            onFail: (errorMessage: String) -> Unit
        ) {
            apiClient?.postMoodPuncher(
                token = "Bearer ${sessionManager.getUserToken()}", moodPuncher = moodPuncher
            )?.enqueue {
                onResponse = {
                    if (it.isSuccessful) {
                        val moodPuncherResponse = it.body()
                        if (moodPuncherResponse != null) {
                            handler.post {
                                onSuccess(moodPuncherResponse)
                            }
                        }
                    } else {
                        handler.post {
                            Log.e("onResponse", it.message().toString())
                            onFail(it.message())
                        }
                    }
                }
                onFailure = {
                    handler.post {
                        Log.e("onFailure", it!!.message.toString())
                        onFail("網路錯誤，請確認網路連線是否正常")
                    }
                }
            }
        }

        fun postGoogleOAuth(
            jwtToken: JwtToken,
            sessionManager: SessionManager,
            onSuccess: (JwtTokenResponse) -> Unit,
            onFail: (errorMessage: String) -> Unit
        ) {
            apiClient?.postGoogleOAuth(
                token = "Bearer ${sessionManager.getPublicToken()}", jwtToken = jwtToken
            )?.enqueue {
                onResponse = {
                    if (it.isSuccessful) {
                        val googleOauthResponse = it.body()
                        if (googleOauthResponse != null) {
                            handler.post {
                                onSuccess(googleOauthResponse)
                            }
                        }
                    } else {
                        handler.post {
                            Log.e("onResponse", it.message().toString())
                            onFail(it.message())
                        }
                    }
                }
                onFailure = {
                    handler.post {
                        Log.e("onFailure", it!!.message.toString())
                        onFail("網路錯誤，請確認網路連線是否正常")
                    }
                }
            }
        }

        fun postLineOAuth(
            jwtToken: JwtToken,
            sessionManager: SessionManager,
            onSuccess: (JwtTokenResponse) -> Unit,
            onFail: (errorMessage: String) -> Unit
        ) {
            apiClient?.postLineOAuth(
                token = "Bearer ${sessionManager.getPublicToken()}", jwtToken = jwtToken
            )?.enqueue {
                onResponse = {
                    if (it.isSuccessful) {
                        val lineOauthResponse = it.body()
                        if (lineOauthResponse != null) {
                            handler.post {
                                onSuccess(lineOauthResponse)
                            }
                        }
                    } else {
                        handler.post {
                            Log.e("onResponse", it.message().toString())
                            onFail(it.message())
                        }
                    }
                }
                onFailure = {
                    handler.post {
                        Log.e("onFailure", it!!.message.toString())
                        onFail("網路錯誤，請確認網路連線是否正常")
                    }
                }
            }
        }

        fun postGoogleOAuthBind(
            jwtToken: JwtToken,
            sessionManager: SessionManager,
            onSuccess: (BindingResponse) -> Unit,
            onFail: (errorMessage: String) -> Unit
        ) {
            apiClient?.postGoogleOAuthBind(
                token = "Bearer ${sessionManager.getUserToken()}", jwtToken = jwtToken
            )?.enqueue {
                onResponse = {
                    if (it.isSuccessful) {
                        val googleOauthResponse = it.body()
                        if (googleOauthResponse != null) {
                            handler.post {
                                onSuccess(googleOauthResponse)
                            }
                        }
                    } else {
                        handler.post {
                            Log.e("onResponse", it.message().toString())
                            onFail(it.message())
                        }
                    }
                }
                onFailure = {
                    handler.post {
                        Log.e("onFailure", it!!.message.toString())
                        onFail("網路錯誤，請確認網路連線是否正常")
                    }
                }
            }
        }

        fun postLineOAuthBind(
            jwtToken: JwtToken,
            sessionManager: SessionManager,
            onSuccess: (BindingResponse) -> Unit,
            onFail: (errorMessage: String) -> Unit
        ) {
            apiClient?.postLineOAuthBind(
                token = "Bearer ${sessionManager.getUserToken()}", jwtToken = jwtToken
            )?.enqueue {
                onResponse = {
                    if (it.isSuccessful) {
                        val lineOauthResponse = it.body()
                        if (lineOauthResponse != null) {
                            handler.post {
                                onSuccess(lineOauthResponse)
                            }
                        }
                    } else {
                        handler.post {
                            Log.e("onResponse", it.message().toString())
                            onFail(it.message())
                        }
                    }
                }
                onFailure = {
                    handler.post {
                        Log.e("onFailure", it!!.message.toString())
                        onFail("網路錯誤，請確認網路連線是否正常")
                    }
                }
            }
        }

        fun postGoogleOAuthRebind(
            reBinding: ReBinding,
            sessionManager: SessionManager,
            onSuccess: (ReBindingResponse) -> Unit,
            onFail: (errorMessage: String) -> Unit
        ) {
            apiClient?.postGoogleOAuthRebind(
                token = "Bearer ${sessionManager.getUserToken()}", reBinding = reBinding
            )?.enqueue {
                onResponse = {
                    if (it.isSuccessful) {
                        val googleOauthResponse = it.body()
                        if (googleOauthResponse != null) {
                            handler.post {
                                onSuccess(googleOauthResponse)
                            }
                        }
                    } else {
                        handler.post {
                            Log.e("onResponse", it.message().toString())
                            onFail(it.message())
                        }
                    }
                }
                onFailure = {
                    handler.post {
                        Log.e("onFailure", it!!.message.toString())
                        onFail("網路錯誤，請確認網路連線是否正常")
                    }
                }
            }
        }

        fun postLineOAuthRebind(
            reBinding: ReBinding,
            sessionManager: SessionManager,
            onSuccess: (ReBindingResponse) -> Unit,
            onFail: (errorMessage: String) -> Unit
        ) {
            apiClient?.postLineOAuthRebind(
                token = "Bearer ${sessionManager.getUserToken()}", reBinding = reBinding
            )?.enqueue {
                onResponse = {
                    if (it.isSuccessful) {
                        val lineOauthResponse = it.body()
                        if (lineOauthResponse != null) {
                            handler.post {
                                onSuccess(lineOauthResponse)
                            }
                        }
                    } else {
                        handler.post {
                            Log.e("onResponse", it.message().toString())
                            onFail(it.message())
                        }
                    }
                }
                onFailure = {
                    handler.post {
                        Log.e("onFailure", it!!.message.toString())
                        onFail("網路錯誤，請確認網路連線是否正常")
                    }
                }
            }
        }

        fun getOAuthCheck(
            sessionManager: SessionManager,
            onSuccess: (OAuthCheckResponse) -> Unit,
            onFail: (errorMessage: String) -> Unit
        ) {
            apiClient?.getOAuthCheck(
                token = "Bearer ${sessionManager.getUserToken()}"
            )?.enqueue {
                onResponse = {
                    if (it.isSuccessful) {
                        val oAuthCheckResponse = it.body()
                        if (oAuthCheckResponse != null) {
                            handler.post {
                                onSuccess(oAuthCheckResponse)
                            }
                        }
                    } else {
                        handler.post {
                            Log.e("onResponse", it.message().toString())
                            onFail(it.message())
                        }
                    }
                }
                onFailure = {
                    handler.post {
                        Log.e("onFailure", it!!.message.toString())
                        onFail("網路錯誤，請確認網路連線是否正常")
                    }
                }
            }
        }


        fun deleteOauthUnbind(
            sessionManager: SessionManager,
            oauthType: String,
            onSuccess: (OAuthUnbindResponse) -> Unit,
            onFail: (errorMessage: String) -> Unit
        ) {
            apiClient?.deleteOAuthUnbind(
                token = "Bearer ${sessionManager.getUserToken()}", oauthType = oauthType
            )?.enqueue {
                onResponse = {
                    if (it.isSuccessful) {
                        val oAuthUnbindResponse = it.body()
                        if (oAuthUnbindResponse != null) {
                            handler.post {
                                onSuccess(oAuthUnbindResponse)
                            }
                        }
                    } else {
                        handler.post {
                            Log.e("onResponse", it.message().toString())
                            onFail(it.message())
                        }
                    }
                }
                onFailure = {
                    handler.post {
                        Log.e("onFailure", it!!.message.toString())
                        onFail("網路錯誤，請確認網路連線是否正常，或是該帳號已經綁定過")
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

        suspend fun postFirebaseCloudMessagingToken(
            sessionManager: SessionManager,
            registrationToken: RegistrationToken,
            onSuccess: (RegistrationTokenResponse) -> Unit,
            onFail: (errorMessage: String) -> Unit
        ) {
            //Log.d("RegistrationToken","測試4:"+sessionManager.getUserToken())
            //val Registration_Token= RegistrationToken
            apiClient?.postFirebaseCloudMessagingToken(
                token = "Bearer ${sessionManager.getUserToken()}",
                registration_token = registrationToken
            )?.enqueue {
                onResponse = {
                    if (it.isSuccessful) {
                        val RegistrationTokenResponse = it.body()
                        if (RegistrationTokenResponse != null) {
                            handler.post {
                                onSuccess(RegistrationTokenResponse)
                            }
                        }
                    } else {
                        handler.post {
                            Log.e("onResponse", it.message().toString())
                            onFail(it.code().toString())
                        }
                    }
                }
                onFailure = {
                    handler.post {
                        Log.e("onFailure", it!!.message.toString())
                        onFail("網路錯誤，請確認網路連線是否正常，或是該帳號已經綁定過")
                    }
                }
            }
        }

        suspend fun uploadVideo(
            sessionManager: SessionManager,
            video: File,
            onSuccess: () -> Unit,
            onFail: (errorMessage: String) -> Unit
        ) {
            val requestFile =video.asRequestBody("video/mp4".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("video", video.name, requestFile)
            apiClient?.uploadVideo("Bearer ${sessionManager.getUserToken()}", body)?.enqueue {
                onResponse = {
                    if (it.isSuccessful) {
                        it.body()?.let {
                                onSuccess()
                        }
                        Log.e("uploadVideo", "Successful:空回應")
                        //onSuccess()
                    } else {
                        handler.post {
                            Log.e("uploadVideo", "Error:"+it.message().toString())
                            onFail(it.message())
                        }
                    }
                }
                onFailure = {
                    handler.post {
                        Log.e("uploadVideo onFailure", it!!.message.toString())
                        onFail("網路錯誤，請確認網路連線是否正常，或是該帳號已經綁定過")
                    }
                }
            }
        }
        fun getVideoList(
            userToken: String,
            onSuccess: (list: VideoListResponse) -> Unit,
            onError: (errorCode: String) -> Unit,
            onInternetError: (errorMessage: String) -> Unit
        ) {
            apiClient?.getVideoList(
                token = "Bearer $userToken"
            )?.enqueue {
                onResponse = {
                    if (it.isSuccessful) {
                        Log.d("ApiUSR", it.body().toString())
                        handler.post {
                            val videoListResponse = it.body()
                            if (videoListResponse != null) {
                                onSuccess(videoListResponse)
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
        fun shopping(
            userToken: String,
            shoppingInformation: ShoppingImformations,
            onSuccess: (ShoppingResponse) -> Unit,
            onError: (errorCode: String) -> Unit,
            onInternetError: (errorMessage: String) -> Unit
        ) {
            apiClient?.shopping(
                token = "Bearer $userToken",
                shoppingInformation
            )?.enqueue {
                onResponse = {
                    if (it.isSuccessful) {
                        Log.d("ApiUSR", it.body().toString())
                        handler.post {
                            val ShoppingResponse = it.body()
                            if (ShoppingResponse != null) {
                                onSuccess(ShoppingResponse)
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

        fun getItemsPrice(
            userToken: String,
            onSuccess: (list: getItemsPriceResponse) -> Unit,
            onError: (errorCode: String) -> Unit,
            onInternetError: (errorMessage: String) -> Unit
        ) {
            apiClient?.getItemsPrice(
                token = "Bearer $userToken"
            )?.enqueue {
                onResponse = {
                    if (it.isSuccessful) {
                        Log.d("ApiUSR", it.body().toString())
                        handler.post {
                            val getItemsPriceResponse = it.body()
                            if (getItemsPriceResponse != null) {
                                onSuccess(getItemsPriceResponse)
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
    }
}