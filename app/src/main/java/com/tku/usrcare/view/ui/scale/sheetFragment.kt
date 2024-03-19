package com.tku.usrcare.view.ui.scale

import android.app.Activity
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.tku.usrcare.R
import com.tku.usrcare.api.ApiUSR
import com.tku.usrcare.model.Question
import com.tku.usrcare.model.ReturnSheet
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.component.AutoSizedText
import com.tku.usrcare.view.component.Loading
import com.tku.usrcare.view.component.findActivity
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun SheetTitle(
    nowMainColor: Color,
    scaleTitle: String,
    navController: NavController,
    leaveAlertDialog: MutableState<Boolean>
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = {
                leaveAlertDialog.value = true
            },
            modifier = Modifier
                .size(63.dp)
                .clip(CircleShape),
            colors = ButtonDefaults.buttonColors(Color.White),
            contentPadding = PaddingValues(1.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.size(35.dp),
                tint = colorResource(id = R.color.black)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(nowMainColor.copy(alpha = 0.2F))
                .border(2.dp, nowMainColor, RoundedCornerShape(10.dp)) // 設定外框的寬度、顏色和圓角
                .padding(15.dp), contentAlignment = Alignment.Center
        ) {
            AutoSizedText(text = scaleTitle, size = 30, fontWeight = FontWeight.Bold)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Scale(id: Int, navController: NavController) {
    val isLoadingVisible = remember { mutableStateOf(true) }
    val isOk = remember { mutableStateOf(false) }
    val scaleTitle = remember { mutableStateOf("") }
    val specialOption = remember { mutableStateListOf<String>() }
    val questions = remember { mutableStateListOf<Question>() }
    val context = LocalContext.current
    val nowQuestion = remember { mutableIntStateOf(0) }
    val startTime = remember { mutableStateOf("") }
    val endTime = remember { mutableStateOf("") }
    val timeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.TAIWAN)
    val nowMainColor =
        Color(android.graphics.Color.parseColor(SessionManager(context).getNowMainColor()))
    val nowFontSize = remember { mutableStateOf(25.sp) }
    val nowLineHeight = remember { mutableStateOf(40.sp) }
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
    val noRippleInteractionSource = remember { MutableInteractionSource() }
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
                }
            }
        }
    }
    val showSnakeBar = remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    if (showDialog) {
        AlertDialog(onDismissRequest = {
            navController.popBackStack()
            showDialog = false
        }, title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_checkmark),
                    contentDescription = "done",
                )
                Text(
                    "已完成",
                    style = androidx.compose.ui.text.TextStyle(fontSize = 30.sp),
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    modifier = Modifier.padding(0.dp, 6.dp, 8.dp, 0.dp)
                )
            }
        }, text = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_congratulations_a),
                    contentDescription = ""
                )
                Spacer(modifier = Modifier.width(18.dp))
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_coin),
                        contentDescription = ""
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "+10",
                        fontSize = 30.sp,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(18.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_congratulations_b),
                    contentDescription = ""
                )
            }
        }, confirmButton = {
            TextButton(onClick = {
                navController.popBackStack()
                showDialog = false
            }) {
                Text("確定")
            }
        }, modifier = Modifier.width(550.dp)
        )
    }
    val leaveAlertDialog = remember { mutableStateOf(false) }
    if (leaveAlertDialog.value) {
        AlertDialog(
            onDismissRequest = { leaveAlertDialog.value = false },
            confirmButton = {
                androidx.compose.material3.Button(
                    onClick = {
                        leaveAlertDialog.value = false
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .width(100.dp)
                        .height(50.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = nowMainColor
                    )
                ) {
                    AutoSizedText(
                        text = "確定", size = 24, fontWeight = FontWeight.Bold, color = Color.White
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        leaveAlertDialog.value = false
                    }, modifier = Modifier
                        .width(100.dp)
                        .height(50.dp)
                ) {
                    AutoSizedText(
                        text = "取消", size = 24, fontWeight = FontWeight.Bold, color = Color.Black
                    )
                }
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_logout),
                        contentDescription = "warning",
                        tint = nowMainColor,
                        modifier = Modifier
                            .size(60.dp)
                            .padding(end = 16.dp)
                    )
                    Text(
                        "確定要離開嗎?", fontSize = 30.sp, fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Text(
                    "離開後將不會儲存此次填答結果",
                    fontSize = 21.sp,
                )
            },
        )
    }

    BackHandler {
        leaveAlertDialog.value = true
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Loading(isLoadingVisible.value)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @Composable
    fun Content() {
        LaunchedEffect(Unit) {
            ApiUSR.getScale(context as Activity, id, onSuccess = { response ->
                scaleTitle.value = response.sheetTitle
                response.specialOption.forEach { (key, value) ->
                    specialOption.add("$key: $value")
                }
                questions.addAll(response.questions) // 將問題列表添加到可記憶的狀態中
                isOk.value = true
                //紀錄開始時間 YYYY-MM-ddTHH:mm:ss
                startTime.value = timeFormat.format(System.currentTimeMillis())
                Log.d("startTime", startTime.value)

            }, onError = {
                println(it)
                isOk.value = true
            })
        }
        if (isOk.value) {
            isLoadingVisible.value = false
            val answers =
                remember { mutableStateListOf(*IntArray(questions.size) { -1 }.toTypedArray()) }
            val vibrator = ContextCompat.getSystemService(context, Vibrator::class.java)

            Column(modifier = Modifier.padding(10.dp)) {
                // 顯示問題
                val scrollState = rememberScrollState()
                Box(
                    modifier = Modifier
                        .weight(0.7f)
                        .verticalScroll(scrollState)
                ) {
                    Text(
                        text = questions[nowQuestion.intValue].ques,
                        fontSize = nowFontSize.value,
                        lineHeight = nowLineHeight.value
                    )
                }

                val clickedIndex = remember { mutableIntStateOf(-1) }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.15f)
                        .padding(5.dp, 5.dp, 5.dp, 5.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    questions[nowQuestion.intValue].ans.forEachIndexed { index, ans ->
                        val borderStroke = if (clickedIndex.intValue == index) BorderStroke(
                            6.dp, nowMainColor
                        ) else BorderStroke(0.dp, Color.Transparent)

                        val buttonWeight =
                            1f / questions[nowQuestion.intValue].ans.size + questions[nowQuestion.intValue].ans.size * 0.1f
                        val spacingWeight = 0.1f



                        Button(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(buttonWeight),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = nowMainColor.copy(0.2F)
                            ),
                            border = borderStroke,
                            onClick = {
                                clickedIndex.intValue =
                                    if (clickedIndex.intValue == index) -1 else index
                                answers[nowQuestion.intValue] = index
                            },
                            contentPadding = PaddingValues(3.dp),
                        ) {
                            AutoSizedText(
                                text = ans.toString(),
                                size = 40,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                        if (index != questions[nowQuestion.intValue].ans.size - 1) {
                            Spacer(modifier = Modifier.weight(spacingWeight))
                        }
                    }
                }


                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(0.15f),
                    contentAlignment = Alignment.Center
                ) {
                    // 確認是否為最後一題
                    if (nowQuestion.intValue == questions.size - 1) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(50.dp, 0.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(
                                onClick = {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                        val effect =
                                            VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK)
                                        vibrator?.vibrate(effect)
                                    } else {
                                        if (vibrator != null) {
                                            @Suppress("DEPRECATION") vibrator.vibrate(100)
                                        }
                                    }
                                    clickedIndex.intValue =
                                        answers[nowQuestion.intValue - 1] // 恢復上一個問題的選擇
                                    nowQuestion.intValue -= 1 // 返回上一題
                                }, modifier = Modifier.size(74.dp), // 設定按鈕大小
                                shape = CircleShape, // 設定按鈕為圓形
                                colors = ButtonDefaults.buttonColors(containerColor = nowMainColor) // 設定底色
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_back_arr), // 使用drawable中的圖標
                                    contentDescription = "上一題", // 為無障礙功能提供描述
                                    modifier = Modifier.size(44.dp) // 設定圖標大小
                                )
                            }
                            Button(
                                //圓角
                                shape = RoundedCornerShape(50.dp),
                                modifier = Modifier.size(120.dp, 74.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = nowMainColor), // 設定底色
                                onClick = {
                                    // 檢查最後一題是否已被回答
                                    if (answers[nowQuestion.intValue] != -1) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                            val effect =
                                                VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK)
                                            vibrator?.vibrate(effect)
                                        } else {
                                            if (vibrator != null) {
                                                @Suppress("DEPRECATION") vibrator.vibrate(100)
                                            }
                                        }
                                        //紀錄結束時間 YYYY-MM-ddTHH:mm:ss
                                        endTime.value =
                                            timeFormat.format(System.currentTimeMillis())
                                        Log.d("endTime", endTime.value)
                                        // 所有答案+1
                                        answers.replaceAll { it + 1 }
                                        val returnSheet = ReturnSheet(
                                            answers.map { it.toString() }.toTypedArray(),
                                            startTime.value,
                                            endTime.value
                                        )
                                        ApiUSR.postSheetResult(
                                            context as Activity, id.toString(), returnSheet , onSuccess = {
                                                navController.navigate("Final/${if (it) "B" else "A"}")
                                            }
                                        )
//                                        showDialog = true
                                    } else {
                                        // 可以在這裡添加提示，告知用戶最後一題必須回答
                                        showSnakeBar.value = true
                                    }
                                }) {
                                AutoSizedText(
                                    text = "送出",
                                    size = 24,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(50.dp, 0.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            if (nowQuestion.intValue > 0) {
                                Button(
                                    onClick = {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                            val effect =
                                                VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK)
                                            vibrator?.vibrate(effect)
                                        } else {
                                            if (vibrator != null) {
                                                @Suppress("DEPRECATION") vibrator.vibrate(100)
                                            }
                                        }
                                        clickedIndex.intValue =
                                            answers[nowQuestion.intValue - 1] // 恢復上一個問題的選擇
                                        nowQuestion.intValue -= 1 // 返回上一題
                                    },
                                    modifier = Modifier.size(74.dp), // 設定按鈕大小
                                    shape = CircleShape, // 設定按鈕為圓形
                                    colors = ButtonDefaults.buttonColors(containerColor = nowMainColor), // 設定底色
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_back_arr), // 使用drawable中的圖標
                                        contentDescription = "上一題", // 為無障礙功能提供描述
                                        modifier = Modifier.size(44.dp) // 設定圖標大小
                                    )
                                }
                            } // 如果不是第一題，則顯示上一題按鈕
                            Spacer(modifier = androidx.compose.ui.Modifier.weight(1f))
                            Button(
                                onClick = {
                                    if (answers[nowQuestion.intValue] != -1) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                            val effect =
                                                VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK)
                                            vibrator?.vibrate(effect)
                                        } else {
                                            if (vibrator != null) {
                                                @Suppress("DEPRECATION") vibrator.vibrate(100)
                                            }
                                        }
                                        clickedIndex.intValue = -1 // 清除外框
                                        nowQuestion.intValue += 1 // 移動到下一題
                                        // 如果下一題已經被回答，則恢復選擇
                                        if (answers[nowQuestion.intValue] != -1) {
                                            clickedIndex.intValue = answers[nowQuestion.intValue]
                                        }
                                    } else {
                                        showSnakeBar.value = true
                                    }
                                }, modifier = Modifier.size(74.dp), // 設定按鈕大小
                                shape = CircleShape, // 設定按鈕為圓形
                                colors = ButtonDefaults.buttonColors(containerColor = nowMainColor) // 設定底色

                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_next_arr), // 使用drawable中的圖標
                                    contentDescription = "下一題", // 為無障礙功能提供描述
                                    modifier = Modifier.size(44.dp) // 設定圖標大小
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun NoticeSnakeBar() {
        val progress = remember { Animatable(0f) }

        LaunchedEffect(showSnakeBar.value) {
            if (showSnakeBar.value) {
                progress.animateTo(
                    targetValue = 1f, animationSpec = tween(durationMillis = 5000)
                )
                showSnakeBar.value = false
            }
        }

        Snackbar(
            shape = RoundedCornerShape(8.dp),
            containerColor = colorResource(id = R.color.strokeGray),
            modifier = Modifier
                .wrapContentHeight()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.u_have_not_answered),
                        color = Color.Black
                    )
                    TextButton(
                        onClick = { showSnakeBar.value = false },
                        colors = ButtonDefaults.textButtonColors(contentColor = nowMainColor)
                    ) {
                        Text(stringResource(id = R.string.confirm), color = nowMainColor)
                    }
                }
                LinearProgressIndicator(
                    progress = { progress.value },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp),
                    trackColor = nowMainColor.copy(alpha = 0.2F),
                    color = nowMainColor
                )
            }
        }
    }

    @Composable
    fun FunctionBar() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_text_size),
                contentDescription = "size",
                modifier = Modifier
                    .size(40.dp)
                    .padding(0.dp)
                    .clip(CircleShape)
                    .clickable {
                        scope.launch {
                            if (!sheetState.bottomSheetState.isVisible) {
                                sheetState.bottomSheetState.expand()
                            } else {
                                sheetState.bottomSheetState.hide()
                            }
                        }
                    }
            )
            Spacer(modifier = Modifier.width(10.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_speak),
                contentDescription = "speak",
                modifier = Modifier
                    .size(40.dp)
                    .padding(0.dp)
                    .clip(CircleShape)
                    .clickable {
                        // todo
                    }
            )
        }
    }

    @Composable
    fun AdjustSliders() {
        val vibrator = ContextCompat.getSystemService(context, Vibrator::class.java)
        fun vibrate() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val effect =
                    VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK)
                vibrator?.vibrate(effect)
            } else {
                if (vibrator != null) {
                    @Suppress("DEPRECATION") vibrator.vibrate(100)
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(nowMainColor.copy(alpha = 0.2F))
                .clip(RoundedCornerShape(10.dp))
                .padding(start = 25.dp, end = 25.dp, bottom = 15.dp, top = 15.dp)
        ) {
            Column {
                var sliderPosition by remember { mutableFloatStateOf(1f) } // 默认位置为1，对应25.sp
                val fontSizes = listOf(20.sp, 25.sp, 30.sp, 35.sp, 40.sp)
                Text(
                    text = "調整字體大小：",
                    fontSize = 20.sp,
                )
                Slider(
                    value = sliderPosition,
                    onValueChange = {
                        sliderPosition = it
                        nowFontSize.value = fontSizes[sliderPosition.toInt()]
                        vibrate()
                    },
                    valueRange = 0f..4f,
                    steps = 3,
                    colors = SliderDefaults.colors(
                        thumbColor = nowMainColor,
                        activeTrackColor = nowMainColor,
                        inactiveTrackColor = nowMainColor.copy(alpha = 0.2F)
                    ),
                )
            }
            Column {
                var sliderPosition by remember { mutableFloatStateOf(1f) } // 默认位置为1，对应25.sp
                val fontSizes = listOf(40.sp, 45.sp, 50.sp, 55.sp, 60.sp)
                Text(
                    text = "調整間距大小：",
                    fontSize = 20.sp,
                )

                Slider(
                    value = sliderPosition,
                    onValueChange = {
                        sliderPosition = it
                        nowLineHeight.value = fontSizes[sliderPosition.toInt()]
                        vibrate()
                    },
                    valueRange = 0f..4f,
                    steps = 3,
                    colors = SliderDefaults.colors(
                        thumbColor = nowMainColor,
                        activeTrackColor = nowMainColor,
                        inactiveTrackColor = nowMainColor.copy(alpha = 0.2F)
                    ),
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        scope.launch {
                            sheetState.bottomSheetState.hide()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = nowMainColor),
                ) {
                    Text(text = stringResource(id = R.string.finish), fontSize = 20.sp)
                }
            }
        }
    }

    BottomSheetScaffold(
        sheetContent = {
            AdjustSliders()
        },
        scaffoldState = sheetState,
        sheetContainerColor = Color.White,
        containerColor = Color.Transparent,
        sheetDragHandle = {},
        sheetSwipeEnabled = false,
        modifier = Modifier.clickable(
            interactionSource = noRippleInteractionSource,
            indication = null
        ) { scope.launch { sheetState.bottomSheetState.hide() } }
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxSize()
            ) {
                Box(
                    modifier = Modifier.weight(0.15f)

                ) {
                    SheetTitle(nowMainColor, scaleTitle.value, navController, leaveAlertDialog)
                }
                Box(
                    modifier = Modifier
                        .weight(0.05f)
                        .fillMaxSize()
                ) {
                    FunctionBar()
                }
                Box(
                    modifier = Modifier
                        .weight(0.80f)
                        .fillMaxSize()
                ) {
                    Content()
                }
            }
            AnimatedContent(targetState = showSnakeBar.value, label = "") { visible ->
                if (visible) {
                    NoticeSnakeBar()
                }
            }
        }
    }
}

