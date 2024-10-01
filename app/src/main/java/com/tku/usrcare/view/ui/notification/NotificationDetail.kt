package com.tku.usrcare.view.ui.notification

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tku.usrcare.R
import com.tku.usrcare.model.BroadcastData
import com.tku.usrcare.repository.TimeTools
import com.tku.usrcare.viewmodel.NotificationViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationDetail(
    broadcastData : BroadcastData,
    notificationViewModel : NotificationViewModel,
    sheetState : BottomSheetScaffoldState,
    coroutineScope : CoroutineScope
) {
    val lazyListState = remember { LazyListState() }
    val firstVisibleItemIndex =
        remember { derivedStateOf { lazyListState.firstVisibleItemIndex } }
    
    Box(
        modifier = androidx.compose.ui.Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = androidx.compose.ui.Modifier
                .fillMaxSize()
                .padding(start = 40.dp, end = 40.dp, top = 10.dp),
            state = lazyListState
        ) {
            item {
                // 顯示通知標題
                Text(// 顯示通知標題
                    text = broadcastData.title,
                    fontSize = 20.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )// 顯示通知日期
                Text(text = TimeTools.whatDate(if (broadcastData.time != "") broadcastData.time.toLong() else 0L))
                // 顯示通知類型(公告、活動、更新、緊急)
                Text(text = broadcastData.type)
            }
            item {
                // 顯示通知內容
                Text(
                    text = broadcastData.message,
                    fontSize = 16.sp
                )
            }
            item {
                Spacer(modifier = androidx.compose.ui.Modifier.height(100.dp))
            }
        }
        val showToTopButton = remember { mutableStateOf(false) }
        showToTopButton.value = firstVisibleItemIndex.value != 0
        androidx.compose.animation.AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 20.dp, end = 30.dp)
                .size(60.dp),
            visible = showToTopButton.value,
            enter = fadeIn(animationSpec = tween(durationMillis = 200)) + scaleIn(
                animationSpec = tween(durationMillis = 200)
            ),
            exit = fadeOut(animationSpec = tween(durationMillis = 200)) + scaleOut(
                animationSpec = tween(durationMillis = 200)
            ),
        ) {
            Button(
                onClick = {
                    coroutineScope.launch {
                        lazyListState.animateScrollToItem(
                            0
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.white)
                ),
                shape = CircleShape,
                border = BorderStroke(2.dp, colorResource(id = R.color.black)),
                contentPadding = PaddingValues(0.dp)
            ) {
                Image(
                    Icons.Filled.KeyboardArrowUp,
                    contentDescription = "up",
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }

}