package com.tku.usrcare.view

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tku.usrcare.R
import com.tku.usrcare.view.component.TitleBox
import com.tku.usrcare.view.ui.scale.Scale
import com.tku.usrcare.view.ui.scale.ScaleList

class ScaleActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            MaterialTheme {
                Surface(color = MaterialTheme.colorScheme.surface) {
                    ScaleNav(navController = navController)
                }
            }
        }
    }

    sealed class ScaleScreen(val route: String) {
        data object ScaleList : ScaleScreen("ScaleList")
        data object Scale : ScaleScreen("Scale/{id}")

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @Composable
    fun ScaleNav(navController: NavHostController) {
        val context = LocalContext.current
        NavHost(navController, startDestination = ScaleScreen.ScaleList.route) {
            composable(ScaleScreen.ScaleList.route) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = colorResource(id = R.color.bgScale)),
                    contentAlignment = Alignment.Center
                ) {
                    window.statusBarColor = ContextCompat.getColor(context, R.color.bgScale)
                    TitleBox(
                        color = colorResource(id = R.color.btnMoodScaleColor),
                        title = stringResource(id = R.string.mood_scale),
                        icon = painterResource(id = R.drawable.ic_moodscale)
                    )
                    ScaleList(navController)
                }
            }
            composable(ScaleScreen.Scale.route) {
                window.statusBarColor = ContextCompat.getColor(context, R.color.white)
                Scale(id = it.arguments?.getString("id")!!.toInt(), navController = navController)
            }
        }
    }
}