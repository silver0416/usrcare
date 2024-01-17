package com.tku.usrcare.view.ui.clock


import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tku.usrcare.R
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.component.AutoSizedText
import com.tku.usrcare.view.component.TitleBox
import com.tku.usrcare.view.component.findActivity
import com.tku.usrcare.viewmodel.ClockViewModel
import com.tku.usrcare.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private lateinit var clockViewModel: ClockViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClockContent(navController: NavController) {

    val context = LocalContext.current
    val sessionManager = SessionManager(context)
    val viewModelFactory = ViewModelFactory(sessionManager)
    clockViewModel = viewModel(
        viewModelStoreOwner = context as ViewModelStoreOwner,
        factory = viewModelFactory
    )


    val sheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            skipHiddenState = true
        )
    )
    val scope = rememberCoroutineScope()
    val isBottomSheetOpen = remember {
        mutableStateOf(false)
    }
    val rotationAngle by animateFloatAsState(
        targetValue = if (isBottomSheetOpen.value) 180f else 0f,
        label = ""
    )


    val reminderList by clockViewModel.reminderList.observeAsState(sessionManager.getReminderList())

    if (!isBottomSheetOpen.value) {
        LaunchedEffect(Unit) {
            clockViewModel.callCloseAllItem.value = true
        }
    }


    // 監聽 BottomSheet 的狀態
    LaunchedEffect(sheetState.bottomSheetState) {
        // 使用 snapshotFlow 監聽 bottomSheetState 的 currentValue
        snapshotFlow { sheetState.bottomSheetState.currentValue }.collect { currentValue ->
            when (currentValue) {
                SheetValue.Expanded -> {
                    // BottomSheet 展開時的邏輯
                    isBottomSheetOpen.value = true
                }

                else -> {
                    // BottomSheet 隱藏時的邏輯
                    isBottomSheetOpen.value = false
                }
            }
        }
    }

    BackHandler {
        if (isBottomSheetOpen.value) {
            scope.launch {
                sheetState.bottomSheetState.partialExpand()
            }
        } else {
            context.findActivity()?.finish()
        }
    }
    BottomSheetScaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = sheetState,
        sheetContent = {
            LazyColumn(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp),
            ) {
                if (reminderList.isNullOrEmpty()) {
                    item {
                        Text(
                            text = "現在，沒有設定任何鬧鐘(。_。)",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp),
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                } else {
                    items(reminderList!!, key = { reminder ->
                        reminder.requestId
                    }) { reminder ->
                        ReminderItem(alarmItem = reminder, clockViewModel = clockViewModel)
                    }
                }
            }
        },
        sheetPeekHeight = 130.dp,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.bgClock))
            ) {
                TitleBox(
                    color = colorResource(id = R.color.btnClockColor), title = stringResource(
                        id = R.string.clock_reminder
                    ), icon = painterResource(id = R.drawable.ic_clocknotice)
                )
            }
        },
        sheetDragHandle = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, top = 10.dp),
                horizontalArrangement = Arrangement.Start
            ) { BottomSheetButton(isBottomSheetOpen, sheetState, scope, rotationAngle) }
        },
        sheetContainerColor = colorResource(id = R.color.bgClockCard),
        containerColor = colorResource(id = R.color.bgClock),
    ) {
        ClockList(it, navController)
    }
}


@Composable
fun ClockList(paddingValues: PaddingValues, navController: NavController) {
    Box(modifier = Modifier.padding(paddingValues)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        ) {
            item {
                TypeButton(
                    name = stringResource(id = R.string.drug_reminder),
                    route = "drugNotification",
                    navController = navController
                )
                Spacer(modifier = Modifier.size(10.dp))
                TypeButton(
                    name = stringResource(id = R.string.activity_reminder),
                    route = "activityNotification",
                    navController = navController
                )
                Spacer(modifier = Modifier.size(10.dp))
                TypeButton(
                    name = stringResource(id = R.string.drink_water_reminder),
                    route = "drinkNotification",
                    navController = navController
                )
                Spacer(modifier = Modifier.size(10.dp))
                TypeButton(
                    name = stringResource(id = R.string.rest_reminder),
                    route = "sleepNotification",
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun TypeButton(name: String, route: String, navController: NavController) {
    Button(
        onClick = {
            navController.navigate(route)
        },
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp)
            .clip(MaterialTheme.shapes.medium)
            .border(
                width = 3.dp,
                color = colorResource(id = R.color.btnClockColor),
                shape = MaterialTheme.shapes.medium
            ), colors = androidx.compose.material3.ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = colorResource(id = R.color.black),
        ),
        shape = MaterialTheme.shapes.medium,
        contentPadding = PaddingValues(30.dp)
    ) {
        AutoSizedText(
            text = name,
            size = 40,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetButton(
    isBottomSheetOpen: MutableState<Boolean>,
    sheetState: BottomSheetScaffoldState,
    scope: CoroutineScope,
    rotationAngle: Float
) {
    Button(
        onClick = {
            scope.launch {
                if (!isBottomSheetOpen.value) {
                    sheetState.bottomSheetState.expand()
                    isBottomSheetOpen.value = true
                } else {
                    sheetState.bottomSheetState.partialExpand()
                    isBottomSheetOpen.value = false
                }
            }
        },
        contentPadding = PaddingValues(0.dp),
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = colorResource(id = R.color.black)
        ),
    ) {
        Icon(
            Icons.Default.KeyboardArrowUp,
            contentDescription = "BottomSheetButton",
            modifier = Modifier
                .size(40.dp)
                .rotate(rotationAngle)
        )
    }
}
