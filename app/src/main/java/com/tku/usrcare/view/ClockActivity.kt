package com.tku.usrcare.view

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.tku.usrcare.R
import com.tku.usrcare.view.ui.theme.UsrcareTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tku.usrcare.model.ClockData
import com.tku.usrcare.repository.SessionManager
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class ClockActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            UsrcareTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    ClockNav(navController)
                }
            }
        }
    }
}

sealed class Screen(val route: String) {
    object Main : Screen("Main")
    object Drug : Screen("Drug")
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ClockNav(navController: NavHostController) {
    NavHost(navController, startDestination = Screen.Main.route) {
        composable(Screen.Main.route) {
            Main(navController)
        }
        composable(Screen.Drug.route) {
            Drug(navController)
        }
    }
}


fun Context.findActivity(): Activity? {
    var currentContext = this
    while (currentContext is ContextWrapper) {
        if (currentContext is Activity) {
            return currentContext
        }
        currentContext = currentContext.baseContext
    }
    return null
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Main(navController: NavHostController) {
    Box(
        Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.bgClock))
    ) {
        TitleBox()
        CenterButtons(navController)
        NoticeList()
    }
}

@Composable
fun Drug(navController: NavHostController) {
    val context = LocalContext.current
    val name = stringResource(R.string.drug_reminder)
    var detail by remember { mutableStateOf("") }
    val selectedTime = remember { mutableStateOf("") }
    val sessionManager = SessionManager(context)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.bgClock))
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
        Column() {
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = stringResource(R.string.enter_drug_name), fontSize = 20.sp)
            Spacer(modifier = Modifier.height(6.dp))
            TextField(
                value = detail,
                onValueChange = { detail = it },
                label = {
                    Text(
                        text = stringResource(R.string.enter_drug_name),
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                })
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = stringResource(R.string.enter_drug_time), fontSize = 20.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Row() {
                Button(
                    onClick = {
                        TimePickerDialog(context, { _, hour, minute ->
                            selectedTime.value = "$hour:$minute"
                        }, 12, 0, true).show()
                    },
                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.btnInClockColor))
                ) {
                    Text("選擇時間")
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = selectedTime.value, fontSize = 27.sp)
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
        Row() {

            Button(
                onClick = {
                    navController.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.gray))
            ) {
                Text(text = stringResource(R.string.previous))
            }
            Spacer(modifier = Modifier.width(40.dp))
            Button(onClick = {
                val clockData = ClockData(
                    name,
                    detail,
                    selectedTime.value,
                    sessionManager.getTempWeek(context = context),
                    true
                )
                val oldData = sessionManager.getClock(context = context)
                val dataList = oldData ?: mutableListOf()
                dataList.add(clockData)
                sessionManager.saveClock(context = context, dataList)

                navController.popBackStack()
            },
            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.btnInClockColor))
                ) {
                Text(text = stringResource(R.string.next))
            }
        }
    }
}


@Composable
fun TitleBox() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val activity = context.findActivity()
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        Card(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
                .shadow(15.dp, RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(colorResource(id = R.color.btnClockColor))
        ) {
            Row(
                modifier = Modifier
                    .width(320.dp)
                    .padding(3.dp, 10.dp, 10.dp, 10.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                Button(
                    onClick = {
                        activity?.finish()
                    },
                    modifier = Modifier
                        .size(43.dp)
                        .clip(CircleShape),
                    colors = ButtonDefaults.buttonColors(Color.White),
                    contentPadding = PaddingValues(1.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.size(20.dp),
                        tint = colorResource(id = R.color.black)
                    )
                }
                Spacer(Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = R.drawable.ic_clocknotice),
                    contentDescription = null,
                    modifier = Modifier
                        .size(42.dp),
                    tint = colorResource(id = R.color.white)
                )
                Spacer(modifier = Modifier.width(7.dp))
                Text(
                    text = stringResource(R.string.clock_reminder),
                    fontSize = 28.sp,
                    color = colorResource(id = R.color.white),
                )
                Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun CenterButtons(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { navController.navigate(Screen.Drug.route) },
                modifier = Modifier
                    .width(282.dp)
                    .height(93.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.MainButtonColor))
            ) {
                Text(
                    text = stringResource(R.string.drug_reminder),
                    fontSize = 40.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* Handle click */ },
                modifier = Modifier
                    .width(282.dp)
                    .height(93.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.MainButtonColor))
            ) {
                Text(stringResource(R.string.activity_reminder), fontSize = 40.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* Handle click */ },
                modifier = Modifier
                    .width(282.dp)
                    .height(93.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.MainButtonColor))
            ) {
                Text(stringResource(R.string.drink_water_reminder), fontSize = 40.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* Handle click */ },
                modifier = Modifier
                    .width(282.dp)
                    .height(93.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.MainButtonColor))
            ) {
                Text(stringResource(R.string.rest_reminder), fontSize = 40.sp)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NoticeList() {
    val coroutineScope = rememberCoroutineScope()
    val sheetHeight = 600f  // The height of the bottom sheet
    val offsetY = remember { Animatable(1500f) } // Initialize at -180f
    var status = false
    val context = LocalContext.current
    val activity = context.findActivity()

    BackHandler(true) {
        if (status) {
            coroutineScope.launch { offsetY.animateTo(1500f) }
            status = false
        } else {
            activity?.finish()
        }
    }

    Box(
        Modifier
            .fillMaxSize()
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
                            status = false
                        } else {
                            coroutineScope.launch { offsetY.animateTo(-0f) }
                            status = true
                        }
                    }) { change, dragAmount ->
                        coroutineScope.launch {
                            val original = offsetY.value
                            val newValue = (original + dragAmount.y).coerceIn(-0f, 1500f)
                            offsetY.snapTo(newValue)
                        }
                        change.consume()
                    }
                }
        ) {
            val sessionManager = SessionManager(context)
            val clockList = sessionManager.getClock(context = context)
            val clockListCount = clockList.size

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp, 50.dp, 0.dp, 0.dp),
                content = {
                    items(clockListCount) {
                        ListBox(clockList[it], it)
                    }
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ListBox(clockData: ClockData, index: Int) {
    val title = clockData.title
    val detail = clockData.detail
    val time = clockData.time
    val week = clockData.week
    val switch = clockData.switch

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
                    SwitchComponent(switch,index)
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
                    fontSize = 20.sp,
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
                    }
                    .clip(RoundedCornerShape(25))
                    .background(color = if (weekSelected.value[i]) colorResource(id = R.color.btnInClockColor) else Color.Gray)
            ) {
                Text(
                    color = Color.White,
                    text = week[i],
                    fontSize = 20.sp,
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
fun SwitchComponent(enable: Boolean,index: Int) {
    val context = LocalContext.current
    var isSwitchChecked by remember { mutableStateOf(enable) }
    val sessionManager = SessionManager(context)
    Switch(
        checked = isSwitchChecked,
        onCheckedChange = { isChecked ->
            isSwitchChecked = isChecked
            sessionManager.editClockSwitch(context, index, isChecked)
        },
        colors = SwitchDefaults.colors(checkedThumbColor = Color.Green)
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun ClockPreview() {
    UsrcareTheme {
        Main(navController = rememberNavController())
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun ListBoxPreview() {
    UsrcareTheme {
        ListBox(
            clockData = ClockData(
                "title",
                "detail",
                "time",
                mutableListOf(false, false, false, false, false, false, false),
                true
            ),
            index = 0
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DrugPreview() {
    UsrcareTheme {
        Drug(navController = rememberNavController())
    }
}
