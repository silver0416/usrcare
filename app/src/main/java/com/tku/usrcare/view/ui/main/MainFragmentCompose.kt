package com.tku.usrcare.view.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tku.usrcare.R
import com.tku.usrcare.view.component.FixedSizeText
import com.tku.usrcare.view.component.Loading


@ExperimentalMaterial3Api
@Composable
fun MainFragmentDialogs() {
    val isDailySignInDialogShow = remember { mutableStateOf(true) }
    if (isDailySignInDialogShow.value) {
        DailySignInDialog(isDailySignInDialogShow)
    }
}


@Composable
fun DailySignInDialog(isDailySignInDialogShow: MutableState<Boolean>) {
    val content = remember { mutableStateOf("dailyMood") }
    androidx.compose.material3.AlertDialog(
        onDismissRequest = { isDailySignInDialogShow.value = false },
        title = { Text(text = "每日簽到") },
        text = {
            when (content.value) {
                "dailyMood" -> DailySignInContent()
                "loading" -> Loading(isVisible = true)
                else -> isDailySignInDialogShow.value = false
            }
        },
        confirmButton = {
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DailySignInContent() {
    val imageIds = listOf(
        R.drawable.ic_mood1,
        R.drawable.ic_mood2,
        R.drawable.ic_mood3,
        R.drawable.ic_mood4,
        R.drawable.ic_mood5
    )

    @Composable
    fun ZoomableImageRow(imageIds: List<Int>) {
        val scales = remember { mutableStateListOf(*Array(imageIds.size) { 1f }) }
        val imageSize = 50.dp  // 新的圖片大小
        val imagePadding = 5.dp  // 新的間隔大小
        val totalSizeWithPadding = (imageSize + imagePadding * 2) - 5.dp  // 新的總大小
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(Unit) {  // 重新添加 pointerInput
                    awaitPointerEventScope {
                        while (true) {
                            var offsetX = 0f
                            val event = awaitPointerEvent()
                            val position = event.changes.firstOrNull()?.position ?: continue
                            val newScales = scales.toList().toMutableList()
                            newScales.forEachIndexed { index, _ ->
                                val start = offsetX
                                val end = offsetX + totalSizeWithPadding.toPx()
                                newScales[index] = if (position.x in start..end) {
                                    1.5f
                                } else {
                                    1f
                                }
                                offsetX += totalSizeWithPadding.toPx()
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
@Preview
fun DailySignInDialogPreview() {
    DailySignInDialog(isDailySignInDialogShow = remember { mutableStateOf(true) })
}