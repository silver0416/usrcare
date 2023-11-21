package com.tku.usrcare.view

import SettingMain
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tku.usrcare.R
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.ui.setting.GoogleOAuthBinding
import com.tku.usrcare.view.ui.setting.LineOAuthBinding
import com.tku.usrcare.viewmodel.SettingViewModel
import com.tku.usrcare.viewmodel.ViewModelFactory


class SettingActivity : ComponentActivity() {

    private lateinit var settingViewModel: SettingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModelFactory = ViewModelFactory(SessionManager(this))
        settingViewModel = ViewModelProvider(this, viewModelFactory)[SettingViewModel::class.java]
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
        data object main : SettingScreen("main")

        //OAuth(Google)
        data object google : SettingScreen("google")

        //OAuth(Line)
        data object line : SettingScreen("line")

        //通知管理
        object notification : SettingScreen("notification")

        //常見問題
        data object common : SettingScreen("common")

        //意見回饋
        data object feedback : SettingScreen("feedback")

        //密碼與帳號安全
        data object password : SettingScreen("password")

        //關於app
        data object about : SettingScreen("about")

        //隱私權政策
        data object privacy : SettingScreen("privacy")

        //服務條款
        data object terms : SettingScreen("terms")
    }


    @Composable
    fun SettingNav(navController: NavHostController) {
        NavHost(navController = navController,
            startDestination = SettingScreen.main.route,
            enterTransition = {
                scaleIn(
                    animationSpec = tween(300, easing = FastOutSlowInEasing),
                    initialScale = 0.8f
                ) + fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                scaleOut(
                    animationSpec = tween(300, easing = FastOutSlowInEasing),
                    targetScale = 1.2f
                ) + fadeOut(animationSpec = tween(300))
            },
            popEnterTransition = {
                scaleIn(
                    animationSpec = tween(300, easing = FastOutSlowInEasing),
                    initialScale = 1.2f
                ) + fadeIn(animationSpec = tween(300))
            },
            popExitTransition = {
                scaleOut(
                    animationSpec = tween(300, easing = FastOutSlowInEasing),
                    targetScale = 0.8f
                ) + fadeOut(animationSpec = tween(300))
            }) {
            composable(SettingScreen.main.route) {
                SettingMain(navController = navController)
            }

            composable(SettingScreen.google.route) {
                GoogleOAuthBinding(settingViewModel, navController)
            }
            composable(SettingScreen.line.route) {
                LineOAuthBinding(settingViewModel, navController)
            }
        }
    }
}

