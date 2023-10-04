package com.tku.usrcare.view.ui.scale

import android.app.Activity
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tku.usrcare.R
import com.tku.usrcare.api.ApiUSR
import com.tku.usrcare.model.Question
import com.tku.usrcare.model.ReturnSheet
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.component.Loading
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
                imageVector = Icons.Default.ArrowBack,
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
                .padding(20.dp), contentAlignment = Alignment.Center
        ) {
            Text(
                text = scaleTitle,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                fontSize = with(LocalDensity.current) { 25.dp.toSp() }
            )
        }
    }
}
@RequiresApi(Build.VERSION_CODES.Q)
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
    var showDialog by remember { mutableStateOf(false) }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                navController.popBackStack()
                showDialog = false
            },
            title = {
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
            },
            text = {
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
            },
            confirmButton = {
                TextButton(onClick = {
                    navController.popBackStack()
                    showDialog = false
                }) {
                    Text("確定")
                }
            },
            modifier = Modifier.width(550.dp)
        )
    }
    val leaveAlertDialog = remember { mutableStateOf(false) }
    if (leaveAlertDialog.value) {
        AlertDialog(
            onDismissRequest = { leaveAlertDialog.value = false },
            confirmButton = {
                TextButton(onClick = {
                    leaveAlertDialog.value = false
                    navController.popBackStack()
                }) {
                    Text("確定")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    leaveAlertDialog.value = false
                }) {
                    Text("取消")
                }
            },
            title = {
                Text("提示")
            },
            text = {
                Text("確定要離開嗎？")
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
            val vibrator = context.getSystemService(Activity.VIBRATOR_SERVICE) as Vibrator
            Column(modifier = Modifier.padding(10.dp)) {
                // 顯示問題
                val scrollState = rememberScrollState()
                Box(
                    modifier = Modifier
                        .height(300.dp)
                        .verticalScroll(scrollState)
                ) {
                    Text(text = questions[nowQuestion.intValue].ques, fontSize = 25.sp)
                }
                val clickedIndex = remember { mutableIntStateOf(-1) }
                //選項按鈕橫列
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp, 12.dp, 12.dp, 12.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    itemsIndexed(questions[nowQuestion.intValue].ans) { index, ans ->
                        // 如果此按鈕的索引與當前被點擊的索引匹配，則添加外框
                        val borderStroke = if (clickedIndex.intValue == index) BorderStroke(
                            6.dp, nowMainColor
                        ) else BorderStroke(0.dp, Color.Transparent)

                        Button(
                            modifier = Modifier
                                .padding(10.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = nowMainColor.copy(0.2F)
                            ),
                            border = borderStroke,
                            onClick = {
                                // 更新當前被點擊的按鈕的索引
                                // 如果當前索引已經被點擊，則重置為 -1
                                clickedIndex.intValue = if (clickedIndex.intValue == index) -1 else index
                                answers[nowQuestion.intValue] = index
                            },
                            elevation = ButtonDefaults.elevation(
                                defaultElevation = 0.dp,
                                pressedElevation = 0.dp,
                                disabledElevation = 0.dp
                            )
                        ) {
                            Text(
                                text = ans.toString(),
                                fontSize = with(LocalDensity.current) { 40.dp.toSp() }
                            )
                        }
                    }
                }

                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    // 確認是否為最後一題
                    if (nowQuestion.intValue == questions.size - 1) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(50.dp, 0.dp), horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(
                                onClick = {
                                    val effect =
                                        VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK)
                                    vibrator.vibrate(effect)
                                    clickedIndex.intValue =
                                        answers[nowQuestion.intValue - 1] // 恢復上一個問題的選擇
                                    nowQuestion.intValue -= 1 // 返回上一題
                                },
                                modifier = Modifier.size(74.dp), // 設定按鈕大小
                                shape = CircleShape, // 設定按鈕為圓形
                                colors = ButtonDefaults.buttonColors(backgroundColor = nowMainColor) // 設定底色
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
                                modifier = Modifier
                                    .size(120.dp,74.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = nowMainColor), // 設定底色
                                onClick = {
                                    // 檢查最後一題是否已被回答
                                    if (answers[nowQuestion.intValue] != -1) {
                                        val effect =
                                            VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK)
                                        vibrator.vibrate(effect)
                                        //紀錄結束時間 YYYY-MM-ddTHH:mm:ss
                                        endTime.value = timeFormat.format(System.currentTimeMillis())
                                        Log.d("endTime", endTime.value)
                                        // 所有答案+1
                                        answers.replaceAll { it + 1 }
                                        val returnSheet = ReturnSheet(
                                            answers.map { it.toString() }.toTypedArray(),
                                            startTime.value,
                                            endTime.value
                                        )
                                        ApiUSR.postSheetResult(
                                            context as Activity,
                                            id.toString(),
                                            returnSheet
                                        )
                                        showDialog = true
                                    } else {
                                        // 可以在這裡添加提示，告知用戶最後一題必須回答
                                    }
                                }) {
                                Text(text = "送出", fontSize = 28.sp, color = Color.White, style = TextStyle(fontWeight = FontWeight.Bold))
                            }
                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(50.dp, 0.dp), horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            if (nowQuestion.intValue > 0) {
                                Button(
                                    onClick = {
                                        val effect =
                                            VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK)
                                        vibrator.vibrate(effect)
                                        clickedIndex.intValue =
                                            answers[nowQuestion.intValue - 1] // 恢復上一個問題的選擇
                                        nowQuestion.intValue -= 1 // 返回上一題
                                    },
                                    modifier = Modifier.size(74.dp), // 設定按鈕大小
                                    shape = CircleShape, // 設定按鈕為圓形
                                    colors = ButtonDefaults.buttonColors(backgroundColor = nowMainColor), // 設定底色
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
                                        val effect =
                                            VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK)
                                        vibrator.vibrate(effect)
                                        clickedIndex.intValue = -1 // 清除外框
                                        nowQuestion.intValue += 1 // 移動到下一題
                                        // 如果下一題已經被回答，則恢復選擇
                                        if (answers[nowQuestion.intValue] != -1) {
                                            clickedIndex.intValue = answers[nowQuestion.intValue]
                                        }
                                    } else {
                                        //TODO: 跳出提示必答
                                    }
                                },
                                modifier = Modifier.size(74.dp), // 設定按鈕大小
                                shape = CircleShape, // 設定按鈕為圓形
                                colors = ButtonDefaults.buttonColors(backgroundColor = nowMainColor) // 設定底色

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

    Column(modifier = Modifier.padding(20.dp)) {
        SheetTitle(nowMainColor, scaleTitle.value, navController, leaveAlertDialog)
        Spacer(modifier = Modifier.height(15.dp))
        Content()
    }
}

