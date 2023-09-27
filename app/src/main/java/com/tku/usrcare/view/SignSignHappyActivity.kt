package com.tku.usrcare.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tku.usrcare.R
import com.tku.usrcare.view.ui.signsignhappy.SignSignHappyMain

class SignSignHappyActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.bgSignSignHappy)
        setContent {
            val navController = rememberNavController()
            MaterialTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SignSignHappyNav(navController = navController)
                }
            }
        }
    }

    sealed class SignSignHappyScreen(val route: String) {
        data object Main : SignSignHappyScreen("main")
    }

    @Composable
    fun SignSignHappyNav(navController: NavHostController) {
        NavHost(navController = navController, startDestination = SignSignHappyScreen.Main.route) {
            composable(SignSignHappyScreen.Main.route) {
                SignSignHappyMain()
            }
        }
    }
}