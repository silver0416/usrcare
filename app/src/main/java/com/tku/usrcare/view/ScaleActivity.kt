package com.tku.usrcare.view
import com.tku.usrcare.view.ui.scale.ScaleList

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tku.usrcare.view.ui.clock.Drug
import com.tku.usrcare.view.ui.scale.Scale
import com.tku.usrcare.view.ui.theme.UsrcareTheme

class ScaleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            UsrcareTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ScaleNav(navController)
                }
            }
        }
    }
}

sealed class ScaleScreen(val route: String) {
    object ScaleList : ScaleScreen("ScaleList")
    object Scale : ScaleScreen("Scale/{id}")
}

@Composable
fun ScaleNav(navController: NavHostController) {
    NavHost(navController, startDestination = ScaleScreen.ScaleList.route) {
        composable(ScaleScreen.ScaleList.route) {
            ScaleList(navController)
        }
        composable(ScaleScreen.Scale.route) {
            Scale(id = it.arguments?.getString("id")!!.toInt(),navController = navController)
        }
    }
}