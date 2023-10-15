package com.tku.usrcare.view

import SettingMain
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tku.usrcare.R


class SettingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.bgMain)
        setContent {
            val navController = rememberNavController()
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    SettingNav(navController = navController)
                }
            }
        }
    }

    sealed class SettingScreen(val route: String) {

        //清單主頁
        object main : SettingScreen("main")

        //通知管理
        object notification : SettingScreen("notification")

        //常見問題
        object common : SettingScreen("common")

        //意見回饋
        object feedback : SettingScreen("feedback")

        //密碼與帳號安全
        object password : SettingScreen("password")

        //關於app
        object about : SettingScreen("about")

        //隱私權政策
        object privacy : SettingScreen("privacy")

        //服務條款
        object terms : SettingScreen("terms")
    }


    @Composable
    fun SettingNav(navController: NavHostController) {
        NavHost(navController = navController, startDestination = SettingScreen.main.route) {
            composable(SettingScreen.main.route) {
                SettingMain()
            }
        }
    }
}

