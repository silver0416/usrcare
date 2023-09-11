package com.tku.usrcare.model

import com.google.gson.annotations.SerializedName


data class Login(
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String
)

data class LoginResponse(
    @SerializedName("user_token")
    val token: String,
    @SerializedName("name")
    val name: String,
)


data class ClockData(
    val id: Int,
    val title: String,
    val detail: String,
    val time: String,
    val week: MutableList<Boolean>,
    val switch: Boolean
)

data class Question(
    @SerializedName("ques")
    val ques: String,
    @SerializedName("ans")
    val ans: List<Int>
)

data class Scale(
    @SerializedName("sheet_title")
    val sheetTitle: String,
    @SerializedName("special_option")
    val specialOption: Map<String, String>,
    @SerializedName("questions")
    val questions: List<Question>
)

data class Sheets(
    @SerializedName("listID")
    val sheetId: Int,
    @SerializedName("name")
    val sheetTitle: String,
)

data class ScaleListResponse(
    @SerializedName("sheets")
    val sheets: List<Sheets>
)

data class EmailCheckResponse(
    @SerializedName("exist")
    val exist: Boolean
)

data class EmailVerify(
    @SerializedName("email")
    val email: String,
    @SerializedName("OTP")
    val OTP: String
)

data class EmailVerifyResponse(
    @SerializedName("success")
    val success: Boolean
)

data class UsernameCheckResponse(
    @SerializedName("exist")
    val exist: Boolean
)


data class RegisterAccount(
    @SerializedName("username")
    val username: String?,
    @SerializedName("password")
    val password: String?,
    @SerializedName("salt")
    val salt: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("gender")
    val gender: String?,
    @SerializedName("birthday")
    val birthday: String?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("city")
    val city: String?,
    @SerializedName("district")
    val district: String?,
    @SerializedName("neighbor")
    val neighbor: String?,
    @SerializedName("address")
    val address: String?,
    @SerializedName("EName")
    val eName: String?,
    @SerializedName("EPhone")
    val ePhone: String?,
    @SerializedName("ERelation")
    val eRelation: String?,
)

data class RegisterAccountResponse(
    @SerializedName("user_token")
    val token: String,
    @SerializedName("error")
    val error: String
)

data class ReturnSheet(
    @SerializedName("answer")
    val answer: Array<String>,
    @SerializedName("start_time")
    val startTime: String,
    @SerializedName("end_time")
    val endTime: String,
)

data class ReturnSheetResponse(
    @SerializedName("success")
    val success: Boolean
)

data class SaltResponse(
    @SerializedName("salt")
    val salt: String
)