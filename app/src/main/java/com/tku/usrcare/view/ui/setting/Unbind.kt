package com.tku.usrcare.view.ui.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavHostController
import com.tku.usrcare.R
import com.tku.usrcare.viewmodel.SettingViewModel

@Composable
fun Unbind(
    settingViewModel: SettingViewModel,
    navController: NavHostController,
    oauthType: String
) {
    Box(
        modifier = androidx.compose.ui.Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.bgMain)),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        settingViewModel.deleteOAuth(oauthType)
        settingViewModel.finishUnbind.observeForever {
            if (it) {
                navController.navigate("main") {
                    popUpTo("main") {
                        inclusive = true
                    }
                }
            }
        }
    }
}