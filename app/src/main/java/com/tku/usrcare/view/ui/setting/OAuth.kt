package com.tku.usrcare.view.ui.setting

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.linecorp.linesdk.LineApiResponseCode
import com.linecorp.linesdk.Scope
import com.linecorp.linesdk.auth.LineAuthenticationParams
import com.linecorp.linesdk.auth.LineLoginApi
import com.tku.usrcare.OAuthClientID
import com.tku.usrcare.R
import com.tku.usrcare.api.ApiUSR
import com.tku.usrcare.model.JwtToken
import com.tku.usrcare.model.ReBinding
import com.tku.usrcare.view.component.AutoSizedText
import com.tku.usrcare.view.component.Loading
import com.tku.usrcare.viewmodel.SettingViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun GoogleOAuthBinding(settingViewModel: SettingViewModel, navController: NavHostController) {
    val context = LocalContext.current
    val nowState = remember { mutableStateOf("串接中") }
    val result = remember {
        mutableStateOf(
            ActivityResult(
                -2,
                null
            )
        )
    }
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result.value = it
        }
    val coroutineScope = rememberCoroutineScope()

    settingViewModel.showAlertDialogEvent.observeForever() {
        nowState.value = it
    }

    val showReplacementButton = remember { mutableStateOf(false) }
    val showCancelButton = remember { mutableStateOf(false) }

    val id = remember { mutableStateOf("") }
    val oldId = remember { mutableStateOf("") }


    result.value.let { activityResult ->
        if (activityResult.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(activityResult.data)
            val jwtToken = JwtToken(task.result?.idToken ?: "")
            ApiUSR.postGoogleOAuthBind(jwtToken = jwtToken,
                settingViewModel.getSessionManager(),
                onSuccess = { bindingResponse ->
                    if (bindingResponse.state != null) {
                        nowState.value = "綁定成功"
                        settingViewModel.getOAuthCheck()
                        showReplacementButton.value = false
                        coroutineScope.launch {
                            withContext(Dispatchers.IO) {
                                delay(500)
                                "完成"
                            }
                            navController.navigateUp()
                        }
                    } else {
                        nowState.value = "此google帳號已綁定過其他USR帳號\n是否取代?"
                        showReplacementButton.value = true
                        id.value = jwtToken.idToken
                        oldId.value = bindingResponse.exist.toString()
                    }
                },
                onFail = {
                    nowState.value = "綁定失敗"
                    showCancelButton.value = true
                }
            )
        } else if (activityResult.resultCode == Activity.RESULT_CANCELED) {
            nowState.value = "綁定失敗"
            showCancelButton.value = true
        } else {
            nowState.value = ""
        }
    }


    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(OAuthClientID.GOOGLE_CLIENT_ID)
        .requestEmail()
        .build()
    val mGoogleSignInClient = GoogleSignIn.getClient(context, gso)
    LaunchedEffect(Unit) {
        mGoogleSignInClient.signOut()
        val signInGoogleIntent = mGoogleSignInClient.signInIntent
        launcher.launch(signInGoogleIntent)
    }

    BackHandler {
        mGoogleSignInClient.signOut()
        navController.navigateUp()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = com.tku.usrcare.R.color.bgMain)),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Loading(isVisible = !showCancelButton.value)
            Spacer(modifier = Modifier.size(20.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(colorResource(id = R.color.white))
            ) {
                Text(
                    text = nowState.value,
                    style = androidx.compose.ui.text.TextStyle(fontSize = 30.sp),
                    modifier = Modifier
                        .animateContentSize()
                        .padding(
                            if (nowState.value != "") {
                                10.dp
                            } else {
                                0.dp
                            }
                        ),
                )
            }
            Spacer(modifier = Modifier.size(20.dp))
            AnimatedVisibility(visible = showReplacementButton.value) {
                Column {
                    Button(
                        onClick = {
                            showReplacementButton.value = false
                            nowState.value = "處理中"
                            val reBinding = ReBinding(
                                idToken = id.value,
                                oldUserID = oldId.value
                            )
                            ApiUSR.postGoogleOAuthRebind(
                                reBinding = reBinding,
                                settingViewModel.getSessionManager(),
                                onSuccess = {
                                    if (it.state == "success") {
                                        nowState.value = "綁定成功"
                                        settingViewModel.getOAuthCheck()
                                        showReplacementButton.value = false
                                        coroutineScope.launch {
                                            withContext(Dispatchers.IO) {
                                                delay(500)
                                                "完成"
                                            }
                                            navController.navigateUp()
                                        }
                                    } else {
                                        nowState.value = "綁定失敗"
                                        mGoogleSignInClient.signOut()
                                        showCancelButton.value = true
                                    }
                                },
                                onFail = {
                                    nowState.value = "綁定失敗"
                                    mGoogleSignInClient.signOut()
                                    showCancelButton.value = true
                                }
                            )
                        },
                        modifier = Modifier
                            .size(200.dp, 50.dp)
                            .clip(
                                RoundedCornerShape(8.dp)
                            )
                            .background(colorResource(id = R.color.white))
                            .border(
                                4.dp,
                                colorResource(id = R.color.MainButtonColor),
                                RoundedCornerShape(8.dp)
                            ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.white)
                        )
                    ) {
                        AutoSizedText(text = "取代", size = 25)
                    }
                    Spacer(modifier = Modifier.size(20.dp))
                    Button(
                        onClick = {
                            mGoogleSignInClient.signOut()
                            navController.navigateUp()
                        },
                        modifier = Modifier
                            .size(200.dp, 50.dp)
                            .clip(
                                RoundedCornerShape(8.dp)
                            )
                            .background(colorResource(id = R.color.white))
                            .border(
                                4.dp,
                                colorResource(id = R.color.red),
                                RoundedCornerShape(8.dp)
                            ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.white)
                        ),
                    ) {
                        AutoSizedText(text = "取消", size = 25)
                    }
                }
            }
            AnimatedVisibility(visible = showCancelButton.value) {
                Button(
                    onClick = {
                        mGoogleSignInClient.signOut()
                        navController.navigateUp()
                    },
                    modifier = Modifier
                        .size(200.dp, 50.dp)
                        .clip(
                            RoundedCornerShape(8.dp)
                        )
                        .background(colorResource(id = R.color.white))
                        .border(
                            4.dp,
                            colorResource(id = R.color.MainButtonColor),
                            RoundedCornerShape(8.dp)
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.white)
                    ),
                ) {
                    AutoSizedText(text = "返回", size = 25)
                }
            }
        }
    }
}

