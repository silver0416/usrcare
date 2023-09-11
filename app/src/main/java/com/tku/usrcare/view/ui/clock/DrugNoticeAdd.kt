package com.tku.usrcare.view.ui.clock

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tku.usrcare.R
import com.tku.usrcare.model.ClockData
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.flag
import com.tku.usrcare.view.ui.theme.UsrcareTheme
import java.util.UUID

@Composable
fun Drug(navController: NavHostController) {
    val context = LocalContext.current
    val name = stringResource(R.string.drug_reminder)
    var detail by remember { mutableStateOf("") }
    val selectedTime = remember { mutableStateOf("") }
    val sessionManager = SessionManager(context)
    var selectedTimeOption by remember { mutableStateOf("") }
    val isEditAlertDialogVisible = remember { mutableStateOf(false) }
    val drugOpt =
        listOf("降血壓", "降血糖", "降血脂", "鈣補充", "胃藥", "消炎", "助眠藥", "抗生素", "維他命")
    val isChooseTimeAlertDialogVisible = remember { mutableStateOf(false) }

    @Composable
    fun Options(option: String) {
        OutlinedButton(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(20.dp))
                .background(Color.White),
            onClick = {
                detail = option
                isEditAlertDialogVisible.value = false
            }
        ) {
            Text(text = option, fontSize = 20.sp, color = Color.Black)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.bgClock))
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isEditAlertDialogVisible.value) { // 如果openDialog的值為true，則顯示對話框
            AlertDialog(
                onDismissRequest = { isEditAlertDialogVisible.value = false }, // 點擊外部關閉對話框
                title = {},
                text = {
                    Column(
                        modifier = Modifier.padding(13.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        for (i in drugOpt.indices step 2/*每兩個一排*/) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                for (j in i..i + 1) {
                                    if (j < drugOpt.size) {
                                        Options(option = drugOpt[j])
                                        Spacer(modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 0.dp))
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(onClick = { isEditAlertDialogVisible.value = false }) {
                        Text(stringResource(id = R.string.cancel))
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        detail = ""
                        isEditAlertDialogVisible.value = false
                    }) {
                        Text(stringResource(id = R.string.clear))
                    }
                }
            )
        }
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
                text = stringResource(R.string.drug_reminder),
                fontSize = 40.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.height(25.dp))
        Column {
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = stringResource(R.string.enter_drug_name), fontSize = 20.sp)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = detail,
                onValueChange = { newValue ->
                    detail = newValue
                },
                Modifier
                    .shadow(15.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .width(320.dp)
                    .height(50.dp),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = colorResource(id = R.color.black),
                    backgroundColor = colorResource(id = R.color.white),
                    focusedIndicatorColor = colorResource(id = R.color.white),
                    unfocusedIndicatorColor = colorResource(id = R.color.white),
                    disabledIndicatorColor = colorResource(id = R.color.white)
                ),
                trailingIcon = { // 在尾端添加按鈕
                    Icon(
                        painter = painterResource(id = R.drawable.btn_edit), // 使用 drawable 中的圖示
                        contentDescription = "自定義按鈕",
                        tint = Color.Blue, // 圖示顏色
                        modifier = Modifier
                            .clickable {
                                isEditAlertDialogVisible.value = true
                            }
                            .padding(0.dp, 2.dp, 8.dp, 0.dp) // 圖示大小
                    )
                },
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box() {

            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = stringResource(R.string.enter_drug_time), fontSize = 20.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Row {
                val optionTimes = listOf("08:00", "12:00", "18:00", "22:00")
                LazyColumn {
                    items(optionTimes.size) { index ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(0.dp, 0.dp, 0.dp, 6.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            RadioButton(
                                selected = (selectedTimeOption == optionTimes[index]),
                                onClick = {
                                    selectedTimeOption = optionTimes[index]
                                    //轉換為系統鬧鐘可用的時間格式
                                    selectedTime.value = optionTimes[index]
                                },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = colorResource(id = R.color.btnInClockColor),
                                    unselectedColor = colorResource(id = R.color.gray)
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = optionTimes[index],
                                fontSize = 20.sp,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
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
            Button(
                onClick = {
                    if (selectedTime.value != "") {
                        if (!flag) {
                            sessionManager.saveTempWeek(
                                context = context,
                                mutableListOf(true, true, true, true, true, true, true)
                            )
                        }
                        val getAlarmIdList = sessionManager.getAlarmIdList(context = context)
                        val alarmId = (UUID.randomUUID().toString() + name).hashCode()
                        getAlarmIdList.add(alarmId)
                        sessionManager.saveAlarmIdList(context = context, getAlarmIdList)
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
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            sessionManager.saveClock(context = context, oldData)
                        }
                        navController.popBackStack()
                    } else {
                        //請選擇時間
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


@Composable
fun SimpleWeek(weekSelected: MutableState<MutableList<Boolean>>) {
    val week = listOf("一", "二", "三", "四", "五", "六", "日")
    val sessionManager = SessionManager(LocalContext.current)
    val context = LocalContext.current
    Row {
        for (i in week.indices) {
            Box(
                modifier = Modifier
                    .clickable {
                        weekSelected.value = weekSelected.value
                            .toMutableList()
                            .apply { this[i] = !this[i] }
                        sessionManager.saveTempWeek(
                            context = context,
                            MutableList(7) { i -> weekSelected.value[i] })
                        flag = true
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


@Preview(showBackground = true)
@Composable
fun DrugPreview() {
    UsrcareTheme {
        Drug(navController = rememberNavController())
    }
}