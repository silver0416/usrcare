package com.tku.usrcare.view.ui.clock

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tku.usrcare.R
import com.tku.usrcare.model.AlarmItem
import com.tku.usrcare.repository.ReminderBuilder
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.repository.UniqueCode
import com.tku.usrcare.view.component.AutoSizedText
import com.tku.usrcare.view.component.keyboardAsState
import com.tku.usrcare.viewmodel.ClockViewModel
import com.tku.usrcare.viewmodel.ViewModelFactory
import java.time.LocalDateTime
import java.util.Calendar

private lateinit var clockViewModel: ClockViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewReminder(navController: NavController, title: String) {

    val context = LocalContext.current
    val sessionManager = SessionManager(context)
    val viewModelFactory = ViewModelFactory(sessionManager)
    clockViewModel = viewModel(
        viewModelStoreOwner = context as ViewModelStoreOwner, factory = viewModelFactory
    )
    val reminderName = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val addPresetNameDialog = remember { mutableStateOf(false) }
    val addPresetName = remember { mutableStateOf("") }
    val timePicker = remember { mutableStateOf(false) }
    val selectedHour = remember { mutableIntStateOf(0) }
    val selectedMinute = remember { mutableIntStateOf(0) }
    val isTimePicker = remember { mutableStateOf(true) }
    val addedTimes = remember { mutableStateListOf<AlarmItem>() }
    val targetIndex = remember { mutableIntStateOf(-1) }


    if (addPresetNameDialog.value) {
        AlertDialog(onDismissRequest = {
            addPresetNameDialog.value = false
        }, confirmButton = {
            Button(
                onClick = {
                    if (addPresetName.value == "") {
                        return@Button
                    }
                    clockViewModel.addPresetName(name = addPresetName.value, title = title)
                    addPresetName.value = ""
                    addPresetNameDialog.value = false
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.btnInClockColor),
                ),
            ) {
                Text(text = "輸入")
            }
        },
            dismissButton = {
                TextButton(onClick = { addPresetNameDialog.value = false }) {
                    addPresetName.value = ""
                    Text(text = "取消", color = colorResource(id = R.color.btnInClockColor))
                }
            }, title = {
                Text(
                    text = "請輸入要新增的預設名稱",
                )
            }, text = {
                val isKeyboardOpen by keyboardAsState()
                val focusRequester = remember { FocusRequester() }
                val keyboardController = LocalSoftwareKeyboardController.current
                val focusManagerInDiaLog = LocalFocusManager.current
                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                    keyboardController?.show()
                }
                OutlinedTextField(
                    value = addPresetName.value,
                    onValueChange = { if (it.length <= 8) addPresetName.value = it },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = colorResource(id = R.color.btnClockColor),
                        unfocusedIndicatorColor = colorResource(id = R.color.gray).copy(alpha = 0.5f),
                        cursorColor = colorResource(id = R.color.gray),
                    ),
                    textStyle = TextStyle(
                        textAlign = TextAlign.Center, fontSize = 30.sp
                    ),
                    modifier = Modifier
                        .focusRequester(focusRequester),
                    interactionSource = remember { MutableInteractionSource() }         //此處用意在當點擊TextField時，清除Focus再重新Focus，並顯示鍵盤
                        .also { interactionSource ->
                            LaunchedEffect(interactionSource) {
                                interactionSource.interactions.collect {
                                    if (it is PressInteraction.Release) {
                                        if (isKeyboardOpen.toString() == "Closed") {
                                            focusManagerInDiaLog.clearFocus()
                                            focusRequester.requestFocus()
                                            keyboardController?.show()
                                        }
                                    }
                                }
                            }
                        }
                )
            })
    }
    if (timePicker.value) {

        val nowHr = LocalDateTime.now().hour
        val nowMin = LocalDateTime.now().minute
        if (targetIndex.intValue != -1) {
            selectedHour.intValue = addedTimes[targetIndex.intValue].hour
            selectedMinute.intValue = addedTimes[targetIndex.intValue].minute
        } else {
            selectedHour.intValue = nowHr
            selectedMinute.intValue = if (nowMin != 59) {
                nowMin + 1
            } else {
                0
            }
        }
        val timePickerState = rememberTimePickerState(
            initialHour = selectedHour.intValue,
            initialMinute = selectedMinute.intValue
        )
        Dialog(
            onDismissRequest = { timePicker.value = false },
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(colorResource(id = R.color.bgClock))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (isTimePicker.value) {
                        TimePicker(
                            state = timePickerState,
                            colors = androidx.compose.material3.TimePickerDefaults.colors(
                                selectorColor = colorResource(id = R.color.btnInClockColor),
                                timeSelectorSelectedContainerColor = colorResource(id = R.color.bgClockCard),
                                periodSelectorSelectedContainerColor = colorResource(id = R.color.bgClockCard),
                            ),
                            modifier = Modifier
                                .background(colorResource(id = R.color.bgClock))
                        )
                    } else {
                        TimeInput(
                            state = timePickerState,
                            colors = androidx.compose.material3.TimePickerDefaults.colors(
                                selectorColor = colorResource(id = R.color.btnInClockColor),
                                timeSelectorSelectedContainerColor = colorResource(id = R.color.bgClockCard),
                                periodSelectorSelectedContainerColor = colorResource(id = R.color.bgClockCard),
                            ),
                            modifier = Modifier
                                .background(colorResource(id = R.color.bgClock))
                        )
                    }
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(0.35f)
                                .padding(start = 10.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            IconButton(onClick = {
                                isTimePicker.value = !isTimePicker.value
                            }) {
                                Icon(
                                    painter = if (isTimePicker.value) {
                                        painterResource(id = R.drawable.ic_keyboard)
                                    } else {
                                        painterResource(id = R.drawable.ic_clock)
                                    },
                                    contentDescription = "TimeEnterType",
                                )
                            }
                        }
                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.weight(0.65f)
                        ) {
                            TextButton(
                                onClick = { timePicker.value = false },
                            ) {
                                Text(
                                    text = "取消",
                                    color = colorResource(id = R.color.btnInClockColor)
                                )
                            }
                            Spacer(modifier = Modifier.size(10.dp))
                            Button(
                                onClick = {
                                    if (targetIndex.intValue != -1) {
                                        val oldData = addedTimes[targetIndex.intValue]
                                        val newData = AlarmItem(
                                            type = oldData.type,
                                            description = oldData.description,
                                            oldData.requestId,
                                            hour = timePickerState.hour,
                                            minute = timePickerState.minute,
                                            weekdays = oldData.weekdays,
                                            false
                                        )
                                        addedTimes[targetIndex.intValue] = newData
                                        timePicker.value = false
                                    } else {
                                        AlarmItem(
                                            type = title,
                                            description = reminderName.value,
                                            requestId = UniqueCode.getNextCode(context),
                                            hour = timePickerState.hour,
                                            minute = timePickerState.minute,
                                            weekdays = mutableListOf(),
                                            true
                                        ).let { addedTimes.add(it) }
                                        timePicker.value = false
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorResource(id = R.color.btnInClockColor),
                                ),
                            ) {
                                Text(text = "確定")
                            }
                        }

                    }
                }
            }
        }

    }

    Scaffold(topBar = {
        NewReminderTitle(title = title)
    }, bottomBar = {
        NewReminderBottomBar(
            navController = navController,
            addedTimes = addedTimes
        )
    }, modifier = Modifier
        .pointerInput(Unit) {
            detectTapGestures(onTap = {
                focusManager.clearFocus()
            })
        }
        .fillMaxSize(), containerColor = colorResource(id = R.color.bgClock)) {
        NewReminderContent(
            paddingValues = it,
            title = title,
            reminderName,
            addPresetNameDialog = addPresetNameDialog,
            timePicker = timePicker,
            addedTimes = addedTimes,
            targetIndex = targetIndex
        )
    }
}


