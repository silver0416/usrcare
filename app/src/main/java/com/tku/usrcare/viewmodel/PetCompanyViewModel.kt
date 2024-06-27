package com.tku.usrcare.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tku.usrcare.api.ApiUSR
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.PetCompanyActivity
import kotlinx.coroutines.launch
//全抄SettingViewModel，這裡好像是多餘的
class PetCompanyViewModel (private val sessionManager: SessionManager) : ViewModel(){
    val showAlertDialogEvent = SingleLiveEvent<String>()
    val finishUnbind = SingleLiveEvent<Boolean>()
    val steps = MutableLiveData<Int>(0)
    /*
    fun getSessionManager(): SessionManager {
        return sessionManager
    }

    fun getOAuthCheck() {
        viewModelScope.launch {
            ApiUSR.getOAuthCheck(
                sessionManager,
                onSuccess = {
                    sessionManager.saveOAuthCheck(it)
                    sessionManager.saveIsOAuthCheck(true)
                },
                onFail = {
                    showAlertDialogEvent.value = it
                },
            )
        }
    }

    fun deleteOAuth(oauthType: String) {
        viewModelScope.launch {
            ApiUSR.deleteOauthUnbind(
                sessionManager,
                oauthType,
                onSuccess = {
                    sessionManager.saveIsOAuthCheck(false)
                    getOAuthCheck()
                    finishUnbind.value = true
                },
                onFail = {
                    showAlertDialogEvent.value = it
                },
            )
        }
    }

    fun getUserToken(): String? {
        return sessionManager.getUserToken()
    }
    */
}
