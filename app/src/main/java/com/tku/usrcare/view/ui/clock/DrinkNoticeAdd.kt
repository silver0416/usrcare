package com.tku.usrcare.view.ui.clock

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.tku.usrcare.R
import com.tku.usrcare.model.ClockData
import com.tku.usrcare.repository.SessionManager
import java.util.UUID

@Composable
fun Drink(navController: NavHostController) {
    val context = LocalContext.current
    val name = stringResource(R.string.drink_water_reminder)
    val detail by remember { mutableStateOf("") }
    val selectedTime = remember { mutableStateOf("") }
    val sessionManager = SessionManager(context)
    var selectedTimeOption by remember { mutableStateOf("") }
    val isChooseTimeAlertDialogVisible = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.bgClock))
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isChooseTimeAlertDialogVisible.value) {
            AlertDialog(onDismissRequest = { isChooseTimeAlertDialogVisible.value = false },
                title = { Text(text = stringResource(R.string.please_choose_time)) },
                text = { Text(text = stringResource(R.string.please_choose_time)) },
                confirmButton = {
                    Button(onClick = { isChooseTimeAlertDialogVisible.value = false }) {
                        Text(text = stringResource(R.string.confirm))
                    }
                })
        }
        Box(
            modifier = Modifier
                .width(282.dp)
                .height(93.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(colorResource(id = R.color.MainButtonColor))
                .align(Alignment.CenterHorizontally)
        ) {

            Text(
                text = stringResource(R.string.drink_water_reminder),
                fontSize = 40.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.height(25.dp))
        Column {
            Text(text = stringResource(R.string.enter_drink_time), fontSize = 25.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.Start) {
                val optionTimes = listOf(
                    "6:30", "08:00", "10:30", "12:00", "14:30", "16:00", "18:30", "20:00", "22:30"
                )
                // 分割列表到兩個部分
                val midPoint = optionTimes.size / 2

                @Composable
                fun TimeOptionRow(time: String, selectedTime: MutableState<String>) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        RadioButton(
                            selected = (selectedTimeOption == time),
                            onClick = {
                                selectedTimeOption = time
                                //轉換為系統鬧鐘可用的時間格式
                                selectedTime.value = time
                            },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = colorResource(id = R.color.btnInClockColor),
                                unselectedColor = colorResource(id = R.color.gray)
                            )
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = time,
                            fontSize = 28.sp,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.Start) { // 外部Row
                    Column(modifier = Modifier.weight(1f)) { // 第一個Column
                        for (index in 0 until midPoint) {
                            Spacer(modifier = Modifier.height(10.dp))
                            TimeOptionRow(time = optionTimes[index], selectedTime = selectedTime)
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) { // 第二個Column
                        for (index in midPoint until optionTimes.size) {
                            Spacer(modifier = Modifier.height(10.dp))
                            TimeOptionRow(time = optionTimes[index],  selectedTime = selectedTime)
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }



            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        SimpleWeek(remember {
            mutableStateOf(
                mutableListOf(
                    true,
                    true,
                    true,
                    true,
                    true,
                    true,
                    true
                )
            )
        })
        Spacer(modifier = Modifier.height(16.dp))
        Row {

            Button(
                onClick = {
                    navController.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.gray))
            ) {
                Text(text = stringResource(R.string.previous))
            }
            Spacer(modifier = Modifier.width(40.dp))
            sessionManager.saveTempWeek(
                context = context,
                mutableListOf(true, true, true, true, true, true, true)
            )
            Button(
                onClick = {
                    if (selectedTime.value != "") {
                        val alarmId = (UUID.randomUUID().toString() + name).hashCode()
                        val clockData = ClockData(
                            alarmId,
                            name,
                            detail,
                            selectedTime.value,
                            sessionManager.getTempWeek(context = context),
                            true
                        )
                        val oldData = sessionManager.getClock(context = context)
                        oldData.add(clockData)
                        sessionManager.saveClock(context = context, oldData)
                        navController.popBackStack()
                    } else {
                        isChooseTimeAlertDialogVisible.value = true
                    }
                },
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.btnInClockColor))
            ) {
                Text(text = stringResource(R.string.next))
            }
        }
    }

}