package com.tku.usrcare.model

import androidx.compose.runtime.MutableState
import com.google.gson.annotations.SerializedName

data class UserInfo(
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("re_password")
    val re_password: String,
    @SerializedName("email")
    val email: String
)

data class Login(
    @SerializedName("phone")
    val phone: String
)
data class LoginResponse(
    @SerializedName("OTP")
    var OTP: String,
    @SerializedName("status")
    var status: String
)

data class Authorization(
    @SerializedName("phone")
    val phone: String,
    @SerializedName("OTP")
    val OTP: String
)
data class AuthorizationResponse(
    @SerializedName("user_token")
    val token: String,
    @SerializedName("error")
    val error: String
)

data class ClockData(
    val title: String,
    val detail: String,
    val time: String,
    val week: MutableList<Boolean>,
    val switch: Boolean
)