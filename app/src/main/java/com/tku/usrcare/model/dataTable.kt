package com.tku.usrcare.model

import com.google.gson.annotations.SerializedName


data class Login(
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String,
)

data class LoginResponse(
    @SerializedName("user_token")//json Field
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
    @SerializedName("consultation")
    val consultation: Boolean
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
    @SerializedName("mood")
    val mood: String,
    @SerializedName("score")
    val score: Int,
    @SerializedName("property")
    val property: String,
    @SerializedName("suggestion")
    val suggestion: String
)

data class MoodPuncherSave(
    val dateTime: String,
    val moodText: String,
    val moodResponse : String,
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


//數獨
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
    val action: String,
    @SerializedName("url")
    val url: String?,
)


data class SportVideoUploadResponse(
    @SerializedName("video_upload_response")
    val videoResponse: String?,
)

data class RegistrationToken(
    @SerializedName("registration_token")
    val registration_token: String,
)

data class RegistrationTokenResponse(
    @SerializedName("state")
    val state: String,
)

data class getItemsResponse(
    @SerializedName("items")
    val items: List<item>,
)

data class item(
    @SerializedName("categoryID")
    val categoryID: Int,
    @SerializedName("image_path")
    val image_path: String,
    @SerializedName("itemID")
    val itemID: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("value")
    val value: Int,
    @SerializedName("price")
    val price: Int,
)


data class VideoList(
    @SerializedName("analyzed")
    val analyzed: Boolean,
    @SerializedName("expired")
    val expired: Boolean,
    @SerializedName("expiry_time")
    val expiry_time: String,
    @SerializedName("upload_time")
    val upload_time: String,
    @SerializedName("url")
    val url: String?,
    @SerializedName("videoID")
    val videoID: String?,
    @SerializedName("score")
    val score: Float?,
)

data class VideoListResponse(
    @SerializedName("list")
    val list: List<VideoList>,
)

data class ShoppingImformations(
    @SerializedName("itemID")
    val itemID: Int,//買什麼
    @SerializedName("quantity")
    val quantity: Int,//買多少
)

data class webGameRecord(
    @SerializedName("game")
    val game: String,
    @SerializedName("level")
    var level: Int?,
    @SerializedName("start_time")
    val start_time: String?,
    @SerializedName("end_time")
    val end_time: String?,
    @SerializedName("score")
    val score: Int?,
    @SerializedName("guess_times")
    val guess_times: Int?,
)

data class GameRecordResponse(
    @SerializedName("state")
    val state: String,
)

data class stepRecord(
    @SerializedName("steps")
    val steps: Int,
    @SerializedName("date")
    val date: String,
)

data class stepRecordResponse(
    @SerializedName("state")
    val state: String,
)

data class healthReport(
    @SerializedName("checkin")
    val checkin: checkin,
    @SerializedName("exercise")
    val exercise: exercise,
    @SerializedName("game")
    val game: abilities,
    @SerializedName("mental_record")
    val mental_record: mental_record,
    //@SerializedName("LonelinessScale")
    //val LonelinessScale: List<String>,
    @SerializedName("pet_companion")
    val pet_companion: pet_companion,
    @SerializedName("reportPeriod")
    val reportPeriod: String,
)

data class checkin(
    @SerializedName("average_mood_score")
    val average_mood_score: Float,
    @SerializedName("feedback")
    val feedback: List<String>,
    @SerializedName("mood_difference_from_last_period")
    val mood_difference_from_last_period: Float,
    @SerializedName("mood_trend")
    val mood_trend: String,
)

data class exercise(
    @SerializedName("average_exercise_score")
    val average_exercise_score: Float,
    @SerializedName("exercise_level")
    val exercise_level: String,
    @SerializedName("feedback")
    val feedback: String,
)

data class game(
    @SerializedName("abilities")
    val abilities: abilities,
)

data class abilities(
    @SerializedName("記憶力")
    val 記憶力: Float,
    @SerializedName("反應力")
    val 反應力: Float,
    @SerializedName("邏輯推理能力")
    val 邏輯推理能力: Float,
    @SerializedName("手眼協調與專注力")
    val 手眼協調與專注力: Float,
    @SerializedName("持續力")
    val 持續力: Float,
)
data class mental_record(
    @SerializedName("AD8")
    val AD8: AD8,
    @SerializedName("LonelinessScale")
    val LonelinessScale: LonelinessScale,
)

data class AD8(
    @SerializedName("average_score")
    val average_score: Int,
    @SerializedName("feedback")
    val feedback: String,
)

data class LonelinessScale(
    @SerializedName("average_score")
    val average_score: Float,
    @SerializedName("feedback")
    val feedback: String,
    @SerializedName("mood_level")
    val mood_level: String,
)
data class pet_companion(
    @SerializedName("total_steps")
    val total_steps: Int,
    @SerializedName("average_daily_steps")
    val average_daily_steps: Int,
    @SerializedName("goal_achievement_dates")
    val goal_achievement_dates: List<String>,//格式: MM-DD
    @SerializedName("goal_achievement_days")
    val goal_achievement_days: Int,
    @SerializedName("goal_achievement_rate")
    val goal_achievement_rate: Float,
    @SerializedName("daily_step_goal")
    val daily_step_goal: Int,
    @SerializedName("quarterly_step_goal")
    val quarterly_step_goal: Int,
    @SerializedName("step_difference_from_last_period")
    val step_difference_from_last_period: Int,
    @SerializedName("step_trend")
    val step_trend: String,
    @SerializedName("weekly_steps")
    val weekly_steps: Map<String, weekly_steps>,
    @SerializedName("feedback")
    val feedback: String,
)

data class weekly_steps(
    @SerializedName("total_steps")
    val total_steps: Int,
    @SerializedName("week_end")
    val week_end: String,
    @SerializedName("week_start")
    val week_start: String,
)

//未來可能改成使用者設定
data class postUserSetting(
    @SerializedName("config_key")
    val config_key: String,
    @SerializedName("value")
    val value: Int,
)

data class postUserSettingResponse(
    @SerializedName("state")
    val state: String,
)

data class getUserSettingResponse(
    @SerializedName("pet_companion_pedomoter_goal")
    val pet_companion_pedomoter_goal: Int?,
    val pet_companion_pedomoter_goal_suggestion: Int?,
)

data class getUserInventoryResponse(
    @SerializedName("items")
    val items: Map<String, userItem>?=emptyMap(),
)
data class userItem(
    @SerializedName("itemID")
    val itemID: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("quantity")
    val quantity: Int,
)

data class postUserUseItem(
    @SerializedName("itemID")
    val itemID: Int,//道具OD
    @SerializedName("quantity")
    val quantity: Int,//道具數量
)

data class postUserUseItemResponse(
    @SerializedName("state")
    val state: String,
)


