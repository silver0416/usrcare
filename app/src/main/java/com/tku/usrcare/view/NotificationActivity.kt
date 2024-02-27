package com.tku.usrcare.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
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
import com.tku.usrcare.view.ui.notification.NotificationMain
import com.tku.usrcare.viewmodel.NotificationViewModel
import com.tku.usrcare.viewmodel.ViewModelFactory

private lateinit var notificationViewModel: NotificationViewModel
class NotificationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelFactory = ViewModelFactory(SessionManager(this))
        notificationViewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[NotificationViewModel::class.java]
        setContent {
            val navController = rememberNavController()
            MaterialTheme {
                Surface(color = MaterialTheme.colorScheme.surface) {
                    NotificationNav(navController = navController)
                }
            }
        }
    }

    sealed class NotificationScreen(val route: String) {
        data object NotificationList : NotificationScreen("NotificationList")
    }

    @Composable
    fun NotificationNav(navController: NavHostController) {
        NavHost(
            navController = navController,
            startDestination = NotificationScreen.NotificationList.route,
            enterTransition = enterTransition(),
            exitTransition = exitTransition(),
            popEnterTransition = popEnterTransition(),
            popExitTransition = popExitTransition(),
            modifier = Modifier.background(colorResource(id = R.color.bgMain))
        ) {
            composable(NotificationScreen.NotificationList.route) {
                NotificationMain(notificationViewModel, navController)
            }
        }
    }
}