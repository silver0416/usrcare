package com.tku.usrcare.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tku.usrcare.R
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.component.enterTransition
import com.tku.usrcare.view.component.exitTransition
import com.tku.usrcare.view.component.popEnterTransition
import com.tku.usrcare.view.component.popExitTransition
import com.tku.usrcare.view.ui.clock.ClockContent
import com.tku.usrcare.view.ui.clock.NewReminder
import com.tku.usrcare.viewmodel.ClockViewModel
import com.tku.usrcare.viewmodel.ViewModelFactory

class ClockActivity : ComponentActivity() {
    private lateinit var clockViewModel: ClockViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelFactory = ViewModelFactory(SessionManager(this))
        clockViewModel = ViewModelProvider(this, viewModelFactory)[ClockViewModel::class.java]
        window.statusBarColor = ContextCompat.getColor(this, R.color.bgClock)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    ClockNav()
                }
            }
        }
    }

    sealed class ClockScreen(val route: String , val title: String) {
        //清單主頁
        data object main : ClockScreen("main" , "")
        data object drugNotification : ClockScreen("drugNotification" , "\uD83D\uDC8A\t用藥提醒")
        data object activityNotification : ClockScreen("activityNotification" , "\uD83C\uDFC3\u200D♂\uFE0F\t活動提醒")
        data object drinkNotification : ClockScreen("drinkNotification" , "\uD83D\uDCA7\t喝水提醒")
        data object sleepNotification : ClockScreen("sleepNotification" , "\uD83D\uDCA4\t休息提醒")
    }

    @Composable
    fun ClockNav() {
        val navController = rememberNavController()
        NavHost(
            navController = navController, startDestination = ClockScreen.main.route,
            enterTransition = enterTransition(),
            exitTransition = exitTransition(),
            popEnterTransition = popEnterTransition(),
            popExitTransition = popExitTransition(),
        ) {
            composable(ClockScreen.main.route) {
                ClockContent(navController = navController)
            }
            composable(ClockScreen.drugNotification.route) {
                NewReminder(navController = navController, title = "\uD83D\uDC8A\t用藥提醒")
            }
            composable(ClockScreen.activityNotification.route) {
                NewReminder(navController = navController, title = "\uD83C\uDFC3\u200D♂\uFE0F\t活動提醒")
            }
            composable(ClockScreen.drinkNotification.route) {
                NewReminder(navController = navController, title = "\uD83D\uDCA7\t喝水提醒")
            }
            composable(ClockScreen.sleepNotification.route) {
                NewReminder(navController = navController, title = "\uD83D\uDCA4\t休息提醒")
            }
        }
    }
}