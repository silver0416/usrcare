package com.tku.usrcare.view.ui.main

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.changedToUpIgnoreConsumed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.play.core.review.ReviewManagerFactory
import com.tku.usrcare.R
import com.tku.usrcare.model.MoodTime
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.SettingActivity
import com.tku.usrcare.view.component.ApiFaildAlertDialogCompose
import com.tku.usrcare.view.component.AutoSizedText
import com.tku.usrcare.view.component.FixedSizeText
import com.tku.usrcare.view.component.Loading
import com.tku.usrcare.view.component.findActivity
import com.tku.usrcare.view.ui.setting.UpdateCheckerDialog
import com.tku.usrcare.viewmodel.MainViewModel
import com.tku.usrcare.viewmodel.ViewModelFactory


private lateinit var mainViewModel: MainViewModel


@ExperimentalMaterial3Api
@Composable
fun MainFragmentDialogs() {
    val context = LocalContext.current
    val isDailySignInDialogShow = remember { mutableStateOf(true) }
    val showUpdateCheckerDialog = remember { mutableStateOf(true) }
    val showApiFailedDialog = remember { mutableStateOf(false) }
    val apiFailedDialogMessage = remember { mutableStateOf("") }
    val sessionManager = SessionManager(context)
    val viewModelFactory = ViewModelFactory(sessionManager)
    mainViewModel =
        viewModel(
            viewModelStoreOwner = context as androidx.lifecycle.ViewModelStoreOwner,
            factory = viewModelFactory
        )
    mainViewModel.showAlertDialogEvent.observe(context as androidx.lifecycle.LifecycleOwner) {
        apiFailedDialogMessage.value = it
        showApiFailedDialog.value = true

    }

    val isOauthBindingShow = mainViewModel.isOauthBindingShow.observeAsState(false)
    if (isOauthBindingShow.value) {
        OauthBindingAlertDialog()
    }

    val isSignedToday = mainViewModel.isSignedToday()
    if (isSignedToday) {
        isDailySignInDialogShow.value = false
    }

    if (showUpdateCheckerDialog.value) {
        UpdateCheckerDialog(
            showUpdateCheckerDialog = showUpdateCheckerDialog,
            skipNoUpdateDialog = true
        )
    }

    if (isDailySignInDialogShow.value) {
        DailySignInDialog(isDailySignInDialogShow)
    }

    if (showApiFailedDialog.value) {
        ApiFaildAlertDialogCompose(context = context, errorMessage = apiFailedDialogMessage.value)
    }

}


