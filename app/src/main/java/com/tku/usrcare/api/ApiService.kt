package com.tku.usrcare.api

import com.tku.usrcare.Constants
import com.tku.usrcare.model.*

import retrofit2.Call
import retrofit2.Response
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
    @POST(Constants.AUTHANTICATION_URL)
    fun postAuthorization(
        @Header("Authorization") token: String,
        @Body authorization: Authorization
    ): Call<AuthorizationResponse>



}

