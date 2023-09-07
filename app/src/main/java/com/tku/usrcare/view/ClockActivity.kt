package com.tku.usrcare.view

import com.tku.usrcare.view.ui.clock.Drug
import com.tku.usrcare.view.ui.clock.CenterButtons
import com.tku.usrcare.view.ui.clock.TitleBox
import com.tku.usrcare.view.ui.clock.NoticeList


import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tku.usrcare.R
import com.tku.usrcare.view.ui.clock.ActivityNotice
import com.tku.usrcare.view.ui.clock.Drink
import com.tku.usrcare.view.ui.clock.ListFAB
import com.tku.usrcare.view.ui.clock.Sleep
import com.tku.usrcare.view.ui.theme.UsrcareTheme


class ClockActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            MaterialTheme{
                Surface(color = MaterialTheme.colorScheme.background) {
                    ClockNav(navController = navController)
                }
            }
        }
    }
}

sealed class Screen(val route: String) {
    object Main : Screen("Main")
    object Drug : Screen("Drug")
    object Activity : Screen("Activity")
    object Drink : Screen("Drink")
    object Sleep : Screen("Sleep")
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ClockNav(navController: NavHostController) {
    NavHost(navController, startDestination = Screen.Main.route) {
        composable(Screen.Main.route) {
            Main(navController)
        }
        composable(Screen.Drug.route) {
            Drug(navController)
        }
        composable(Screen.Activity.route) {
            ActivityNotice(navController)
        }
        composable(Screen.Drink.route) {
            Drink(navController)
        }
        composable(Screen.Sleep.route) {
            Sleep(navController)
        }
    }
}

fun Context.findActivity(): Activity? {
    var currentContext = this
    while (currentContext is ContextWrapper) {
        if (currentContext is Activity) {
            return currentContext
        }
        currentContext = currentContext.baseContext
    }
    return null
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Main(navController: NavHostController) {
    Box(
        Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.bgClock))
    ) {
        val coroutineScope = rememberCoroutineScope()
        val offsetY = remember { Animatable(1500f) } // Initialize at -180f
        val status = remember {
            mutableStateOf(false)
        }
        Scaffold(
            floatingActionButton = {
                ListFAB(coroutineScope, offsetY, status)
            },
            containerColor = Color.Transparent
        ) { padding ->
            TitleBox()
            CenterButtons(navController)
            Column(modifier = Modifier.padding(padding)) {
                NoticeList(coroutineScope, offsetY, status)
            }
        }
    }
}

var flag = false

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun ClockPreview() {
    UsrcareTheme {
        Main(navController = rememberNavController())
    }
}


