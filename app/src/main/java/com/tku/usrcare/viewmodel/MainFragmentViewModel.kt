package com.tku.usrcare.viewmodel

import android.content.BroadcastReceiver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tku.usrcare.api.ApiUSR
import com.tku.usrcare.model.MoodTime
import com.tku.usrcare.repository.SessionManager
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class MainFragmentViewModel(private val sessionManager: SessionManager) : ViewModel() {
    val userName = sessionManager.getUserName()
    val userToken = sessionManager.getUserToken()
    val points = MutableLiveData<String>("載入中...")
    val timeFormat = SimpleDateFormat("yyyy-MM-dd", Locale.TAIWAN)
    val timeFormatChinese = SimpleDateFormat("MM月dd日(E)", Locale.TAIWAN)
    val today: String = timeFormat.format(System.currentTimeMillis())
    val signedDateTimeList = sessionManager.getSignedDateTime()
    val showAlertDialogEvent = SingleLiveEvent<String>()
    val isDailySignInDialogShow = SingleLiveEvent<Boolean>()
    val postComplete = SingleLiveEvent<Boolean>()
    private lateinit var pointsUpdateReceiver: BroadcastReceiver

    fun getPoints() {
        viewModelScope.launch {
            ApiUSR.getPoints(
                userToken = sessionManager.getUserToken().toString(),
                onSuccess = {
                    points.value = it.toString()
                    postComplete.value = true
                },
                onError = {
                    points.value = ""
                    showAlertDialogEvent.value = it
                },
                onInternetError = {
                    points.value = ""
                    showAlertDialogEvent.value = it
                }
            )
        }
    }

    fun isSignedToday(): Boolean {
        for (i in signedDateTimeList) {
            //將i轉換成日期格式
            val signedDateTime = timeFormat.parse(i[0])
            val signedDate = signedDateTime?.let { timeFormat.format(it) }
            if (signedDate == today) {
                return true
            }
        }
        return false
    }
    fun addSignedDateTime(mood : Int) {
        //檢查是否已經簽到
        if (isSignedToday()) {
            return
        }
        //將簽到時間加入清單
        sessionManager.addSignedDateTime(today, mood)
    }

    fun postMood(mood: Int, moodTime: MoodTime) {
        viewModelScope.launch {
            ApiUSR.postMood(
                sessionManager,
                mood = mood.toString(),
                moodTime = moodTime,
                onSuccess = {
                    addSignedDateTime(mood)
                    getPoints()
                },
                onError = {
                    showAlertDialogEvent.value = it
                },
                onInternetError = {
                    showAlertDialogEvent.value = it
                }
            )
            isDailySignInDialogShow.value = false
        }
    }

}