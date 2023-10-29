package com.tku.usrcare.view.ui.main

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.changedToUpIgnoreConsumed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tku.usrcare.R
import com.tku.usrcare.model.MoodTime
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.component.ApiFaildAlertDialogCompose
import com.tku.usrcare.view.component.FixedSizeText
import com.tku.usrcare.view.component.Loading
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
    val apiFaildeDialogMessage = remember { mutableStateOf("") }
    val sessionManager = SessionManager(context)
    val viewModelFactory = ViewModelFactory(sessionManager)
    mainViewModel =
        viewModel(
            viewModelStoreOwner = context as androidx.lifecycle.ViewModelStoreOwner,
            factory = viewModelFactory
        )
    mainViewModel.showAlertDialogEvent.observe(context as androidx.lifecycle.LifecycleOwner) {
        apiFaildeDialogMessage.value = it
        showApiFailedDialog.value = true
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
        ApiFaildAlertDialogCompose(errorMessage = apiFaildeDialogMessage.value)
    }

}



@Composable
fun DailySignInDialog(isDailySignInDialogShow: MutableState<Boolean>) {
    val content = remember { mutableStateOf("dailyMood") }
    val timeFormatChinese = mainViewModel.timeFormatChinese
    AlertDialog(
        onDismissRequest = { },
        title = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                FixedSizeText(
                    text = timeFormatChinese.format(System.currentTimeMillis()),
                    size = 80.dp,
                    fontWeight = FontWeight.Bold
                )
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


        fun sendMoodResult(mood: Int) {
            val moodTime = MoodTime(timeFormat.format(System.currentTimeMillis()))
            mainViewModel.postMood(mood, moodTime)
            mainViewModel.addSignedDateTime(mood)
            //觀察變化
            mainViewModel.isDailySignInDialogShow.observeForever {
                isDailySignInDialogShow.value = it
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

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
@Preview
fun DailySignInDialogPreview() {
    DailySignInDialog(isDailySignInDialogShow = remember { mutableStateOf(true) })
}