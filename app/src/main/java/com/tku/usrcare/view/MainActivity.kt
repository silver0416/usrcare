package com.tku.usrcare.view

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.firebase.messaging.FirebaseMessaging
import com.tku.usrcare.R
import com.tku.usrcare.api.ApiUSR
import com.tku.usrcare.api.ApiUSR.Companion.postFirebaseCloudMessagingToken
import com.tku.usrcare.databinding.ActivityMainBinding
import com.tku.usrcare.model.RegistrationToken
import com.tku.usrcare.model.Version
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.repository.UniqueCode
import com.tku.usrcare.view.ui.main.MainFragmentDialogs
import com.tku.usrcare.viewmodel.MainViewModel
import com.tku.usrcare.viewmodel.ViewModelFactory
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var sessionManager: SessionManager
    @RequiresApi(Build.VERSION_CODES.Q)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelFactory = ViewModelFactory(SessionManager(this))
        sessionManager = SessionManager(this)
        mainViewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[MainViewModel::class.java]
        mainViewModel.getHistoryStory()
        mainViewModel.getVocabulary()
        if (!mainViewModel.isOAuthCheck()) {
            mainViewModel.getOAuthCheck()
        }
        else {
            if (mainViewModel.mSessionManager.getIsAskOauthBinding()){
                if (!(mainViewModel.mSessionManager.getOAuthCheck().google || mainViewModel.mSessionManager.getOAuthCheck().line)) {
                    mainViewModel.isOauthBindingShow.value = true
                }
            }
        }
        UniqueCode.init(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        intent = Intent(this, LoginActivity::class.java)
        checkLogin(intent)
        checkNotificationsPermission(intent)
        checkInternetExist(intent)
        checkTokenUseful(mainViewModel)
        createNotificationChannel(getString(R.string.clock_reminder))
        createNotificationChannel("broadcast")
        val composeView = binding.composeView
        mainViewModel.subScribeFirebaseTopic("broadcast")

        composeView.setContent {
            MainFragmentDialogs()
        }

    }

    override fun onStart() {
        super.onStart()
        this.onBackPressedDispatcher.addCallback(this) {
            val alertDialog = androidx.appcompat.app.AlertDialog.Builder(this@MainActivity)
            alertDialog.setTitle("Exit")
            alertDialog.setMessage("確定要離開APP嗎")
            alertDialog.setPositiveButton("是的") { _, _ ->
                finishAffinity()
            }
            alertDialog.setNegativeButton("取消") { _, _ ->
            }
            alertDialog.show()
        }
    }

    override fun onResume() {
        super.onResume()
        checkNotificationsPermission(intent)
        checkInternetExist(intent)
        checkLogin(intent)
        checkTokenUseful(mainViewModel)
    }

    private fun checkLogin(intent: Intent) {
        val sessionManager = SessionManager(this)
        val userToken = sessionManager.getUserToken()
        if (userToken == null) {
            intent.setClass(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createNotificationChannel(channelName:String) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        //val channelName = getString(R.string.clock_reminder)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel =
            NotificationChannel(channelName, channelName, importance)
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun checkNotificationsPermission(intent: Intent) {
        // 檢查權限
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val areNotificationsEnabled = notificationManager.areNotificationsEnabled()


        if (!areNotificationsEnabled) {
            intent.setClass(this, PermissionsRequestActivity::class.java)
            startActivity(intent)
        } else {
            val channel =
                notificationManager.getNotificationChannel(getString(R.string.clock_reminder))
            val isChannelEnabled = channel?.importance != NotificationManager.IMPORTANCE_NONE
            if (!isChannelEnabled) {
                intent.setClass(this, PermissionsRequestActivity::class.java)
                startActivity(intent)
            }
        }
        //  為android 14檢查SCHEDULE_EXACT_ALARM權限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val isAlarmManagerExactAlarmPermissionGranted =
                alarmManager.canScheduleExactAlarms()
            if (!isAlarmManagerExactAlarmPermissionGranted) {
                intent.setClass(this, PermissionsRequestActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun checkInternetExist(intent: Intent) {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

        // 檢查網路是否可用
        val isInternetAvailable =
            network != null && networkCapabilities != null && networkCapabilities.hasCapability(
                NetworkCapabilities.NET_CAPABILITY_INTERNET
            )


        if (!isInternetAvailable) {
            // 如果網路不可用，則啟動 InternetRequestActivity
            intent.setClass(this, InternetRequestActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkTokenUseful(mainViewModel: MainViewModel) {
        //Log.d("RegistrationToken","測試1:")
        ApiUSR.postTest(
            activity = this, version = Version(
                packageManager.getPackageInfo(
                    packageName, 0
                ).versionName.toString()
            )
        ) { it ->
            if (it == "403") {
                mainViewModel.showAlertDialogEvent.value = it
            }
        }
        lifecycleScope.launch{
            //Log.d("RegistrationToken","測試2:")
            postRegistrationToken(sessionManager,mainViewModel)
        }
    }

    suspend fun postRegistrationToken(sessionManager: SessionManager,mainViewModel: MainViewModel){
        try{
            //Log.d("RegistrationToken","測試3:")
            val RegistrationToken=RegistrationToken(sessionManager.getRegistrationToken().toString())
            Log.d("RegistrationToken","測試4:"+RegistrationToken)
            postFirebaseCloudMessagingToken(
             sessionManager,RegistrationToken,{ response ->
                Log.d("RegistrationToken","response:成功")
            }, { errorMessage ->
                Log.d("RegistrationToken","response:失敗")
            })
        }catch(e:Exception){
            Log.d("RegistrationToken","錯誤:"+e)
        }
    }
}
