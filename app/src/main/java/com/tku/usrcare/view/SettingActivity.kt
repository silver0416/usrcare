package com.tku.usrcare.view

import SettingMain
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
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
import com.tku.usrcare.view.ui.setting.GoogleOAuthBinding
import com.tku.usrcare.view.ui.setting.LineOAuthBinding
import com.tku.usrcare.view.ui.setting.Unbind
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
            val intent = intent
            if (intent != null) {
                if (intent.extras?.containsKey("oauthType") == true) {
                    val oauthType = intent.extras!!.getString("oauthType")
                    if (oauthType != null) {
                        val bundle = Bundle()
                        bundle.putString("oauthType", oauthType)
                        navController.navigate(oauthType)
                    }
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

        //unbind
        data object unbind : SettingScreen("unbind/{oauthType}")

        //通知管理
        data object notification : SettingScreen("notification")

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
            enterTransition = enterTransition(),
            exitTransition = exitTransition(),
            popEnterTransition = popEnterTransition(),
            popExitTransition = popExitTransition(),
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(id = R.color.bgMain))
            ) {
            composable(SettingScreen.main.route) {
                SettingMain(settingViewModel = settingViewModel, navController = navController)
            }

            composable(SettingScreen.google.route) {
                GoogleOAuthBinding(settingViewModel, navController)
            }
            composable(SettingScreen.line.route) {
                LineOAuthBinding(settingViewModel, navController)
            }
            composable(SettingScreen.unbind.route) {
                Unbind(settingViewModel,navController,it.arguments?.getString("oauthType")?:"")
            }
        }
    }
}

