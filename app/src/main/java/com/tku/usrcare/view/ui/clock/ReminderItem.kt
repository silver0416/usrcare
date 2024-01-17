package com.tku.usrcare.view.ui.clock

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tku.usrcare.R
import com.tku.usrcare.model.AlarmItem
import com.tku.usrcare.repository.ReminderBuilder
import com.tku.usrcare.view.component.FixedSizeText
import com.tku.usrcare.viewmodel.ClockViewModel
import java.util.Calendar


@Composable
fun ReminderItem(alarmItem: AlarmItem, clockViewModel: ClockViewModel) {

    val isActive = remember { mutableStateOf(alarmItem.isActive) }
    val week = remember { mutableStateOf(alarmItem.weekdays) }
    val hour = remember { mutableStateOf(alarmItem.hour) }
    val minute = remember { mutableStateOf(alarmItem.minute) }
    val ampm = remember { mutableStateOf("上午") }
    if (hour.value > 12) {
        hour.value -= 12
        ampm.value = "下午"
    }


    val isExpanded = remember { mutableStateOf(false) }
    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded.value) 180f else 0f, label = ""
    )
    val context = LocalContext.current

    clockViewModel.callCloseAllItem.observeForever {
        isExpanded.value = false
    }

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 15.dp)
        .wrapContentHeight()
        .clip(androidx.compose.foundation.shape.RoundedCornerShape(10.dp))
        .background(colorResource(id = com.tku.usrcare.R.color.bgClock))
        .clickable { isExpanded.value = !isExpanded.value }
        .animateContentSize()) {
        Column {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier
                        .padding(
                            start = 10.dp,
                            top = 10.dp,
                            bottom = 10.dp
                        )
                        .clip(MaterialTheme.shapes.medium)
                        .clickable {
                            isExpanded.value = !isExpanded.value
                            //todo:點擊後跳轉到編輯頁面
                        }
                ) {
                    Text(
                        text = ampm.value,
                        fontSize = 30.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Text(
                        text = "${
                            if (hour.value < 10) "0${hour.value}" else hour.value
                        }:${if (minute.value < 10) "0${minute.value}" else minute.value}",
                        fontSize = 70.sp
                    )
                }
                IconButton(
                    onClick = {
                        isExpanded.value = !isExpanded.value
                    }, modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.LightGray)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp) // 設定圖標大小
                            .rotate(rotationAngle) // 應用旋轉動畫
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 20.dp, end = 20.dp),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = alarmItem.type, fontSize = 22.sp)
                Switch(
                    checked = isActive.value, onCheckedChange = {
                        val reminderBuilder = ReminderBuilder(context.applicationContext)
                        if (isActive.value) {
                            reminderBuilder.cancelAlarm(alarmItem)
                        } else {
                            reminderBuilder.setTime(hour.value, minute.value)
                            reminderBuilder.setWeeklyAlarm(week.value)
                            reminderBuilder.setAlarm(alarmItem)
                        }
                        clockViewModel.updateReminderIsActive(
                            alarmItem.requestId, !isActive.value
                        )
                        isActive.value = !isActive.value
                    }, colors = androidx.compose.material3.SwitchDefaults.colors(
                        checkedThumbColor = colorResource(id = com.tku.usrcare.R.color.btnClockColor),
                        checkedTrackColor = colorResource(id = com.tku.usrcare.R.color.bgClockCard),
                        uncheckedThumbColor = colorResource(id = com.tku.usrcare.R.color.gray),
                        uncheckedTrackColor = Color.LightGray
                    )
                )
            }
            if (isExpanded.value) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val list = listOf("日", "一", "二", "三", "四", "五", "六")
                    val weekIdList = listOf(
                        Calendar.SUNDAY,
                        Calendar.MONDAY,
                        Calendar.TUESDAY,
                        Calendar.WEDNESDAY,
                        Calendar.THURSDAY,
                        Calendar.FRIDAY,
                        Calendar.SATURDAY
                    )
                    for (i in 0..6) {
                        item {
                            OutlinedButton(
                                onClick = {
                                    if (week.value.contains(weekIdList[i])) {
                                        week.value = week.value.filter { it != weekIdList[i] }
                                    } else {
                                        week.value = week.value + weekIdList[i]
                                    }
                                    clockViewModel.updateWeekdays(
                                        alarmItem.requestId, week.value
                                    )
                                    val reminderBuilder =
                                        ReminderBuilder(context.applicationContext)
                                    reminderBuilder.updateWeeklyAlarm(
                                        alarmItem.copy(isActive = false),
                                        week.value
                                    )
                                },
                                modifier = Modifier.size(45.dp),
                                colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (week.value.contains(
                                            weekIdList[i]
                                        )
                                    ) colorResource(
                                        id = R.color.btnInClockColor
                                    ) else Color.Transparent,
                                ),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(
                                    text = list[i],
                                    fontSize = 20.sp,
                                    style = androidx.compose.ui.text.TextStyle(
                                        color = if (week.value.contains(weekIdList[i])) colorResource(
                                            id = R.color.bgClockCard
                                        ) else Color.Gray
                                    )
                                )
                            }
                        }
                    }
                }
                val dragOffset = remember { mutableFloatStateOf(0f) }
                val maxDragDistance = 800f
                val acceptDistance = 400f
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .offset { IntOffset(dragOffset.floatValue.toInt(), 0) }
                        .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    //向右滑動方塊以刪除
                    Box(
                        modifier = Modifier
                            .size(90.dp, 50.dp)
                            .clip(androidx.compose.foundation.shape.RoundedCornerShape(10.dp))
                            .border(
                                2.dp,
                                Color.Red,
                                androidx.compose.foundation.shape.RoundedCornerShape(10.dp)
                            )
                            .background(if (dragOffset.floatValue > acceptDistance) Color.Red else Color.Transparent)
                            .draggable(orientation = Orientation.Horizontal,
                                state = rememberDraggableState { delta ->
                                    dragOffset.floatValue += delta
                                    dragOffset.floatValue =
                                        dragOffset.floatValue.coerceIn(0f, maxDragDistance)
                                },
                                onDragStopped = {
                                    // 如果拖動距離大於400，則刪除該項目
                                    if (dragOffset.floatValue > acceptDistance) {
                                        // 在這裡處裡刪除該項目
                                        ReminderBuilder(context.applicationContext).cancelAlarm(
                                            alarmItem
                                        )
                                        clockViewModel.deleteReminder(alarmItem.requestId)
                                    } else {
                                        // 恢復原來的位置
                                        dragOffset.floatValue = 0f
                                    }

                                })
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(5.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                modifier = Modifier.size(30.dp),
                                contentDescription = null,
                                tint = if (dragOffset.floatValue > acceptDistance) Color.White else Color.Red
                            )
                            FixedSizeText(
                                text = "刪除",
                                size = 35.dp,
                                color = if (dragOffset.floatValue > acceptDistance) Color.White else Color.Red
                            )
                        }
                    }

                    // 4. 滑動方塊
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(id = R.string.drag_to_delete),
                            fontSize = 15.sp,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}
