package com.tku.usrcare.view

import android.app.Activity
import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.tku.usrcare.R
import com.tku.usrcare.api.ApiUSR
import com.tku.usrcare.model.stepRecord
import com.tku.usrcare.model.webGameRecord
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.viewmodel.MainViewModel
import com.tku.usrcare.viewmodel.PetCompanyViewModel
import com.tku.usrcare.viewmodel.ViewModelFactory
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class StepCounterActivity(private val context: Context) : SensorEventListener {

    private val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val stepSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    private val _stepCount = MutableLiveData<Int>()
    val stepCount: LiveData<Int> = _stepCount

    private var initialSteps: Int = 0
    private var lastSavedDate: LocalDate = LocalDate.now()
    private var currentSteps: Int = 0
    private var currentDate: LocalDate = LocalDate.now()

    private val prefs: SharedPreferences = context.getSharedPreferences("DailyStepCounter", Context.MODE_PRIVATE)

    //var systemService: SensorManager? = null;
    fun start() {
        stepSensor?.let { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
            Log.d("StepCounterActivity", "Sensor啟動")
        }
        loadLastSavedSteps()
    }

    fun stop() {
        sensorManager.unregisterListener(this)
        saveCurrentSteps()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        Log.d("StepCounterActivity", "觸發onSensorChanged")
        event?.let {
            if (it.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                updateTodayDate()
                currentSteps = it.values[0].toInt()
                Log.d("StepCounterActivity", "步數:$currentSteps")
                if (isNewDay()) {
                    uploadStepsRecord(currentSteps - (initialSteps ?: 0))
                    Log.d("StepCounterActivity", "已上傳:$currentSteps - $initialSteps")
                    initialSteps = currentSteps
                    //Log.d("StepCounterActivity", "currentSteps:$currentSteps")
                    //Log.d("StepCounterActivity", "_stepCount:"+_stepCount.value)
                    //Log.d("StepCounterActivity", "initialSteps:$initialSteps")
                    //Log.d("StepCounterActivity", "lastSavedDate:$lastSavedDate")
                    lastSavedDate = LocalDate.now()

                    _stepCount.postValue(0)
                    saveCurrentSteps()
                }
                else
                {
                    val todaySteps = currentSteps - (initialSteps ?: 0)
                    Log.d("StepCounterActivity", "todaySteps:$todaySteps")
                    _stepCount.postValue(todaySteps)
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // 這裡用不到
    }

    private fun uploadStepsRecord(steps: Int){

        val date=lastSavedDate.atStartOfDay(ZonedDateTime.now().zone).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        val stepRecord= stepRecord(steps,date)
        ApiUSR.postStepRecord(
            SessionManager(context).getUserToken().toString(),
            stepRecord,
            onSuccess = {
                Log.d("StepCounterActivity", it.toString())
            },
            onError = {
                Log.d("StepCounterActivity", it)
            },
            onInternetError = {
                Log.d("StepCounterActivity", it)
            }
        )
    }

    private fun isNewDay(): Boolean {
        // 解析ISO8601格式的時間
        val today = LocalDate.now()
        if(lastSavedDate.toString()=="")
        {
            currentSteps=0
            lastSavedDate=LocalDate.now()
        }
        //從解析後的時間取出日期
        Log.d("StepCounterActivity", "比較"+today+"和"+lastSavedDate)
        return today != lastSavedDate




        //val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
/*
        val stepRecord= stepRecord(currentSteps,currentDay)
        getTodayDate()
        Log.d("StepCounterActivity", "currentDay:$currentDay")
        Log.d("StepCounterActivity", "lastSavedDay:$lastSavedDay")
        if (currentDay.equals(lastSavedDay).not()) {
            lastSavedDay = currentDay
            //ApiUSR
            /*ApiUSR.postStepRecord(
                SessionManager(context).getUserToken().toString(),
                stepRecord,
                onSuccess = {
                    Log.d("StepCounterActivity", it.toString())
                },
                onError = {
                    Log.d("StepCounterActivity", it)
                },
                onInternetError = {
                    Log.d("StepCounterActivity", it)
                }
            )*/
            Log.d("StepCounterActivity", "觸發isNewDay=true")
            return true
        }
        lastSavedDay = currentDay
        Log.d("StepCounterActivity", "觸發isNewDay=false")
        return false*/
    }

    private fun resetSteps() {
        initialSteps = currentSteps
        _stepCount.postValue(0)
        saveCurrentSteps()
    }

    private fun updateTodayDate() {
        currentDate=LocalDate.now()
    }
    private fun loadLastSavedSteps() {
        try {
            lastSavedDate = LocalDate.parse(prefs.getString("lastSavedDate", LocalDate.now().toString()) ?: LocalDate.now().toString())
        } catch (e: Exception) {
            lastSavedDate = LocalDate.now()
        }
        //Log.d("StepCounterActivity", "$lastSavedDay")
        val savedSteps = prefs.getInt("savedSteps", 0)
        initialSteps = prefs.getInt("initialSteps", 0)
        //currentSteps = prefs.getInt("currentSteps", 0)
        _stepCount.postValue(savedSteps)
    }

    private fun saveCurrentSteps() {
        prefs.edit().apply {
            putString("lastSavedDate", lastSavedDate.toString())
            putInt("savedSteps", _stepCount.value ?: 0)
            putInt("initialSteps", initialSteps)
           //putInt("currentSteps", currentSteps)
            //putString("currentDay", currentDay)
            apply()
        }
    }
}
