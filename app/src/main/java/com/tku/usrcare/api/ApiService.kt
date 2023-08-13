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
    @GET(Constants.TEST_URL)
    fun getTest(@Header("Authorization") token: String): Call<Void>

    @Headers("Content-Type:application/json")
    @POST(Constants.LOGIN_URL)
    fun postLogin(
        @Header("Authorization") token: String,
        @Body login: Login
    ): Call<LoginResponse>

    @Headers("Content-Type:application/json")
    @POST(Constants.AUTHENTICATION_URL)
    fun postAuthorization(
        @Header("Authorization") token: String,
        @Body authorization: Authorization
    ): Call<AuthorizationResponse>

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


}

