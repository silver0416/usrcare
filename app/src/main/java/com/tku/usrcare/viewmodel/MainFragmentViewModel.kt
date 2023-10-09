package com.tku.usrcare.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tku.usrcare.api.ApiUSR
import com.tku.usrcare.repository.SessionManager
import kotlinx.coroutines.launch

class MainFragmentViewModel(private val sessionManager: SessionManager) : ViewModel() {
    val userName = sessionManager.getUserName()
    val userToken = sessionManager.getUserToken()
    val points = MutableLiveData<String>("載入中...")
    val pointsFailResultType = MutableLiveData<String>()

    fun getPoints() {
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