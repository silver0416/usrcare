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

data class AlarmItem(
    @SerializedName("type")
    val type: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("requestId")
    val requestId: Int,

    @SerializedName("hour")
    val hour: Int,

    @SerializedName("minute")
    val minute: Int,

    @SerializedName("weekdays")
    val weekdays: List<Int>,

    @SerializedName("isActive")
    val isActive: Boolean
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
    @SerializedName("id_token")
    val idToken: String?,
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
    val salt: String?
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

data class VocabularyResponse(
    @SerializedName("chinese")
    val chinese: String,
    @SerializedName("english")
    val english: String,
    @SerializedName("phonetic_notation")
    val phoneticNotation: String,
)

data class CheatResponse(
    @SerializedName("increments")
    val cheat: String,
    @SerializedName("points")
    val points: Int,
)

data class MoodPuncher(
    @SerializedName("typewriter")
    val moodText: String,
)

data class MoodPuncherResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("negative_score")
    val negativeScore: Int,
    @SerializedName("positive_score")
    val positiveScore: Int,
    @SerializedName("SRS")
    val srs: Int
)

data class MoodPuncherSave(
    val dateTime: String,
    val moodText: String,
    val positiveScore: Int,
    val negativeScore: Int,
    val srs: Int
)

data class JwtToken(
    @SerializedName("id_token")
    val idToken: String
)

data class JwtTokenResponse(
    @SerializedName("name")
    val name: String?,
    @SerializedName("user_token")
    val userToken: String?,
    @SerializedName("error")
    val error: String?
)

data class BindingResponse(
    @SerializedName("state")
    val state: String?,
    @SerializedName("exist")
    val exist: String?,
)

data class ReBinding(
    @SerializedName("id_token")
    val idToken: String?,
    @SerializedName("old_userID")
    val oldUserID: String?,
)

data class ReBindingResponse(
    @SerializedName("state")
    val state: String,
)

data class OAuthCheckResponse(
    @SerializedName("Google")
    val google: Boolean,
    @SerializedName("LINE")
    val line: Boolean,
)

data class OAuthUnbindResponse(
    @SerializedName("state")
    val state: String,
)


data class SudokuPuzzleData(
    @SerializedName("win_flag")
    val winFlag: Boolean,
    @SerializedName("diff")
    val diff: String,
    @SerializedName("sudokuPuzzle")
    val sudokuPuzzle: Array<Array<Int>>,
    @SerializedName("timerInterval")
    val timerInterval: Int,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SudokuPuzzleData

        if (winFlag != other.winFlag) return false
        if (diff != other.diff) return false
        if (!sudokuPuzzle.contentDeepEquals(other.sudokuPuzzle)) return false
        return timerInterval == other.timerInterval
    }

    override fun hashCode(): Int {
        var result = winFlag.hashCode()
        result = 31 * result + diff.hashCode()
        result = 31 * result + sudokuPuzzle.contentDeepHashCode()
        result = 31 * result + timerInterval
        return result
    }
}

data class BroadcastData(
    @SerializedName("type")
    val type: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("time")
    val time: String,
    @SerializedName("action")
    val action: String
)


