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



}