@Composable
fun NewReminderTitle(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 45.dp, end = 45.dp, top = 15.dp, bottom = 15.dp)
            .wrapContentHeight()
            .clip(MaterialTheme.shapes.medium)
            .background(colorResource(id = R.color.white))
            .border(
                width = 3.dp,
                color = colorResource(id = R.color.btnInClockColor),
                shape = MaterialTheme.shapes.medium
            ), contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Box(
            modifier = Modifier.padding(10.dp),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            AutoSizedText(
                text = title,
                size = 45,
            )
        }
    }
}

@Composable
fun NewReminderBottomBar(
    navController: NavController,
    addedTimes: SnapshotStateList<AlarmItem>,
) {
    val context = LocalContext.current

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        Button(
            onClick = { navController.navigateUp() },
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.white),
            ),
            modifier = Modifier
                .padding(10.dp)
                .clip(MaterialTheme.shapes.medium)
                .border(
                    width = 3.dp,
                    color = colorResource(id = R.color.red),
                    shape = MaterialTheme.shapes.medium
                ),
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Cancel",
                    tint = colorResource(id = R.color.red)
                )
                AutoSizedText(
                    text = "取消",
                    color = colorResource(id = R.color.black),
                    size = 20,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
            }
        }
        Button(
            onClick = {
                for (i in addedTimes) {
                    val reminderBuilder = ReminderBuilder(context.applicationContext)
                    reminderBuilder.setTime(i.hour, i.minute)
                    reminderBuilder.setWeeklyAlarm(i.weekdays)
                    reminderBuilder.setAlarm(alarmItem = addedTimes[addedTimes.indexOf(i)])
                    clockViewModel.addReminder(i)
                }

                navController.navigateUp()
            },
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.white),
            ),
            modifier = Modifier
                .padding(10.dp)
                .clip(MaterialTheme.shapes.medium)
                .border(
                    width = 3.dp,
                    color = colorResource(id = R.color.btnInClockColor),
                    shape = MaterialTheme.shapes.medium
                ),
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_checkmark),
                    contentDescription = "save",
                    modifier = Modifier.size(28.dp)
                )
                AutoSizedText(
                    text = "儲存",
                    color = colorResource(id = R.color.black),
                    size = 20,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewReminderContent(
    paddingValues: PaddingValues,
    title: String,
    reminderName: MutableState<String>,
    addPresetNameDialog: MutableState<Boolean>,
    timePicker: MutableState<Boolean>,
    addedTimes: SnapshotStateList<AlarmItem>,
    targetIndex: MutableState<Int>
) {

    Box(modifier = Modifier.padding(paddingValues)) {
        LazyColumn(
            modifier = Modifier
                .padding(
                    top = 10.dp, bottom = 10.dp, start = 20.dp, end = 20.dp
                )
                .animateContentSize()
        ) {
            item { NameEditor(title, reminderName, addPresetNameDialog = addPresetNameDialog) }
            item { Spacer(modifier = Modifier.size(10.dp)) }
            item { SetTime() }
            item { Spacer(modifier = Modifier.size(10.dp)) }
            items(addedTimes.size, key = { addedTimes[it].requestId })
            { index ->
                Column(modifier = Modifier.animateItemPlacement()) {
                    TimeBox(timePicker, addedTimes, index, targetIndex)
                    Spacer(modifier = Modifier.size(10.dp))
                }
            }
            item { AddTime(timePicker, targetIndex) }
        }
    }
}


@Composable
fun NameEditor(
    title: String, reminderName: MutableState<String>, addPresetNameDialog: MutableState<Boolean>
) {
    val openSelectSession = remember { mutableStateOf(false) }

    if (openSelectSession.value) {
        BackHandler {
            openSelectSession.value = false
        }
    }

    Column {
        Text(
            text = "請輸入名稱",
            color = colorResource(id = R.color.black),
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.size(10.dp))
        Box {
            Column(modifier = Modifier.animateContentSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(
                            if (!openSelectSession.value) {
                                MaterialTheme.shapes.medium
                            } else {
                                MaterialTheme.shapes.medium.copy(
                                    bottomStart = CornerSize(0.dp), bottomEnd = CornerSize(0.dp)
                                )
                            }
                        )
                        .background(colorResource(id = R.color.white)),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val containerColor = colorResource(id = R.color.white)
                    val keyboardController = LocalSoftwareKeyboardController.current
                    val focusRequester = remember { FocusRequester() }
                    val focusManager = LocalFocusManager.current
                    val isKeyboardOpen by keyboardAsState()

                    TextField(value = reminderName.value,
                        onValueChange = {
                            // 限制在8個字以內
                            if (it.length <= 8) {
                                reminderName.value = it
                            }
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = containerColor,
                            unfocusedContainerColor = containerColor,
                            disabledContainerColor = containerColor,
                            cursorColor = colorResource(id = R.color.btnInClockColor),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                        maxLines = 1,
                        singleLine = true,
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .weight(0.65f),
                        interactionSource = remember { MutableInteractionSource() }         //此處用意在當點擊TextField時，清除Focus再重新Focus，並顯示鍵盤
                            .also { interactionSource ->
                                LaunchedEffect(interactionSource) {
                                    interactionSource.interactions.collect {
                                        if (it is PressInteraction.Release) {
                                            if (isKeyboardOpen.toString() == "Closed") {
                                                focusManager.clearFocus()
                                                focusRequester.requestFocus()
                                                keyboardController?.show()
                                            }
                                        }
                                    }
                                }
                            })
                    Button(
                        onClick = { openSelectSession.value = !openSelectSession.value },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.bgClockCard),
                        ),
                        modifier = Modifier
                            .padding(10.dp)
                            .weight(0.35f)
                            .clip(MaterialTheme.shapes.medium),
                        contentPadding = PaddingValues(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_mood_puncher),
                                contentDescription = "edit",
                                tint = colorResource(id = R.color.btnClockColor),
                                modifier = Modifier.size(28.dp)
                            )
                            AutoSizedText(
                                text = stringResource(id = R.string.select),
                                color = colorResource(id = R.color.btnClockColor),
                                size = 20
                            )
                        }
                    }
                }
                if (openSelectSession.value) {
                    SelectSession(
                        title,
                        reminderName = reminderName,
                        addPresetNameDialog = addPresetNameDialog,
                        openSelectSession = openSelectSession
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SelectSession(
    title: String,
    reminderName: MutableState<String>,
    addPresetNameDialog: MutableState<Boolean>,
    openSelectSession: MutableState<Boolean>
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .animateContentSize()
            .clip(
                MaterialTheme.shapes.medium.copy(
                    topStart = CornerSize(0.dp), topEnd = CornerSize(0.dp)
                )
            )
            .background(colorResource(id = R.color.white))
    ) {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .animateContentSize()
                .padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 10.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            val presetNameList = remember {
                ClockViewModel.getReminderPresetList(clockViewModel, title)
            }
            clockViewModel.callUpdatePresetNameList.observeForever {
                if (it) {
                    presetNameList.value = mutableListOf("")
                    presetNameList.value = ClockViewModel.getReminderPresetList(
                        clockViewModel,
                        title).value
                }
            }
            for (i in presetNameList.value) {
                Button(
                    onClick = {
                        reminderName.value = i
                        openSelectSession.value = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.strokeGray),
                    ),
                    modifier = Modifier.padding(10.dp),
                    shape = MaterialTheme.shapes.large,
                    border = BorderStroke(2.dp, colorResource(id = R.color.black)),
                    contentPadding = PaddingValues(14.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 5.dp, pressedElevation = 5.dp, disabledElevation = 5.dp
                    )
                ) {
                    AutoSizedText(
                        text = i, color = colorResource(id = R.color.black), size = 20
                    )
                }
            }
            Button(
                onClick = {
                    addPresetNameDialog.value = true
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.bgClockCard),
                ),
                modifier = Modifier.padding(10.dp),
                shape = MaterialTheme.shapes.large,
                border = BorderStroke(2.dp, colorResource(id = R.color.black)),
                contentPadding = PaddingValues(14.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 5.dp, pressedElevation = 5.dp, disabledElevation = 5.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "add",
                    tint = colorResource(id = R.color.black),
                    modifier = Modifier.size(28.dp)
                )
                AutoSizedText(
                    text = "新增預設名稱", color = colorResource(id = R.color.black), size = 20
                )
            }
        }
    }
}

