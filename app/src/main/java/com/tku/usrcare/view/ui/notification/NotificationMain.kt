package com.tku.usrcare.view.ui.notification

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tku.usrcare.R
import com.tku.usrcare.model.BroadcastData
import com.tku.usrcare.view.component.FixedSizeText
import com.tku.usrcare.view.component.findActivity
import com.tku.usrcare.viewmodel.NotificationViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationMain(
    notificationViewModel: NotificationViewModel,
    navHostController: NavHostController
) {
    val context = LocalContext.current
    val sheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            skipHiddenState = false,
            initialValue = SheetValue.Expanded
        )
    )
    val scope = rememberCoroutineScope()
    val isBottomSheetOpen = remember {
        mutableStateOf(false)
    }
    BackHandler {
        if (isBottomSheetOpen.value) {
            scope.launch {
                sheetState.bottomSheetState.hide()
            }
        } else {
            context.findActivity()?.finish()
        }
    }
    LaunchedEffect(Unit) {
        scope.launch {
            sheetState.bottomSheetState.hide()
        }
    }

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
                    sheetState.bottomSheetState.hide()
                    notificationViewModel.clearSelectedMessage()
                }
            }
        }
    }

    val selectedMessage = remember {
        mutableStateOf(
            BroadcastData(
                "",
                "",
                "",
                "",
                "",
                "",
            )
        )
    }
    notificationViewModel.selectedMessage.observeForever {
        if (it != null) {
            selectedMessage.value = it
        }
    }


    BottomSheetScaffold(
        sheetContent = {
            NotificationDetail(
                broadcastData = selectedMessage.value,
                notificationViewModel = notificationViewModel,
                sheetState = sheetState,
                coroutineScope = scope
            )
        },
        scaffoldState = sheetState,
        containerColor = colorResource(id = R.color.bgMain),
        sheetContainerColor = colorResource(id = R.color.SecondaryButtonColor),
    ) {
        Column(
            Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                NotificationList(
                    notificationViewModel,
                    sheetState,
                )
                TopBar()
            }
        }
    }
}


@Composable
fun TopBar() {
    val context = LocalContext.current
    val activity = context.findActivity()
    val gradient = Brush.verticalGradient(
        0.0f to colorResource(id = R.color.bgMain),
        0.5f to colorResource(id = R.color.bgMain).copy(alpha = 0.7f),
        0.75f to colorResource(id = R.color.bgMain).copy(alpha = 0.3f),
        1.0f to Color.Transparent
    )
    Box(
        modifier = Modifier
            .background(brush = gradient)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
        ) {
            Button(
                onClick = {
                    activity?.finish()
                },
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                colors = ButtonDefaults.buttonColors(Color.White),
                contentPadding = PaddingValues(1.dp)

            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(25.dp),
                    tint = colorResource(id = R.color.black)
                )
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 50.dp),
            ) {
                FixedSizeText(
                    text = "通知", size = 90.dp, color = Color.Black, fontWeight = FontWeight.Bold
                )
            }
        }
    }
}