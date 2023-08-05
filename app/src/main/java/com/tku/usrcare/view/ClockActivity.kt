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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tku.usrcare.R
import com.tku.usrcare.model.ClockData
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.ui.clock.ListBox
import com.tku.usrcare.view.ui.theme.UsrcareTheme


class ClockActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            UsrcareTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    ClockNav(navController)
                }
            }
        }
    }
}

sealed class Screen(val route: String) {
    object Main : Screen("Main")
    object Drug : Screen("Drug")
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
        TitleBox()
        CenterButtons(navController)
        NoticeList()
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