@Composable
fun DailySignInDialog(isDailySignInDialogShow: MutableState<Boolean>) {
    val content = remember { mutableStateOf("dailyMood") }
    val timeFormatChinese = mainViewModel.timeFormatChinese
    AlertDialog(
        onDismissRequest = { },
        title = {
            if (content.value == "dailyMood"){
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    FixedSizeText(
                        text = timeFormatChinese.format(System.currentTimeMillis()),
                        size = 80.dp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        text = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                when (content.value) {
                    "dailyMood" -> DailySignInContent(
                        isDailySignInDialogShow = isDailySignInDialogShow,
                        content
                    )

                    "loading" -> Loading(isVisible = true)
                    "fail" -> Button(onClick = { isDailySignInDialogShow.value = false }) {
                        Text(text = "簽到失敗")
                    }

                    "askReview" -> AskReviewContent(isDailySignInDialogShow = isDailySignInDialogShow)

                    else -> isDailySignInDialogShow.value = false
                }
            }
        },
        confirmButton = {
        }
    )
}


@Composable
fun DailySignInContent(
    isDailySignInDialogShow: MutableState<Boolean>,
    content: MutableState<String>
) {
    val imageIds = listOf(
        R.drawable.ic_mood1,
        R.drawable.ic_mood2,
        R.drawable.ic_mood3,
        R.drawable.ic_mood4,
        R.drawable.ic_mood5
    )

    @RequiresApi(Build.VERSION_CODES.Q)
    @Composable
    fun ZoomableImageRow(imageIds: List<Int>) {
        val context = LocalContext.current
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val scales = remember { mutableStateListOf(*Array(imageIds.size) { 1f }) }
        val imageSize = 50.dp  // 新的圖片大小
        val imagePadding = 5.dp  // 新的間隔大小
        val totalSizeWithPadding = (imageSize + imagePadding * 2) - 5.dp  // 新的總大小
        val timeFormat = mainViewModel.timeFormat
        val timeFormatIso = mainViewModel.timeFormatIso


        fun sendMoodResult(mood: Int) {
            val moodTime = MoodTime(timeFormatIso.format(System.currentTimeMillis()))
            val signedTimes = mainViewModel.signedDateTimeList.size
            var goReview = false
            if (signedTimes > 3 && !mainViewModel.getIfMakeReview()) {
                Log.d("Review", "signedTimes > 3")
                goReview = true
            }
            mainViewModel.postMood(mood, moodTime, goReview)
            mainViewModel.addSignedDateTime(mood)
            if (goReview) {
                Log.d("Review", "goReview")
                content.value = "askReview"
            } else {
                //觀察變化
                mainViewModel.isDailySignInDialogShow.observeForever {
                    isDailySignInDialogShow.value = it
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            var offsetX = 0f
                            val event = awaitPointerEvent()
                            val position = event.changes.firstOrNull()?.position ?: continue
                            event.changes.forEach { pointerInputChange ->
                                when (pointerInputChange.changedToUpIgnoreConsumed()) {
                                    true -> {
                                        // 手指離開螢幕
                                        content.value = "loading"
                                        sendMoodResult(scales.indexOfFirst { it == 1.5f } + 1)
                                    }

                                    else -> {}
                                }
                            }
                            val newScales = scales
                                .toList()
                                .toMutableList()
                            var hasScaleChanged = false // 用於標記是否需要震動

                            newScales.forEachIndexed { index, _ ->
                                val start = offsetX
                                val end = offsetX + totalSizeWithPadding.toPx()
                                val newScale = if (position.x in start..end) {
                                    1.5f
                                } else {
                                    1f
                                }

                                // 檢查是否需要震動
                                if (newScales[index] != newScale) {
                                    hasScaleChanged = true
                                }

                                newScales[index] = newScale
                                offsetX += totalSizeWithPadding.toPx()
                            }

                            // 如果有變更，則觸發震動
                            if (hasScaleChanged) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    val effect =
                                        VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK)
                                    vibrator.vibrate(effect)
                                } else {
                                    @Suppress("DEPRECATION")
                                    vibrator.vibrate(10)
                                }
                            }
                            scales.clear()
                            scales.addAll(newScales)
                        }
                    }
                },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            imageIds.forEachIndexed { index, imageId ->
                Image(
                    painter = painterResource(id = imageId),
                    contentDescription = null,
                    modifier = Modifier
                        .size(imageSize)
                        .padding(imagePadding)
                        .graphicsLayer(
                            scaleX = scales[index],
                            scaleY = scales[index]
                        )
                )
            }
        }
    }

    Column(
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        FixedSizeText(
            text = stringResource(id = R.string.are_you_ok_today),
            size = 80.dp
        )
        Spacer(modifier = Modifier.size(12.dp))
        ZoomableImageRow(imageIds = imageIds)
    }
}

