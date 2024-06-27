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
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.messaging.FirebaseMessaging
import com.tku.usrcare.R
import com.tku.usrcare.api.ApiUSR
import com.tku.usrcare.databinding.ActivityMainBinding
import com.tku.usrcare.model.Version
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.repository.UniqueCode
import com.tku.usrcare.view.ui.main.MainFragmentDialogs
import com.tku.usrcare.viewmodel.MainViewModel
import com.tku.usrcare.viewmodel.ViewModelFactory


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    @RequiresApi(Build.VERSION_CODES.Q)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelFactory = ViewModelFactory(SessionManager(this))
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
        createNotificationChannel()
        val composeView = binding.composeView
        mainViewModel.subScribeFirebaseTopic("broadcast")

        composeView.setContent {
            MainFragmentDialogs()
        }
        // 取得並顯示 registration token
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                //Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            // Get new FCM registration token
            val registrationToken = task.result
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

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        val name = getString(R.string.clock_reminder)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel =
            NotificationChannel(getString(R.string.clock_reminder), name, importance)
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
    }


}