package com.tku.usrcare.view

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tku.usrcare.R
import com.tku.usrcare.view.ui.scale.Scale
import com.tku.usrcare.view.ui.scale.ScaleList
import com.tku.usrcare.view.ui.scale.TitleBox

class ScaleActivity : ComponentActivity() {
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

    @Composable
    fun ScaleNav(navController: NavHostController) {
        NavHost(navController, startDestination = ScaleScreen.ScaleList.route) {
            composable(ScaleScreen.ScaleList.route) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = colorResource(id = R.color.bgScale)),
                    contentAlignment = Alignment.Center
                ) {
                    TitleBox()
                    ScaleList(navController)
                }
            }
            composable(ScaleScreen.Scale.route) {
                Scale(id = it.arguments?.getString("id")!!.toInt(), navController = navController)
            }
        }
    }
}