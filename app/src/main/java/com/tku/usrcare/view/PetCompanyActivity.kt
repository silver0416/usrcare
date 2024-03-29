package com.tku.usrcare.view

import android.content.Context
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
import com.tku.usrcare.view.ui.petcompany.Store
import com.tku.usrcare.viewmodel.PetCompanyViewModel
import com.tku.usrcare.viewmodel.ViewModelFactory

class PetCompanyActivity: ComponentActivity() {
    private lateinit var petCompanyViewModel: PetCompanyViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelFactory = ViewModelFactory(SessionManager(this))
        petCompanyViewModel = ViewModelProvider(this, viewModelFactory)[PetCompanyViewModel::class.java]
        window.statusBarColor = ContextCompat.getColor(this, R.color.bgSatKTV)
        setContent {
            val navController = rememberNavController()
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    PatCompanyNav(navController = navController)
                }
            }
        }
    }
    sealed class PetScreen(val route: String) {
        //清單主頁
        data object main : PetScreen("main")
        //OAuth(store)
        data object store : PetScreen("store")

    }
    @Composable
    fun PatCompanyNav(navController: NavHostController) {
        val context: Context = this
        NavHost(navController = navController,
            startDestination = PetScreen.main.route,
            enterTransition = enterTransition(),
            exitTransition = exitTransition(),
            popEnterTransition = popEnterTransition(),
            popExitTransition = popExitTransition(),
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(id = R.color.bgSatKTV))
        ) {
            composable(PetScreen.main.route) {
                com.tku.usrcare.view.ui.petcompany.MainPage(petCompanyViewModel = petCompanyViewModel, navController = navController)
            }

            composable(PetScreen.store.route) {
                Store(petCompanyViewModel, navController,context=context)
            }
        }
    }
}


