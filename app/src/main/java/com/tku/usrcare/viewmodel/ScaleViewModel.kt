package com.tku.usrcare.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tku.usrcare.model.MoodPuncherSave
import com.tku.usrcare.repository.SessionManager
import java.text.SimpleDateFormat
import java.util.Locale

class ScaleViewModel(private val sessionManager: SessionManager) : ViewModel() {
    val moodNowText = MutableLiveData<String>("")
    val timeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.TAIWAN)
    val moodPuncherList = MutableLiveData<ArrayList<MoodPuncherSave>?>()
    val recognizedText = MutableLiveData<String>("")

    fun saveMoodNowText(text: String) {
        moodNowText.value = text
    }

    fun getMoodNowText(): String {
        Log.d("ScaleViewModel", "getMoodNowText: ${moodNowText.value.toString()}")
        return moodNowText.value.toString()
    }

    fun addMoodPuncherList(moodPuncherSave: MoodPuncherSave) {
        sessionManager.addMoodPuncher(
            moodPuncherSave
        )
        getMoodPuncherList()
    }

    fun getMoodPuncherList(): ArrayList<MoodPuncherSave>? {
        moodPuncherList.value = sessionManager.getMoodPuncher()
        return sessionManager.getMoodPuncher()
    }

}