package com.tku.usrcare.view.ui.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavHostController
import com.tku.usrcare.R
import com.tku.usrcare.view.component.Loading
import com.tku.usrcare.viewmodel.SettingViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        Loading(isVisible = true)
        val coroutineScope = rememberCoroutineScope()
        settingViewModel.deleteOAuth(oauthType)
        settingViewModel.finishUnbind.observeForever {
            if (it) {
                coroutineScope.launch {
                    withContext(Dispatchers.IO) {
                        delay(1000)
                        "完成"
                    }
                    navController.navigateUp()
                }
            }
        }
    }
}