@Composable
fun SetTime() {
    Column {
        Text(
            text = "請新增時間及選擇重複日",
            color = colorResource(id = R.color.black),
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            fontSize = 20.sp
        )
    }
}

@Composable
fun AddTime(timePicker: MutableState<Boolean>, targetIndex: MutableState<Int>) {
    Button(
        onClick = {
            targetIndex.value = -1
            timePicker.value = true
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.white),
        ),
        shape = MaterialTheme.shapes.small,
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "add",
            tint = colorResource(id = R.color.btnInClockColor),
            modifier = Modifier.size(28.dp)
        )
        AutoSizedText(
            text = "新增時間",
            color = colorResource(id = R.color.btnInClockColor),
            size = 20,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
        )
    }
}


@Composable
fun TimeBox(
    timePicker: MutableState<Boolean>,
    addedTimes: SnapshotStateList<AlarmItem>,
    index: Int,
    targetIndex: MutableState<Int>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(colorResource(id = R.color.white)),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier
            .padding(10.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(colorResource(id = R.color.bgClockCard))
            .clickable {
                targetIndex.value = index
                timePicker.value = true
            }) {
            Row(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val adjustedHour = if (addedTimes[index].hour < 10) {
                    "0${addedTimes[index].hour}"
                } else {
                    if (addedTimes[index].hour > 12 && addedTimes[index].hour - 12 < 10) {
                        "0${addedTimes[index].hour - 12}"
                    } else if (addedTimes[index].hour > 12 && addedTimes[index].hour - 12 >= 10) {
                        "${addedTimes[index].hour - 12}"
                    } else {
                        addedTimes[index].hour.toString()
                    }
                }
                val adjustedMinute = if (addedTimes[index].minute < 10) {
                    "0${addedTimes[index].minute}"
                } else {
                    addedTimes[index].minute.toString()
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AutoSizedText(
                        text = if (addedTimes[index].hour > 11) "下午" else "上午",
                        size = 20,
                        color = colorResource(id = R.color.btnClockColor)
                    )
                    AutoSizedText(
                        text = "${adjustedHour}:${adjustedMinute}",
                        size = 48,
                        color = colorResource(id = R.color.btnClockColor)
                    )
                }
                IconButton(onClick = {
                    addedTimes.removeAt(index)
                }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "delete",
                        tint = colorResource(id = R.color.btnInClockColor),
                        modifier = Modifier.size(38.dp)
                    )
                }
            }
        }
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val list = listOf("日", "一", "二", "三", "四", "五", "六")
            val weekIdList =
                listOf(
                    Calendar.SUNDAY,
                    Calendar.MONDAY,
                    Calendar.TUESDAY,
                    Calendar.WEDNESDAY,
                    Calendar.THURSDAY,
                    Calendar.FRIDAY,
                    Calendar.SATURDAY
                )

            fun isIndexInWeekdays(i: Int): Boolean {
                return try {
                    addedTimes[index].weekdays.contains(weekIdList[i])
                } catch (indexOutOfBoundsException: IndexOutOfBoundsException) {
                    false
                }
            }

            for (i in list) {
                item {
                    OutlinedButton(
                        onClick = {
                            if (addedTimes[index].weekdays.contains(weekIdList[list.indexOf(i)])) {
                                addedTimes[index] = AlarmItem(
                                    addedTimes[index].type,
                                    addedTimes[index].description,
                                    addedTimes[index].requestId,
                                    addedTimes[index].hour,
                                    addedTimes[index].minute,
                                    addedTimes[index].weekdays.filter {
                                        it != weekIdList[list.indexOf(
                                            i
                                        )]
                                    },
                                    addedTimes[index].isActive
                                )
                            } else {
                                addedTimes[index] = AlarmItem(
                                    addedTimes[index].type,
                                    addedTimes[index].description,
                                    addedTimes[index].requestId,
                                    addedTimes[index].hour,
                                    addedTimes[index].minute,
                                    addedTimes[index].weekdays + weekIdList[list.indexOf(i)],
                                    addedTimes[index].isActive
                                )
                            }
                        },
                        modifier = Modifier
                            .size(50.dp)
                            .padding(5.dp),
                        contentPadding = PaddingValues(0.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isIndexInWeekdays(list.indexOf(i))) {
                                colorResource(id = R.color.btnInClockColor)
                            } else {
                                Color.Transparent
                            },
                        )
                    ) {
                        Text(
                            text = i, color =
                            if (isIndexInWeekdays(list.indexOf(i))) {
                                colorResource(id = R.color.white)
                            } else {
                                colorResource(id = R.color.black)
                            }
                        )
                    }
                }
            }
        }
    }
}