@Composable
fun LineOAuthBinding(settingViewModel: SettingViewModel, navController: NavHostController) {
    val context = LocalContext.current
    val nowState = remember { mutableStateOf("串接中") }
    val result = remember {
        mutableStateOf(
            ActivityResult(
                -2,
                null
            )
        )
    }
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result.value = it
        }
    val coroutineScope = rememberCoroutineScope()

    settingViewModel.showAlertDialogEvent.observeForever() {
        nowState.value = it
    }

    val showReplacementButton = remember { mutableStateOf(false) }
    val showCancelButton = remember { mutableStateOf(false) }

    val id = remember { mutableStateOf("") }
    val oldId = remember { mutableStateOf("") }

    result.value.let { activityResult ->
        when (activityResult.resultCode) {
            Activity.RESULT_OK -> {
                val task = LineLoginApi.getLoginResultFromIntent(activityResult.data)
                when (task.responseCode) {
                    LineApiResponseCode.SUCCESS -> {
                        val jwtToken = JwtToken(task.lineCredential?.accessToken?.tokenString ?: "")
                        ApiUSR.postLineOAuthBind(jwtToken = jwtToken,
                            settingViewModel.getSessionManager(),
                            onSuccess = { bindingResponse ->
                                if (bindingResponse.state != null) {
                                    nowState.value = "綁定成功"
                                    settingViewModel.getOAuthCheck()
                                    showReplacementButton.value = false
                                    coroutineScope.launch {
                                        withContext(Dispatchers.IO) {
                                            delay(500)
                                            "完成"
                                        }
                                        navController.navigateUp()
                                    }
                                } else {
                                    nowState.value = "此line帳號已綁定過其他USR帳號\n是否取代?"
                                    showReplacementButton.value = true
                                    id.value = jwtToken.idToken
                                    oldId.value = bindingResponse.exist.toString()
                                }
                            },
                            onFail = {
                                nowState.value = "綁定失敗"
                                showCancelButton.value = true
                            }
                        )
                    }

                    LineApiResponseCode.CANCEL -> {
                        nowState.value = "綁定失敗"
                        showCancelButton.value = true
                    }

                    else -> {
                        nowState.value = "綁定失敗"
                        showCancelButton.value = true
                    }
                }
            }

            Activity.RESULT_CANCELED -> {
                nowState.value = "綁定失敗"
                showCancelButton.value = true
            }

            else -> {
                nowState.value = ""
            }
        }
    }

    val lap = LineAuthenticationParams.Builder()
        .scopes(listOf(Scope.PROFILE))
        .build()
    val signInLineIntent = LineLoginApi.getLoginIntent(
        context,
        OAuthClientID.LINE_CLIENT_ID,
        lap
    )

    LaunchedEffect(Unit) {
        launcher.launch(signInLineIntent)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = com.tku.usrcare.R.color.bgMain)),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Loading(isVisible = !showCancelButton.value)
            Spacer(modifier = Modifier.size(20.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(colorResource(id = R.color.white))
            ) {
                Text(
                    text = nowState.value,
                    style = androidx.compose.ui.text.TextStyle(fontSize = 30.sp),
                    modifier = Modifier
                        .animateContentSize()
                        .padding(
                            if (nowState.value != "") {
                                10.dp
                            } else {
                                0.dp
                            }
                        ),
                )
            }
            Spacer(modifier = Modifier.size(20.dp))
            AnimatedVisibility(visible = showReplacementButton.value) {
                Column {
                    Button(
                        onClick = {
                            showReplacementButton.value = false
                            nowState.value = "處理中"
                            val reBinding = ReBinding(
                                idToken = id.value,
                                oldUserID = oldId.value
                            )
                            ApiUSR.postLineOAuthRebind(
                                reBinding = reBinding,
                                settingViewModel.getSessionManager(),
                                onSuccess = {
                                    if (it.state == "success") {
                                        nowState.value = "綁定成功"
                                        settingViewModel.getOAuthCheck()
                                        showReplacementButton.value = false
                                        coroutineScope.launch {
                                            withContext(Dispatchers.IO) {
                                                delay(500)
                                                "完成"
                                            }
                                            navController.navigateUp()
                                        }
                                    } else {
                                        nowState.value = "綁定失敗"
                                    }
                                },
                                onFail = {
                                    nowState.value = "綁定失敗"
                                }
                            )
                        },
                        modifier = Modifier
                            .size(200.dp, 50.dp)
                            .clip(
                                RoundedCornerShape(8.dp)
                            )
                            .background(colorResource(id = R.color.white))
                            .border(
                                4.dp,
                                colorResource(id = R.color.MainButtonColor),
                                RoundedCornerShape(8.dp)
                            ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.white)
                        )
                    ) {
                        AutoSizedText(text = "取代", size = 25)
                    }
                    Spacer(modifier = Modifier.size(20.dp))
                    Button(
                        onClick = { navController.navigateUp() },
                        modifier = Modifier
                            .size(200.dp, 50.dp)
                            .clip(
                                RoundedCornerShape(8.dp)
                            )
                            .background(colorResource(id = R.color.white))
                            .border(
                                4.dp,
                                colorResource(id = R.color.red),
                                RoundedCornerShape(8.dp)
                            ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.white)
                        ),
                    ) {
                        AutoSizedText(text = "取消", size = 25)
                    }
                }
            }
            AnimatedVisibility(visible = showCancelButton.value) {
                Button(
                    onClick = { navController.navigateUp() },
                    modifier = Modifier
                        .size(200.dp, 50.dp)
                        .clip(
                            RoundedCornerShape(8.dp)
                        )
                        .background(colorResource(id = R.color.white))
                        .border(
                            4.dp,
                            colorResource(id = R.color.MainButtonColor),
                            RoundedCornerShape(8.dp)
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.white)
                    ),
                ) {
                    AutoSizedText(text = "返回", size = 25)
                }
            }
        }
    }
}
