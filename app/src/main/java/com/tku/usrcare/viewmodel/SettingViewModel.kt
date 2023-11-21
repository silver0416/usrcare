package com.tku.usrcare.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tku.usrcare.api.ApiUSR
import com.tku.usrcare.repository.SessionManager
import kotlinx.coroutines.launch

class SettingViewModel(private val sessionManager: SessionManager) : ViewModel() {
    val showAlertDialogEvent = SingleLiveEvent<String>()

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

    fun getUserToken(): String? {
        return sessionManager.getUserToken()
    }
}