@Composable
fun AskReviewContent(
    isDailySignInDialogShow: MutableState<Boolean> = remember { mutableStateOf(true) }
) {
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        FixedSizeText(
            text = stringResource(id = R.string.give_us_some_review),
            size = 90.dp
        )
        FixedSizeText(
            text = stringResource(id = R.string.give_us_some_review_content),
            size = 90.dp
        )
        Spacer(modifier = Modifier.size(12.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextButton(onClick = {
                mainViewModel.saveIfMakeReview(true)
                isDailySignInDialogShow.value = false
            }) {
                Text(text = stringResource(id = R.string.no_this_time), fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(70.dp))
            Button(onClick = {
                mainViewModel.saveIfMakeReview(true)

                mainViewModel.isDailySignInDialogShow.observeForever {
                    isDailySignInDialogShow.value = it
                }

                val reviewManager = ReviewManagerFactory.create(context)
                // 請求一個ReviewInfo對象
                val request = reviewManager.requestReviewFlow()
                request.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // 獲取ReviewInfo對象
                        val reviewInfo = task.result
                        // 啟動評分流程
                        val flow = context.findActivity()
                            ?.let { reviewManager.launchReviewFlow(it, reviewInfo) }
                        flow?.addOnCompleteListener { _ ->
                            // 可以在這裡處理評分流程結束後的邏輯
                            mainViewModel.isDailySignInDialogShow.value = false
                        }
                    } else {
                        // 處理錯誤情況
                        mainViewModel.isDailySignInDialogShow.value = false
                        Log.d("Review", "requestReviewFlow failed")
                    }
                }
            }) {
                Text(text = stringResource(id = R.string.yes) , fontSize = 20.sp)
            }
        }
    }
}


@Composable
fun OauthBindingAlertDialog() {
    Dialog(
        onDismissRequest = { mainViewModel.isOauthBindingShow.value = false },
        content = {
            Box(
                modifier = Modifier
                    .clip(
                        MaterialTheme.shapes.large
                    )
                    .background(color = colorResource(id = R.color.bgDialog))
                    .padding(top = 26.dp, start = 5.dp, end = 5.dp, bottom = 5.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AutoSizedText(
                        text = "不再煩惱忘記密碼",
                        size = 25,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        GoogleOauthButton()
                        Spacer(modifier = Modifier.size(10.dp))
                        LineOauthButton()
                        TextButton(
                            onClick = {
                                mainViewModel.mSessionManager.saveIsAskOauthBinding(false)
                                mainViewModel.isOauthBindingShow.value = false
                            },
                            elevation = null,
                        ) {
                            AutoSizedText(text = "不要再提醒", color = Color.Black, size = 18)
                        }
                    }
                }
            }
        },
    )
}


@Composable
fun GoogleOauthButton() {
    val context = LocalContext.current
    Button(
        onClick = {
            mainViewModel.isOauthBindingShow.value = false
            context.startActivity(
                Intent(
                    context,
                    SettingActivity::class.java
                ).putExtra("oauthType", "google")
            )
        },
        border = BorderStroke(1.dp, color = Color.LightGray),
        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.white)),
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(8.dp, 12.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_google),
            contentDescription = "line",
            modifier = Modifier
                .size(30.dp)
                .weight(0.2f)
        )
        Box(modifier = Modifier.weight(0.8f), contentAlignment = Alignment.Center) {
            AutoSizedText(
                text = "綁定Google快速登入",
                color = Color.Black,
                size = 35,
            )
        }
    }
}

@Composable
fun LineOauthButton() {
    val context = LocalContext.current
    Button(
        onClick = {
            mainViewModel.isOauthBindingShow.value = false
            context.startActivity(
                Intent(
                    context,
                    SettingActivity::class.java
                ).putExtra("oauthType", "line")
            )
        },
        border = BorderStroke(0.dp, color = Color.LightGray),
        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.line)),
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(8.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_line),
            contentDescription = "line",
            modifier = Modifier
                .size(40.dp)
                .weight(0.2f)
        )
        Box(modifier = Modifier.weight(0.8f), contentAlignment = Alignment.Center) {
            AutoSizedText(
                text = "綁定LINE快速登入",
                color = Color.White,
                size = 35
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
@Preview
fun DailySignInDialogPreview() {
    AskReviewContent(remember { mutableStateOf(true) })
}