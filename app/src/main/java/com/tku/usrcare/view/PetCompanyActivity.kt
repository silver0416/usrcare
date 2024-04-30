package com.tku.usrcare.view

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tku.usrcare.R
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.component.enterTransition
import com.tku.usrcare.view.component.exitTransition
import com.tku.usrcare.view.component.popEnterTransition
import com.tku.usrcare.view.component.popExitTransition
import com.tku.usrcare.view.ui.petcompany.Store
import com.tku.usrcare.viewmodel.PetCompanyViewModel
import com.tku.usrcare.viewmodel.ViewModelFactory

class PetCompanyActivity: ComponentActivity(), SensorEventListener {
    private lateinit var petCompanyViewModel: PetCompanyViewModel
    var systemService: SensorManager? = null;
    var senor: Sensor? = null;
    private val REQUEST_ACTIVITY_RECOGNITION_PERMISSION = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelFactory = ViewModelFactory(SessionManager(this))
        petCompanyViewModel = ViewModelProvider(this, viewModelFactory)[PetCompanyViewModel::class.java]
        window.statusBarColor = ContextCompat.getColor(this, R.color.bgSatKTV)
        setContent {
            val navController = rememberNavController()
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    PatCompanyNav(navController = navController)
                }
            }
        }

        if (ContextCompat.checkSelfPermission(this, "android.permission.ACTIVITY_RECOGNITION")
            != PackageManager.PERMISSION_GRANTED
        ) {
            // 如果没有授予权限，则请求权限
            ActivityCompat.requestPermissions(
                this,
                arrayOf("android.permission.ACTIVITY_RECOGNITION"),
                REQUEST_ACTIVITY_RECOGNITION_PERMISSION
            )
        } else {
            // 已经授予了权限，初始化传感器
            initSensor()
        }
    }

    private fun initSensor() {//初始化传感器
        systemService = getSystemService(SENSOR_SERVICE) as SensorManager
        senor = systemService!!.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        Log.d("MainActivity", "initSensor已執行")
        if (senor==null){
            Log.d("MainActivity", "沒有找到sensor")
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("MainActivity", "onResume已執行")
        if(systemService!=null&&senor!=null){
            systemService?.registerListener(this, senor, SensorManager.SENSOR_DELAY_NORMAL)
        }
        else{
            Log.d("MainActivity", "onResumeg失敗")
        }
    }

    override fun onPause() {
        super.onPause()
        systemService?.unregisterListener(this)
        Log.d("MainActivity", "onPause已執行")
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val values = event!!.values
        petCompanyViewModel.steps.value = values[0].toInt()
        Log.d("MainActivity", values[0].toString())
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Do something
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_ACTIVITY_RECOGNITION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 用户授予了权限，初始化传感器
                initSensor()
            } else {
                // 用户拒绝了权限请求，可以显示提示或采取其他措施
                Toast.makeText(this, "Activity recognition permission denied", Toast.LENGTH_SHORT).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    sealed class PetScreen(val route: String) {
        //清單主頁
        data object main : PetScreen("main")
        //OAuth(store)
        data object store : PetScreen("store")

    }
    @Composable
    fun PatCompanyNav(navController: NavHostController) {
        val context: Context = this
        NavHost(navController = navController,
            startDestination = PetScreen.main.route,
            enterTransition = enterTransition(),
            exitTransition = exitTransition(),
            popEnterTransition = popEnterTransition(),
            popExitTransition = popExitTransition(),
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(id = R.color.bgSatKTV))
        ) {
            composable(PetScreen.main.route) {
                com.tku.usrcare.view.ui.petcompany.MainPage(petCompanyViewModel = petCompanyViewModel, navController = navController)
            }

            composable(PetScreen.store.route) {
                Store(petCompanyViewModel, navController,context=context)
            }
        }
    }
}


