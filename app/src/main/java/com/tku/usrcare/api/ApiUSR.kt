package com.tku.usrcare.api

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.tku.usrcare.databinding.ActivityMainBinding
import com.tku.usrcare.view.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiUSR {
    companion object {
        private val apiClient: ApiService? = ApiClient.client?.create(ApiService::class.java)
        var handler: Handler = Handler(Looper.getMainLooper())

        fun getTest(activity: MainActivity, binding: ActivityMainBinding ,token: String) {
            apiClient?.getTest(token = "Bearer $token")
                ?.enqueue {
                    onResponse = {
                        if (it.isSuccessful) {
                            handler.post {

                            }
                        } else {
                            handler.post {

                            }
                        }
                    }
                    onFailure = {
                        handler.post {
                            Log.e("onFailure", it!!.message.toString())

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

    }
}