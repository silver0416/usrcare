package com.tku.usrcare.view


import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tku.usrcare.R
import com.tku.usrcare.view.component.TitleBox
import com.tku.usrcare.view.ui.clock.ActivityNotice
import com.tku.usrcare.view.ui.clock.CenterButtons
import com.tku.usrcare.view.ui.clock.Drink
import com.tku.usrcare.view.ui.clock.Drug
import com.tku.usrcare.view.ui.clock.ListFAB
import com.tku.usrcare.view.ui.clock.NoticeList
import com.tku.usrcare.view.ui.clock.Sleep
import com.tku.usrcare.view.ui.theme.UsrcareTheme


class ClockActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        window.statusBarColor = ContextCompat.getColor(this, R.color.bgClock)
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            MaterialTheme {
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
    NavHost(navController, startDestination = Screen.Main.route,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { 1000 }, // 從右側進入
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -300 }, // 向左側退出
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -300 }, // 從左側進入
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { 1000 }, // 向右側退出
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        }
    ) {
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
        val transparency = remember { Animatable(0f) }
        val status = remember {
            mutableStateOf(false)
        }
        Scaffold(
            floatingActionButton = {
                ListFAB(coroutineScope, offsetY, transparency, status)
            },
            containerColor = Color.Transparent
        ) { padding ->
            TitleBox(
                color = colorResource(id = R.color.btnClockColor),
                title = stringResource(id = R.string.clock_reminder),
                icon = painterResource(id = R.drawable.ic_clocknotice)
            )
            CenterButtons(navController)
            Column(modifier = Modifier.padding(padding)) {
                NoticeList(coroutineScope, offsetY, transparency, status)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun ClockPreview() {
    UsrcareTheme {
        Main(navController = rememberNavController())
    }
}


