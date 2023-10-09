package com.tku.usrcare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tku.usrcare.repository.SessionManager

class ViewModelFactory(
    private val sessionManager: SessionManager
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(SessionManager::class.java).newInstance(sessionManager)
    }
}
