package com.tku.usrcare.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.tku.usrcare.view.StepCounterActivity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PetCompanyViewModel (application: Application)  : AndroidViewModel(application) {
    private val stepCounter = StepCounterActivity(application)

    val stepCount = stepCounter.stepCount.asFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2000),
            initialValue = 0
        )
    init {
        startCounting()
    }

    private fun startCounting() {
        stepCounter.start()
    }

    override fun onCleared() {
        super.onCleared()
        stepCounter.stop()
    }
}
