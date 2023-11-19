package com.tku.usrcare.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tku.usrcare.api.ApiUSR
import com.tku.usrcare.model.JwtToken
import com.tku.usrcare.repository.SessionManager

class LoginViewModel(private val sessionManager: SessionManager) : ViewModel() {
    val googleId = MutableLiveData<String>()
    val lineId = MutableLiveData<String>()
    val launchAlertDialog : SingleLiveEvent<String> = SingleLiveEvent()
    val launchLogin : SingleLiveEvent<Boolean> = SingleLiveEvent()
    val launchGoogleRegister : SingleLiveEvent<Boolean> = SingleLiveEvent()
    val launchLineRegister : SingleLiveEvent<Boolean> = SingleLiveEvent()
    fun postGoogleOAuthToken(token: String) {
        val jwtToken = JwtToken(token)
        ApiUSR.postGoogleOAuth(
            jwtToken,
            sessionManager,
            onSuccess = {
                        if (it.userToken != null) {
                            sessionManager.saveUserToken(it.userToken)
                            sessionManager.saveUserName(it.name.toString())
                            launchLogin.postValue(true)
                        } else {
                            launchGoogleRegister.postValue(true)
                        }
            },
            onFail = {
                if (it == "403") {
                    launchAlertDialog.postValue("發生錯誤，請重新操作")
                }
            }
        )
    }

    fun postLineOAuthToken(token: String) {
        val jwtToken = JwtToken(token)
        ApiUSR.postLineOAuth(
            jwtToken,
            sessionManager,
            onSuccess = {
                if (it.userToken != null) {
                    sessionManager.saveUserToken(it.userToken)
                    sessionManager.saveUserName(it.name.toString())
                    launchLogin.postValue(true)
                } else {
                    launchLineRegister.postValue(true)
                }
            },
            onFail = {
                if (it == "403") {
                    launchAlertDialog.postValue("發生錯誤，請重新操作")
                }
            }
        )
    }
}