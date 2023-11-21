package com.tku.usrcare.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tku.usrcare.api.ApiUSR
import com.tku.usrcare.model.HistoryStoryResponse
import com.tku.usrcare.model.MoodTime
import com.tku.usrcare.repository.SessionManager
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class MainViewModel(private val sessionManager: SessionManager) : ViewModel() {
    val mSessionManager = sessionManager
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
    val historyStoryComplete = MutableLiveData<Boolean>(false)
    val vocabularyComplete = MutableLiveData<Boolean>(false)

    fun getPoints() {
        viewModelScope.launch {
            ApiUSR.getPoints(
                userToken = sessionManager.getUserToken().toString(),
                onSuccess = {
                    sessionManager.savePoints(it)
                    points.value = it.toString()
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

    fun addSignedDateTime(mood: Int) {
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
                    isDailySignInDialogShow.value = false
                },
                onError = {
                    showAlertDialogEvent.value = it
                    isDailySignInDialogShow.value = false
                },
                onInternetError = {
                    showAlertDialogEvent.value = it
                    isDailySignInDialogShow.value = false
                }
            )
        }
    }

    fun postCheat() {
        viewModelScope.launch {
            ApiUSR.getCheat(
                sessionManager,
                onSuccess = {
                    points.value = it.toString()
                    sessionManager.savePoints(it)
                    postComplete.value = true
                },
                onFail = {
                    showAlertDialogEvent.value = it
                },
            )
        }
    }

    fun checkPoints() {
        viewModelScope.launch {
            points.value = sessionManager.getPoints().toString()
        }
    }


    fun getHistoryStory() {
        viewModelScope.launch {
            ApiUSR.getHistoryStory(
                sessionManager,
                onSuccess = {
                    historyStoryComplete.value = true
                },
                onFail = {
                    if (it == "400") {
                        sessionManager.saveHistoryStory(
                            HistoryStoryResponse(
                                "",
                                "今天沒有歷史故事",
                                "",
                                "歷史上的今天"
                            )
                        )
                        historyStoryComplete.value = true
                    } else {
                        showAlertDialogEvent.value = it
                    }
                },
            )
        }
    }

    fun getVocabulary() {
        viewModelScope.launch {
            ApiUSR.getVocabulary(
                sessionManager,
                onSuccess = {
                    vocabularyComplete.value = true
                },
                onFail = {
                    if (it == "400") {
                        sessionManager.saveVocabulary(
                            com.tku.usrcare.model.VocabularyResponse(
                                "今天沒有英文單字!",
                                "no vocabulary today!",
                            )
                        )
                        vocabularyComplete.value = true
                    } else {
                        showAlertDialogEvent.value = it
                    }
                },
            )
        }
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

    fun isOAuthCheck(): Boolean {
        return sessionManager.getIsOAuthCheck()
    }
}