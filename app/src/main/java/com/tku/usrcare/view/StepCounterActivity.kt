package com.tku.usrcare.view

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tku.usrcare.view.ui.petcompany.updateCoin

class StepCounterActivity :AppCompatActivity(),SensorEventListener{
    var sensorManager: SensorManager? = null
    var stepCounterSensor: Sensor? = null

    var stepCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager?
        stepCounterSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    }
    override fun onResume(){
        super.onResume()
        sensorManager?.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }
    override fun onPause(){
        super.onPause()
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent){
        if(event.sensor.type == Sensor.TYPE_STEP_COUNTER){
            stepCount = event.values[0].toInt()
            updateStepCount(stepCount)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int){
        //do nothing
    }

    fun updateStepCount(stepCount:Int){
        updateCoin(stepCount)
    }
}
