package com.tku.usrcare.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tku.usrcare.api.ApiUSR
import com.tku.usrcare.repository.SessionManager
import kotlinx.coroutines.launch

class MainFragmentViewModel : ViewModel() {
    val userName = MutableLiveData<String>()
    val userToken = MutableLiveData<String>()
    val points = MutableLiveData<String>("載入中...")
    val pointsFailResultType = MutableLiveData<String>()


    fun getUserName(sessionManager: SessionManager) : MutableLiveData<String> {
        // Your logic here
        userName.value = sessionManager.getUserName()
        return userName
    }

    fun getUserToken(sessionManager: SessionManager): String {
        userToken.value = sessionManager.getUserToken()
        return userToken.value.toString()
    }

    fun getPoints(sessionManager: SessionManager) {
        viewModelScope.launch {
            ApiUSR.getPoints(
                userToken = sessionManager.getUserToken().toString(),
                onSuccess = {
                    points.value = it.toString()
                    pointsFailResultType.value = ""
                },
                onError = {
                    points.value = ""
                    pointsFailResultType.value = it
                },
                onFailureError = {
                    points.value = ""
                    pointsFailResultType.value = it
                }
            )
        }
    }
}