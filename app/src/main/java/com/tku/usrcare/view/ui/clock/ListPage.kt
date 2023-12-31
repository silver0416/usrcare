package com.tku.usrcare.view.ui.clock

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tku.usrcare.R
import com.tku.usrcare.model.ClockData
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.findActivity
import com.tku.usrcare.view.ui.theme.UsrcareTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@Composable
fun NoticeList(
    coroutineScope: CoroutineScope,
    offsetY: Animatable<Float, AnimationVector1D>,
    transparency: Animatable<Float, AnimationVector1D>,
    status: MutableState<Boolean>
) {
    val sheetHeight = 600f  // The height of the bottom sheet
    val context = LocalContext.current
    val activity = context.findActivity()
    val defaultOffsetY = offsetY.value

    BackHandler(true) {
        if (status.value) {
            coroutineScope.launch { offsetY.animateTo(1500f) }
            coroutineScope.launch { transparency.animateTo(0f) }
            status.value = false
        } else {
            activity?.finish()
        }
    }
    if (transparency.value != 0f) {
        Box(
            Modifier
                .fillMaxSize()
                .alpha(transparency.value)
        ) {
            Box(
                Modifier
                    .offset { IntOffset(x = 0, y = offsetY.value.roundToInt()) }
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(colorResource(id = R.color.bgClockCard))
                    .align(Alignment.BottomCenter)
                    .width(380.dp)
                    .height(Dp(sheetHeight)) // Set the height of the bottom sheet
                    .pointerInput(Unit) {
                        detectDragGestures(onDragEnd = {
                            if (offsetY.value - sheetHeight > 120f) {  // Change this to check the top position of the bottom sheet
                                coroutineScope.launch { offsetY.animateTo(1500f) }
                                coroutineScope.launch { transparency.animateTo(0f) }
                                status.value = false
                            } else {
                                coroutineScope.launch { offsetY.animateTo(-0f) }
                                coroutineScope.launch { transparency.animateTo(1f) }
                                status.value = true
                            }
                        }) { change, dragAmount ->
                            coroutineScope.launch {
                                val originalOffsetY = offsetY.value
                                val newOffsetY = (originalOffsetY + dragAmount.y).coerceIn(-0f, 1500f)
                                offsetY.snapTo(newOffsetY)
                                val originalTransparency = transparency.value
                                val newTransparency = (originalTransparency - dragAmount.y / defaultOffsetY ).coerceIn(0f, 1f)
                                transparency.snapTo(newTransparency)
                            }
                            change.consume()
                        }
                    }
            ) {
                val sessionManager = SessionManager(context)
                val clockList = sessionManager.getClock(context = context)
                val clockListCount = clockList.size

                Row {
                    Spacer(
                        modifier = Modifier
                            .width(20.dp)
                            .height(30.dp)
                    )
                    if (status.value) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "close",
                            modifier = Modifier
                                .padding(0.dp, 15.dp, 20.dp, 0.dp)
                                .size(40.dp)
                                .clickable {
                                    coroutineScope.launch { offsetY.animateTo(1500f) }
                                    coroutineScope.launch { transparency.animateTo(0f) }
                                    status.value = false
                                },
                            tint = colorResource(id = R.color.btnClockColor)
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp, 50.dp, 0.dp, 0.dp),
                    content = {
                        items(clockListCount) {
                            ListBox(clockList[it], it, status)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ListFAB(
    coroutineScope: CoroutineScope,
    offsetY: Animatable<Float, AnimationVector1D>,
    transparency: Animatable<Float, AnimationVector1D>,
    status: MutableState<Boolean>
) {
    ExtendedFloatingActionButton(
        onClick = {
            coroutineScope.launch { offsetY.animateTo(-0f) }
            coroutineScope.launch { transparency.animateTo(1f) }
            status.value = true
        },
        containerColor = colorResource(id = R.color.btnClockColor),
        contentColor = Color.White,
        modifier = Modifier
            .padding(0.dp, 0.dp, 20.dp, 20.dp)
            .clip(RoundedCornerShape(50))
            .alpha(1f - transparency.value)
    ) {
        Icon(Icons.Default.List, contentDescription = "open")
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = "開啟鬧鐘清單")
    }
}


@Composable
fun ListBox(clockData: ClockData, index: Int, status: MutableState<Boolean>) {
    val id = clockData.id
    val title = clockData.title
    val detail = clockData.detail
    val time = clockData.time
    val week = clockData.week
    val switch = clockData.switch
    val context = LocalContext.current
    val sessionManager = SessionManager(context)
    val showDelAlertDialog = remember { mutableStateOf(false) }

    DelAlarmAlertDialog(show = showDelAlertDialog, id = id, status = status)

    Box(
        modifier = Modifier
            .height(180.dp)  // 修改高度
            .fillMaxWidth()
            .padding(10.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 第一欄
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = title, fontSize = 20.sp)  // 修改字體大小
                    Text(text = detail, fontSize = 20.sp)
                    Text(
                        text = time,
                        fontSize = 35.sp
                    )
                }

                // 第二欄
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    SwitchComponent(switch, index)
                }

                // 第三欄
                Column {
                    OutlinedButton(
                        onClick = { showDelAlertDialog.value = true },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.White,
                        ),
                        border = BorderStroke(
                            width = 3.dp,
                            color = Color.Red
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "delete",
                            modifier = Modifier
                                .size(30.dp),
                            tint = colorResource(id = R.color.red)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(text = "刪除", color = colorResource(id = R.color.red))
                    }
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Week(
                    sessionManager = SessionManager(LocalContext.current),
                    index = index
                )
            }
        }
    }
}

@Composable
fun DelAlarmAlertDialog(show: MutableState<Boolean>, id: Int, status: MutableState<Boolean>) {
    val context = LocalContext.current
    val sessionManager = SessionManager(context)
    if (show.value) {
        AlertDialog(
            onDismissRequest = { show.value = false },
            title = {
                Text(text = "刪除鬧鐘")
            },
            text = {
                Text(text = "確定要刪除鬧鐘嗎？")
            },
            confirmButton = {
                Button(
                    onClick = {
                        sessionManager.removeClock(context, id)
                        status.value = false
                        status.value = true           // 重整鬧鐘清單
                        show.value = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                    )
                ) {
                    Text(text = "確定")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    show.value = false
                }) {
                    Text(text = "取消")
                }
            }
        )
    }
}

@Composable
fun Week(sessionManager: SessionManager, index: Int) {
    val context = LocalContext.current
    val week = listOf("一", "二", "三", "四", "五", "六", "日")

    // 透過 LaunchedEffect 來異步獲取 SharedPreferences 中的數據
    val weekSelected =
        remember { mutableStateOf(mutableListOf(false, false, false, false, false, false, false)) }
    LaunchedEffect(key1 = index) {
        weekSelected.value = sessionManager.getClock(context)[index].week
    }

    Row {
        for (i in week.indices) {
            Box(
                modifier = Modifier
                    .clickable {
                        weekSelected.value =
                            weekSelected.value
                                .toMutableList()
                                .also { it[i] = !it[i] }

                        val currentClockData = sessionManager.getClock(context)[index]

                        sessionManager.editClock(
                            context = context,
                            dataList = sessionManager.getClock(context),
                            position = index,
                            newClockData = ClockData(
                                id = currentClockData.id,
                                title = currentClockData.title,
                                detail = currentClockData.detail,
                                time = currentClockData.time,
                                week = weekSelected.value,
                                switch = currentClockData.switch
                            )
                        )
                    }
                    .clip(RoundedCornerShape(25))
                    .background(color = if (weekSelected.value[i]) colorResource(id = R.color.btnInClockColor) else Color.Gray)
            ) {
                Text(
                    color = Color.White,
                    text = week[i],
                    fontSize = with(LocalDensity.current) { 20.dp.toSp() },
                    modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 0.dp)
                )
            }
            if (i < week.lastIndex) {
                Spacer(modifier = Modifier.width(6.dp))
            }
        }
    }
}

@Composable
fun SwitchComponent(enable: Boolean, index: Int) {
    val context = LocalContext.current
    var isSwitchChecked by remember { mutableStateOf(enable) }
    val sessionManager = SessionManager(context)
    Switch(
        checked = isSwitchChecked,
        onCheckedChange = { isChecked ->
            isSwitchChecked = isChecked
            sessionManager.editClockSwitch(context, index, isChecked)
        },
        colors = SwitchDefaults.colors(
            checkedThumbColor = colorResource(id = R.color.btnClockColor),
            checkedTrackColor = colorResource(id = R.color.bgClockCard),
            uncheckedThumbColor = colorResource(id = R.color.gray),
            uncheckedTrackColor = colorResource(id = R.color.bgClock),
        ),
    )
}


@Preview(showBackground = true)
@Composable
fun ListBoxPreview() {
    UsrcareTheme {
        ListBox(
            clockData = ClockData(
                0,
                "title",
                "detail",
                "time",
                mutableListOf(false, false, false, false, false, false, false),
                true
            ),
            index = 0,
            status = remember { mutableStateOf(false) }
        )
    }
}

