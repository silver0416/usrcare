package com.tku.usrcare.api

import com.tku.usrcare.Constants
import com.tku.usrcare.model.*

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    companion object {
        fun getApi(): ApiService? {
            return ApiClient.client?.create(ApiService::class.java)
        }
    }

    @Headers("Content-Type:application/json")
    @POST(Constants.TEST_URL)
    fun postTest(
        @Header("Authorization") token: String,
        @Body version: Version
    ): Call<Void>

    @Headers("Content-Type:application/json")
    @POST(Constants.LOGIN_URL)
    fun postLogin(
        @Header("Authorization") token: String,
        @Body login: Login
    ): Call<LoginResponse>

    @Headers("Content-Type:application/json")
    @GET(Constants.SCALE_LIST_URL)
    fun getScaleList(
        @Header("Authorization") token: String
    ): Call<ScaleListResponse>

    @Headers("Content-Type:application/json")
    @GET(Constants.SCALE_URL)
    fun getScale(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<Scale>

    @Headers("Content-Type:application/json")
    @GET(Constants.EMAIL_CHECk_URL)
    fun getEmailCheck(
        @Header("Authorization") token: String,
        @Path("email") email: String
    ): Call<EmailCheckResponse>

    @Headers("Content-Type:application/json")
    @POST(Constants.EMAIL_VERIFY_URL)
    fun postEmailVerify(
        @Header("Authorization") token: String,
        @Body emailVerify: EmailVerify
    ): Call<EmailVerifyResponse>

    @Headers("Content-Type:application/json")
    @GET(Constants.USERNAME_CHECK_URL)
    fun getUsernameCheck(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): Call<UsernameCheckResponse>

    @Headers("Content-Type:application/json")
    @POST(Constants.REGISTER_URL)
    fun postRegister(
        @Header("Authorization") token: String,
        @Body registerAccount: RegisterAccount
    ): Call<RegisterAccountResponse>

    @Headers("Content-Type:application/json")
    @POST(Constants.RETURN_SHEET_URL)
    fun postSheetResult(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body returnSheet: ReturnSheet
    ): Call<ReturnSheetResponse>

    @Headers("Content-Type:application/json")
    @GET(Constants.SALT_URL)
    fun getSalt(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): Call<SaltResponse>


    @Headers("Content-Type:application/json")
    @GET(Constants.GET_EMAIL_ACCOUNT_LIST_URL)
    fun getEmailAccountList(
        @Header("Authorization") token: String,
        @Path("email") email: String
    ): Call<EmailAccountListResponse>

    @Headers("Content-Type:application/json")
    @POST(Constants.MOOD_URL)
    fun postMood(
        @Header("Authorization") token: String,
        @Path("mood") mood: String,
        @Body moodTime: MoodTime
    ): Call<Void>

    @Headers("Content-Type:application/json")
    @GET(Constants.POINTS_URL)
    fun getpoints(
        @Header("Authorization") token: String
    ): Call<PointsResponse>

    @Headers("Content-Type:application/json")
    @POST(Constants.POINTS_DEDUCTION_URL)
    fun postPointsDeduction(
        @Header("Authorization") token: String,
        @Body pointsDeduction: PointsDeduction
    ): Call<Void>

    @Headers("Content-Type:application/json")
    @POST(Constants.RESET_PASSWORD_URL)
    fun postResetPassword(
        @Header("Authorization") token: String,
        @Body resetPassword: ResetPassword
    ): Call<Void>

    @Headers("Content-Type:application/json")
    @POST(Constants.OTP_URL)
    fun postAccountOtpCheck(
        @Header("Authorization") token: String,
        @Body accountOtp: AccountOtp
    ): Call<Void>

    @Headers("Content-Type:application/json")
    @GET(Constants.CHECKING_RECORD_URL)
    fun getCheckInRecord(
        @Header("Authorization") token: String
    ): Call<CheckInRecordResponse>

    @Headers("Content-Type:application/json")
    @GET(Constants.HISTORY_STORY_URL)
    fun getHistoryStory(
        @Header("Authorization") token: String
    ): Call<HistoryStoryResponse>

    @Headers("Content-Type:application/json")
    @GET(Constants.VOCABULARY_URL)
    fun getVocabulary(
        @Header("Authorization") token: String
    ): Call<VocabularyResponse>

    @Headers("Content-Type:application/json")
    @GET(Constants.CHEAT_URL)
    fun getCheat(
        @Header("Authorization") token: String
    ): Call<CheatResponse>
}

