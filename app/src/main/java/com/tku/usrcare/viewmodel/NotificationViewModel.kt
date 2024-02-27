package com.tku.usrcare.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tku.usrcare.model.BroadcastData
import com.tku.usrcare.repository.SessionManager

class NotificationViewModel(private val sessionManager: SessionManager) : ViewModel() {
    val mSessionManager = sessionManager

    private val _notificationList = MutableLiveData<List<BroadcastData>>()
    val notificationList : LiveData<List<BroadcastData>> = _notificationList

    private val preferencesChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == "broadcastDataList") {
            _notificationList.value = sessionManager.getBroadcastDataList()
        }
    }

    val selectedMessage = MutableLiveData<BroadcastData?>()

    init {
        sessionManager.registerOnSharedPreferenceChangeListener(preferencesChangeListener)
    }

    override fun onCleared() {
        super.onCleared()
        sessionManager.unregisterOnSharedPreferenceChangeListener(preferencesChangeListener)
    }


    fun getNotificationList() {
        _notificationList.value = sessionManager.getBroadcastDataList()
    }

    fun deleteNotification(broadcastData: BroadcastData) {
        sessionManager.deleteBroadcastData(broadcastData)
    }

    fun setSelectedMessage(broadcastData: BroadcastData) {
        selectedMessage.value = broadcastData
    }

    fun clearSelectedMessage() {
        selectedMessage.value = null
    }



}