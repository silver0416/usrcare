package com.tku.usrcare.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.tku.usrcare.R
import com.tku.usrcare.api.ApiUSR
import com.tku.usrcare.model.stepRecord
import com.tku.usrcare.repository.SessionManager
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

class StepCounterService : Service(), SensorEventListener {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sensorManager: SensorManager
    private var todaySteps: Int = 0
    private var totalSteps: Int = 0
    private var recordSteps: Int = 0
    companion object {
        const val REQUEST_CODE_PERMISSIONS = 1001
    }
    override fun onBind(intent: Intent?): IBinder? = null
    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences("step_counter_prefs", Context.MODE_PRIVATE)
        totalSteps = sharedPreferences.getInt("totalSteps", 0)
        recordSteps = sharedPreferences.getInt("recordSteps", 0)
        Log.d("StepCounterService", "onCreate called")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "step_channel",
                "Step Counter Channel",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                setSound(null, null)
                enableVibration(false)
                enableLights(false)
            }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        super.onCreate()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor != null) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("StepCounterService", "onStartCommand called")
        startForegroundService() // 保持服務在前景運行
        return START_STICKY
    }

    private fun startForegroundService() {
        val notification = NotificationCompat.Builder(this, "step_channel")
            .setOnlyAlertOnce(true)
            .setSilent(true)
            .setContentTitle("計步器正在運行")
            .setContentText("今日步數: $todaySteps")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

        startForeground(1, notification)
    }

    private fun uploadStepsRecord(context: Context,steps:Int) {
        try {
            val savedDate = LocalDate.parse(sharedPreferences.getString("recordDate", ""), DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            Log.d("StepCounterService", savedDate)
            val stepRecord = stepRecord(steps, savedDate)
            ApiUSR.postStepRecord(
                SessionManager(context).getUserToken().toString(),
                stepRecord,
                onSuccess = {
                    Log.d("StepCounterService0", it.toString())
                },
                onError = {
                    Log.d("StepCounterService1", it)
                },
                onInternetError = {
                    Log.d("StepCounterService2", it)
                }
            )
        }catch (e:Exception){
            Log.d("StepCounterService", e.toString())
        }
    }
    override fun onSensorChanged(event: SensorEvent?) {
        Log.d("StepCounterService", "todaySteps: $todaySteps")
        Log.d("StepCounterService", "totalSteps: $totalSteps")
        Log.d("StepCounterService", "recordSteps: $recordSteps")
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            if (recordSteps == 0) {
                recordSteps = event.values[0].toInt()
            }

            totalSteps= event.values[0].toInt()
            todaySteps = totalSteps - recordSteps
            if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
                val sharedPreferences =
                    getSharedPreferences("step_counter_prefs", Context.MODE_PRIVATE)
                val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()) // 獲取當前的日期
                Log.d("StepCounterService", currentDate+"||"+sharedPreferences.getString("recordDate", ""))
                if(currentDate!=sharedPreferences.getString("recordDate", ""))
                {
                    Log.d("StepCounterService", (totalSteps - recordSteps).toString())
                    //上傳
                    try {
                        //todaySteps為負數表示手機經過重啟，直接上傳關機前的紀錄
                        if(totalSteps - recordSteps<0){
                            todaySteps=sharedPreferences.getInt("totalSteps", 0)-sharedPreferences.getInt("recordSteps", 0)

                            uploadStepsRecord(steps=todaySteps,context = this)
                            //歸零
                            with(sharedPreferences.edit()) {
                                putInt("recordSteps", 0)
                                putString("recordDate", currentDate)
                                apply()
                            }
                            recordSteps = sharedPreferences.getInt("recordSteps", 0)
                        }
                        else
                        {
                            uploadStepsRecord(steps=todaySteps,context = this)
                            //歸零
                            with(sharedPreferences.edit()) {
                                putInt("recordSteps", totalSteps)
                                putString("recordDate", currentDate)
                                apply()
                            }
                            recordSteps = sharedPreferences.getInt("recordSteps", 0)
                        }
                    }
                    catch (e:Exception){
                        Log.d("StepCounterService", e.toString())
                    }
                }
                // 更新步數與日期並儲存在本地
                with(sharedPreferences.edit()) {
                    putInt("recordSteps", recordSteps)
                    putInt("totalSteps", totalSteps)
                    putString("recordDate", currentDate)
                    apply()
                }
                // 更新通知顯示步數
                val notification = NotificationCompat.Builder(this, "step_channel")
                    .setContentTitle("計步器正在運行")
                    .setContentText("目前步數: $todaySteps")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .build()

                val notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(1, notification)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }

}


fun startStepCounterService(context: Context) {
    val intent = Intent(context, StepCounterService::class.java)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        context.startForegroundService(intent)
    } else {
        context.startService(intent)
    }
}