package com.tku.usrcare.view

import android.app.Activity
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.tku.usrcare.R
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.viewmodel.MainViewModel
import com.tku.usrcare.viewmodel.PetCompanyViewModel
import com.tku.usrcare.viewmodel.ViewModelFactory

class StepCounterActivity: AppCompatActivity(), SensorEventListener{

    var systemService: SensorManager? = null;
    var senor: Sensor? = null;
    private lateinit var petCompanyViewModel: PetCompanyViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelFactory = ViewModelFactory(SessionManager(this))
        petCompanyViewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[PetCompanyViewModel::class.java]
        initSensor()
    }


    private fun initSensor() {
        systemService = getSystemService(SENSOR_SERVICE) as SensorManager
        senor = systemService!!.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    }

    override fun onResume() {
        super.onResume()
        systemService?.registerListener(this,senor,1000)
    }

    override fun onPause() {
        super.onPause()
        systemService?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val values = event!!.values
        val step = petCompanyViewModel
        Log.d("MainActivity", "values[0]: ${values[0]}")
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Do something
    }
}
