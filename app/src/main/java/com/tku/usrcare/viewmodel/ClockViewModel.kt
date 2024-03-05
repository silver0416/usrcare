package com.tku.usrcare.viewmodel

import android.content.SharedPreferences
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tku.usrcare.model.AlarmItem
import com.tku.usrcare.repository.SessionManager
import kotlinx.coroutines.launch

class ClockViewModel(private val sessionManager: SessionManager) : ViewModel() {
    val mSessionManager = sessionManager

    val callUpdatePresetNameList : SingleLiveEvent<Boolean> = SingleLiveEvent()
    val callCloseAllItem : SingleLiveEvent<Boolean> = SingleLiveEvent()
    val callOpenBottomSheet : SingleLiveEvent<Boolean> = SingleLiveEvent()

    private val _reminder = MutableLiveData<MutableList<AlarmItem>?>()
    val reminderList: LiveData<MutableList<AlarmItem>?> = _reminder

    private val preferencesChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == "reminderList") {
            _reminder.value = sessionManager.getReminderList() // 當SharedPreferences中的reminder值變化時，更新LiveData的值。
        }
    }

    init {
        sessionManager.registerOnSharedPreferenceChangeListener(preferencesChangeListener)
    }

    override fun onCleared() {
        super.onCleared()
        sessionManager.unregisterOnSharedPreferenceChangeListener(preferencesChangeListener)
    }


    fun addPresetName(name: String, title: String) {
        when (title) {
            "\uD83D\uDC8A\t用藥提醒" -> {
                sessionManager.addDrugReminderPresetName(name)
            }

            "\uD83C\uDFC3\u200D♂\uFE0F\t活動提醒" -> {
                sessionManager.addActivityReminderPresetName(name)
            }

            "\uD83D\uDCA7\t喝水提醒" -> {
                // do nothing
            }

            "\uD83D\uDCA4\t休息提醒" -> {
                sessionManager.addSleepReminderPresetName(name)
            }

            else -> {
                // do nothing
            }
        }
        callUpdatePresetNameList.postValue(true)
    }

    fun addReminder(reminder: AlarmItem) {
        sessionManager.addReminder(reminder)
    }

    fun getReminderList(): MutableList<AlarmItem>? {
        return sessionManager.getReminderList()
    }

    fun deleteReminder(alarmId : Int) {
        sessionManager.removeReminderById(alarmId)
    }

    fun updateWeekdays(alarmId: Int, newWeekDays: List<Int>) {
        viewModelScope.launch {
            val reminderList = sessionManager.getReminderList()
            reminderList.forEach {
                if (it.requestId == alarmId) {
                    val newReminder = AlarmItem(
                        it.type,
                        it.description,
                        it.requestId,
                        it.hour,
                        it.minute,
                        newWeekDays,
                        it.isActive
                    )
                    reminderList.remove(it)
                    reminderList.add(newReminder)
                }
            }
        }
    }

    fun updateReminderIsActive(alarmId: Int, isActive: Boolean) {
        viewModelScope.launch {
            sessionManager.updateReminderIsActiveById(alarmId, isActive)
        }
    }

    fun updateReminderTimes(alarmId: Int, hour: Int, minute: Int) {
        viewModelScope.launch {
            val reminderList = sessionManager.getReminderList()
            reminderList.forEach {
                if (it.requestId == alarmId) {
                    val newReminder = AlarmItem(
                        it.type,
                        it.description,
                        it.requestId,
                        hour,
                        minute,
                        it.weekdays,
                        it.isActive
                    )
                    reminderList.remove(it)
                    reminderList.add(newReminder)
                }
            }
        }
    }

    companion object {
        fun getReminderPresetList(clockViewModel: ClockViewModel, name: String): MutableState<List<String>> {
            when (name) {
                "\uD83D\uDC8A\t用藥提醒" -> {
                    return mutableStateOf(clockViewModel.sessionManager.getDrugReminderPresetNameList())
                }
    
                "\uD83C\uDFC3\u200D♂\uFE0F\t活動提醒" -> {
                    return mutableStateOf(clockViewModel.sessionManager.getActivityReminderPresetNameList())
                }
    
                "\uD83D\uDCA7\t喝水提醒" -> {
                    return mutableStateOf(mutableListOf())
                }
    
                "\uD83D\uDCA4\t休息提醒" -> {
                    return mutableStateOf(clockViewModel.sessionManager.getSleepReminderPresetNameList())
                }
    
                else -> {
                    return mutableStateOf(mutableListOf())
                }
            }
        }
    }
}