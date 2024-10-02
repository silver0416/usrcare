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
    val steps = MutableLiveData<Int>(0)
}
