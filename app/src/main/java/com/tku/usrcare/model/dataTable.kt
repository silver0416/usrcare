package com.tku.usrcare.model

import com.google.gson.annotations.SerializedName


data class Login(
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String,
)

data class LoginResponse(
    @SerializedName("user_token")
    val token: String,
    @SerializedName("name")
    val name: String?,
    @SerializedName("OTP")
    val otp: String?,
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


data class EmailAccountListResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("user_token")
    val userToken: String?,
    @SerializedName("username")
    val username: String?,
    @SerializedName("users")
    val users: Array<SimpleUserObject>?,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EmailAccountListResponse

        if (status != other.status) return false
        if (userToken != other.userToken) return false
        if (!users.contentEquals(other.users)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = status.hashCode()
        result = 31 * result + userToken.hashCode()
        result = 31 * result + users.contentHashCode()
        return result
    }
}

data class SimpleUserObject(
    @SerializedName("user_token")
    val userToken: String,
    @SerializedName("username")
    val username: String,
)

data class MoodTime(
    @SerializedName("time")
    val moodTime: String
)

data class PointsResponse(
    @SerializedName("points")
    val points: Int
)

data class PointsDeduction(
    @SerializedName("time")
    val time: String,
    @SerializedName("deduction_type")
    val deductionType: Int,
    @SerializedName("deduction_amount")
    val deductionAmount: Int,
)

data class Version(
    @SerializedName("version")
    val version: String
)

data class ResetPassword(
    @SerializedName("OTP")
    val otp: String,
    @SerializedName("new_password")
    val newPassword: String,
    @SerializedName("salt")
    val salt: String,
)

data class AccountOtp(
    @SerializedName("user_token")
    val userToken: String,
)

data class CheckInRecordResponse(
    @SerializedName("checkin_dates")
    val checkInTime: Array<String>,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CheckInRecordResponse

        if (!checkInTime.contentEquals(other.checkInTime)) return false

        return true
    }

    override fun hashCode(): Int {
        return checkInTime.contentHashCode()
    }
}

data class HistoryStoryResponse(
    @SerializedName("date")
    val date: String,
    @SerializedName("detail")
    val detail: String,
    @SerializedName("event")
    val event: String,
    @SerializedName("title")
    val title: String,
)

