package com.tku.usrcare.view.ui.main

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.tku.usrcare.R
import com.tku.usrcare.view.component.FixedSizeText
import com.tku.usrcare.view.component.Loading
import java.text.SimpleDateFormat
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.Q)
@ExperimentalMaterial3Api
@Composable
fun MainFragmentDialogs() {
    val isDailySignInDialogShow = remember { mutableStateOf(true) }
    if (isDailySignInDialogShow.value) {
        DailySignInDialog(isDailySignInDialogShow)
    }
}


@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun DailySignInDialog(isDailySignInDialogShow: MutableState<Boolean>) {
    val content = remember { mutableStateOf("dailyMood") }
    androidx.compose.material3.AlertDialog(
        onDismissRequest = { isDailySignInDialogShow.value = false },
        title = {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    //取得今天日期和星期
                    val timeFormat = SimpleDateFormat("MM月dd日(E)", Locale.TAIWAN)
                    FixedSizeText(
                        text = timeFormat.format(System.currentTimeMillis()),
                        size = 80.dp,
                        fontWeight = FontWeight.Bold
                    )
                }
        },
        text = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                when (content.value) {
                    "dailyMood" -> DailySignInContent(isDailySignInDialogShow = isDailySignInDialogShow)
                    "loading" -> Loading(isVisible = true)
                    else -> isDailySignInDialogShow.value = false
                }
            }
        },
        confirmButton = {
        }
    )
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun DailySignInContent(isDailySignInDialogShow: MutableState<Boolean>) {
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
        val timeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.TAIWAN)
        fun sendMoodResult(mood: Int) {
            Log.d("mood", mood.toString())
            //get now date and time
            timeFormat.format(System.currentTimeMillis())
            //send mood result to server
            //todo: send mood result to server
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
                                        sendMoodResult(scales.indexOfFirst { it == 1.5f } + 1)
                                        isDailySignInDialogShow.value = false
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
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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