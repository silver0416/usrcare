package com.tku.usrcare.view.ui.petcompany

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.tku.usrcare.R
import com.tku.usrcare.view.component.TitleBox
import com.tku.usrcare.viewmodel.PetCompanyViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import coil.compose.rememberAsyncImagePainter
import com.tku.usrcare.api.ApiUSR
import com.tku.usrcare.model.getItemsResponse
import com.tku.usrcare.model.getUserInventoryResponse
import com.tku.usrcare.model.getUserSettingResponse
import com.tku.usrcare.model.item
import com.tku.usrcare.model.postUserSetting
import com.tku.usrcare.model.postUserUseItem
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.component.FixedSizeText
import com.tku.usrcare.view.component.Loading
import com.tku.usrcare.view.component.normalAlertDialog
import com.tku.usrcare.view.component.twoButtonDialog
import com.tku.usrcare.view.startStepCounterService


@Composable
fun TopBar() {
    Column(
        Modifier
            .background(colorResource(id = R.color.bgPetCompany))
            .fillMaxWidth()
    ) {
        TitleBox(
            color = colorResource(id = R.color.btnPetcompanyColor),
            title = stringResource(id = R.string.pet_company),
            icon = painterResource(id = R.drawable.ic_petcompany)
        )
    }
}

var petName = "您的寵物"

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun PetImage(
    navController: NavHostController,
    showDialog: MutableState<ShowDialog>,
    pet_companion_pedomoter_goal: getUserSettingResponse
) {
    val screenHeightDp = LocalConfiguration.current.screenHeightDp
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val heightFraction = 0.6//圖片高度佔螢幕高度的比例
    val boxHeight = (screenHeightDp * heightFraction).dp
    var petDetailVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current.applicationContext
    val sharedPreferences = context.getSharedPreferences("step_counter_prefs", Context.MODE_PRIVATE)
    //如果因為手機重啟導致tpdaySteps為負數，則將其暫時設為0
    val stepCount = remember {
        mutableStateOf(
            if (sharedPreferences.getInt("totalSteps", 0) - sharedPreferences.getInt(
                    "recordSteps",
                    0
                ) < 0
            ) {
                0
            } else {
                sharedPreferences.getInt("totalSteps", 0) - sharedPreferences.getInt(
                    "recordSteps",
                    0
                )
            }
        )
    }
    val sharedPreferencesListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == "totalSteps") {
            if (sharedPreferences.getInt("totalSteps", 0) - sharedPreferences.getInt(
                    "recordSteps",
                    0
                ) < 0
            ) {
                stepCount.value = 0
            } else {
                stepCount.value = sharedPreferences.getInt(
                    "totalSteps",
                    0
                ) - sharedPreferences.getInt("recordSteps", 0)
            }
            Log.d("Debug", "PetCompany_MainPage1:todaySteps: ${stepCount.value}")
        }
    }
    DisposableEffect(Unit) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferencesListener)
        Log.d("Debug", "PetCompany_MainPage3:todaySteps: ${stepCount.value}")
        onDispose {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(sharedPreferencesListener)
            Log.d("Debug", "PetCompany_MainPage2:todaySteps: ${stepCount.value}")
        }
    }
    //var showDialog by remember { mutableStateOf(false) }

    data class petInformation(
        val informationName: String,
        val number: Int,
    )

    val petInformations = listOf(
        petInformation("行走里程", stepCount.value),
        petInformation(
            "目標步數",
            if (pet_companion_pedomoter_goal.pet_companion_pedomoter_goal != null) {
                pet_companion_pedomoter_goal.pet_companion_pedomoter_goal
            } else if (pet_companion_pedomoter_goal.pet_companion_pedomoter_goal_suggestion != null) {
                pet_companion_pedomoter_goal.pet_companion_pedomoter_goal_suggestion
            } else {
                0
            }
        ),
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(boxHeight),
        //.padding(18.dp),
        //.background(color = Color.Blue),
        contentAlignment = Alignment.Center
    ) {
        Column {
            if (petDetailVisible) {
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize()
                        .clip(RoundedCornerShape(32.dp))
                        .background(color = colorResource(id = R.color.bgPetCompanyCard))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Box(contentAlignment = Alignment.TopEnd) {
                            Image(
                                painter = painterResource(id = R.drawable.pet),
                                contentDescription = "寵物小圖",
                                modifier = Modifier
                                    .height((screenHeightDp * 0.2).dp)
                                    .fillMaxWidth()
                                    .zIndex(1f)
                            )
                            Icon(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clickable { petDetailVisible = !petDetailVisible }
                                    .zIndex(2f),
                                painter = painterResource(id = R.drawable.close),
                                contentDescription = "返回",
                                tint = Color.Unspecified
                            )
                        }
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        )
                        {
                            FixedSizeText(
                                text = petName,
                                size = 80.dp,
                                fontWeight = FontWeight.Bold
                            )
                            TextButton(
                                modifier = Modifier
                                    .size(35.dp)
                                    .clip(RoundedCornerShape(24.dp)),
                                onClick = { showDialog.value = ShowDialog(true, "resetPetName") },
                                contentPadding = PaddingValues(1.dp),
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .size(35.dp)
                                        .background(color = colorResource(id = R.color.bgPetCompanyCard)),
                                    painter = painterResource(id = R.drawable.pencil),
                                    contentDescription = "改名按鈕",
                                    tint = Color.Black,
                                )
                            }
                        }
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.Start
                            //horizontalAlignment = Alignment.CenterHorizontally
                        )
                        {
                            items(petInformations) { item ->
                                Row {
                                    FixedSizeText(
                                        text = item.informationName,
                                        color = Color.Black,
                                        size = 60.dp,
                                        fontWeight = FontWeight.Bold,
                                    )
                                    if (item.informationName == "目標步數") {
                                        TextButton(
                                            modifier = Modifier
                                                .size(25.dp)
                                                .clip(RoundedCornerShape(24.dp)),
                                            onClick = {
                                                showDialog.value =
                                                    ShowDialog(true, "resetGoalSteps")
                                            },
                                            contentPadding = PaddingValues(1.dp),
                                        ) {
                                            Icon(
                                                modifier = Modifier
                                                    .size(25.dp)
                                                    .background(color = colorResource(id = R.color.bgPetCompanyCard)),
                                                painter = painterResource(id = R.drawable.pencil),
                                                contentDescription = "改名按鈕",
                                                tint = Color.Black,
                                            )
                                        }
                                    }

                                    FixedSizeText(
                                        text = "：" + item.number.toString() + "步",
                                        color = Color.Black,
                                        size = 60.dp,
                                        fontWeight = FontWeight.Bold,
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                //Store(petCompanyViewModel = petCompanyViewModel, navController = navController)
                Image(
                    painter = painterResource(id = R.drawable.pet),
                    contentDescription = "寵物狗狗圖",
                    modifier = Modifier
                        .height((screenHeightDp * 0.45).dp)
                        .fillMaxWidth()
                        .clickable { petDetailVisible = !petDetailVisible })
            }
        }
    }
}


@Composable
fun UseItem(userInevtory: getUserInventoryResponse, showDialog: MutableState<ShowDialog>) {
    val screenHeightDp = LocalConfiguration.current.screenHeightDp
    val heightFraction = 0.2//圖片高度佔螢幕高度的比例
    val boxHeight = (screenHeightDp * heightFraction).dp
    //var showUseInformation by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var items by remember { mutableStateOf(getItemsResponse(listOf())) }
    ApiUSR.getItems(
        SessionManager(context).getUserToken().toString(),
        onSuccess = {
            if (it != null) {
                items = it
                Log.d("Store", it.toString())
            } else {
                //prices.value = getItemsPriceResponse(listOf(10,20,5))
                Log.d("Store", "0")
            }
        },
        onError = {
            //prices.value = getItemsPriceResponse(listOf(10,20,5))
            Log.d("Store", "1")
        },
        onInternetError = {
            //prices.value = getItemsPriceResponse(listOf(10,20,5))
            Log.d("Store", "2")
        }
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(boxHeight),
        //.background(color= Color.Black),
        contentAlignment = Alignment.Center,
    )
    {
        LazyRow(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center) {
            items(items.items) { item ->
                Box(contentAlignment = Alignment.BottomEnd) {
                    Button(
                        onClick = {
                            showDialog.value = ShowDialog(true, "usingItem", itemID = item.itemID)
                        },
                        modifier = Modifier
                            .padding(12.dp)
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(
                                5.dp,
                                colorResource(id = R.color.btnPetcompanyColor),
                                CircleShape
                            ),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = colorResource(id = R.color.bgPetCompany),
                        )
                    ) {
                        Box(modifier = Modifier.zIndex(1f)) {
                            Image(
                                modifier = Modifier
                                    .zIndex(1f)
                                    .fillMaxSize()
                                    .padding(3.dp),
                                painter = rememberAsyncImagePainter(item.image_path),
                                contentDescription = "道具圖片",
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(color = Color.Blue)
                            .size(40.dp)
                            .zIndex(2f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userInevtory.items?.get(item.itemID.toString())?.quantity?.toString()
                                ?: "0",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp,
                        )
                    }
                }
            }
        }
    }
}

data class ShowDialog(
    val isShowDialog: Boolean,
    val case: String,
    val firstTime: Boolean? = true,
    val itemID: Int? = 0,
)

@Composable
fun MainPage(navController: NavHostController) {
    val context = LocalContext.current
    val prefs: SharedPreferences =
        context.getSharedPreferences("goalStepsSetting", Context.MODE_PRIVATE)

    var newGoalSteps by remember { mutableStateOf("1000") }
    var newName by remember { mutableStateOf("") }

    val showDialog = remember {
        mutableStateOf(ShowDialog(false, ""))
    }
    val i = remember {
        mutableStateOf(0)
    }
    var goalSteps = remember {
        mutableStateOf("0")
    }
    //啟動計步器服務
    LaunchedEffect(Unit) {
        startStepCounterService(context)
    }
    var pet_companion_pedomoter_goal by remember {
        mutableStateOf(
            getUserSettingResponse(null, null)
        )
    }
    LaunchedEffect(Unit) {
        ApiUSR.getUserSetting(
            SessionManager(context).getUserToken()
                .toString(),
            config_key = "pet_companion_pedomoter_goal",
            onSuccess = {
                try {
                    pet_companion_pedomoter_goal = it
                    //成功取得目標步數資訊後，決定是否跳出首次設定步數的訊息
                    if (pet_companion_pedomoter_goal.pet_companion_pedomoter_goal != null) {
                        //不做任何事
                    } else {
                        showDialog.value = ShowDialog(true, "settingGoal")
                    }
                    Log.d("Debug", "petCompany已取得目標步數: $pet_companion_pedomoter_goal")
                } catch (e: Exception) {
                    Log.d("Debug", "petCompany錯誤: $e")
                }
            },
            onError = {
                Log.d("Debug", "petCompany錯誤: onError")
            },
            onInternetError = {
                Log.d("Debug", "petCompany錯誤: onInternetError")
            }
        )
    }

    var userInevntory by remember {
        mutableStateOf(
            getUserInventoryResponse()
        )
    }
    ApiUSR.getUserInventory(
        SessionManager(context).getUserToken()
            .toString(),
        onSuccess = {
            try {
                userInevntory = it
                Log.d("Debug", "userInevntory已取得: $userInevntory")
            } catch (e: Exception) {
                Log.d("Debug", "userInevntory錯誤: $e")
            }
        },
        onError = {
            Log.d("Debug", "userInevntory錯誤: onError")
        },
        onInternetError = {
            Log.d("Debug", "userInevntory錯誤: onInternetError")
        }
    )


    data class GoalSetting(
        val stage: List<Int> = listOf(),
        val title: List<String> = listOf(),
        val text: List<String> = listOf(),
        val nextButtonText: List<String> = listOf(),
        val alertMessage: List<String> = listOf(),
    )

    val goalSetting = remember(pet_companion_pedomoter_goal) {
        GoalSetting(
            listOf(1, 2),
            listOf("首次使用寵物陪伴", "設定中"),
            listOf(
                "第一次使用寵物陪伴功能請先設定每日行走的目標步數哦。",
                "我們會根據您的年齡推薦合理的建議值，您也可以依照自己的期望設定每日行走目標哦!\n您的推薦步數是：${pet_companion_pedomoter_goal.pet_companion_pedomoter_goal_suggestion.toString()}步"
            ),
            listOf("開始設定", "完成"),
            listOf("", "請輸入至少1000的目標步數")
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .background(color = Color(ContextCompat.getColor(context, R.color.bgPetCompany))),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar()
            PetImage(
                navController = navController,
                pet_companion_pedomoter_goal = pet_companion_pedomoter_goal,
                showDialog = showDialog
            )
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter)
            {
                UseItem(userInevntory, showDialog)
            }
        }
        //統一管理所有AlertDialog的觸發
        when {
            showDialog.value.isShowDialog && showDialog.value.case == "settingGoal" ->
                alertDialogForPetCompany(
                    buttonText = goalSetting.nextButtonText[i.value],
                    nextStage = if ((goalSetting.stage[i.value] == 2 && goalSteps.value.toInt() < 1000)) false else true,
                    showDialog = showDialog.value.isShowDialog,
                    onDismiss = {
                        //stage判斷進行(目標步數設定)的進度
                        if (goalSetting.stage[i.value] != goalSetting.stage[goalSetting.stage.size - 1]) {
                            i.value += 1
                        } else {
                            showDialog.value = ShowDialog(true, "waiting")
                        }
                    },
                    onConfirm = {
                        // 設定進度為2且输入檢測為数字時，上傳資料
                        if (goalSetting.stage[i.value] == 2 && goalSteps.value.all { it.isDigit() }) {
                            prefs.edit().apply {
                                putInt("goalSteps", goalSteps.value.toInt())
                                apply()
                            }
                            //將目標步數上傳給後台
                            ApiUSR.postUserSetting(
                                SessionManager(context).getUserToken()
                                    .toString(),
                                postUserSetting(
                                    config_key = "pet_companion_pedomoter_goal",
                                    value = goalSteps.value.toInt(),
                                ),
                                onSuccess = {
                                    showDialog.value = ShowDialog(false, "")
                                    Log.d(
                                        "Debug",
                                        "PetCompany_MainPage:上傳成功"
                                    )
                                },
                                onError = {
                                    showDialog.value = ShowDialog(false, "")
                                    Log.d(
                                        "Debug",
                                        "PetCompany_MainPage:上傳失敗"
                                    )
                                },
                                onInternetError = {
                                    showDialog.value = ShowDialog(false, "")
                                    Log.d(
                                        "Debug",
                                        "PetCompany_MainPage:上傳失敗"
                                    )
                                }
                            )
                        }
                    },
                    title = goalSetting.title[i.value],
                    view =
                    @Composable
                    {
                        Column(
                        )
                        {
                            FixedSizeText(
                                text = goalSetting.text[i.value],
                                size = 70.dp
                            )
                            when {
                                goalSetting.stage[i.value] == 1 -> {

                                }

                                goalSetting.stage[i.value] == 2 -> {
                                    TextField(
                                        value = goalSteps.value,
                                        onValueChange = { newValue ->
                                            goalSteps.value = newValue.ifEmpty { "0" }
                                        },
                                        label = { Text("請輸入目標步數") },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        singleLine = true,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    )
                                }
                            }
                        }
                    },
                    color = colorResource(id = R.color.btnPetcompanyColor),
                    backgroundColor = colorResource(id = R.color.bgPetCompany),
                    alertMessage = goalSetting.alertMessage[i.value],
                )

            showDialog.value.isShowDialog && showDialog.value.case == "waiting" ->
                waitingDialog(
                    title = "設定中，請稍後",
                    color = colorResource(id = R.color.btnPetcompanyColor),
                    onDismiss = { /*利用waiting控制*/ },
                    backgroundColor = colorResource(id = R.color.bgPetCompany),
                )

            showDialog.value.isShowDialog && showDialog.value.case == "waiting2" ->
                waitingDialog(
                    title = "使用道具中，請稍後",
                    color = colorResource(id = R.color.btnPetcompanyColor),
                    onDismiss = { /*利用waiting控制*/ },
                    backgroundColor = colorResource(id = R.color.bgPetCompany),
                )

            showDialog.value.isShowDialog && showDialog.value.case == "resetGoalSteps" ->
                resetSomething(
                    onDismiss = { showDialog.value = ShowDialog(false, "") },
                    onConfirm = {
                        ApiUSR.postUserSetting(
                            SessionManager(context).getUserToken()
                                .toString(),
                            postUserSetting(
                                config_key = "pet_companion_pedomoter_goal",
                                value = newGoalSteps.toInt(),
                            ),
                            onSuccess = {
                                showDialog.value = ShowDialog(false, "")
                                Log.d(
                                    "Debug",
                                    "PetCompany_MainPage:上傳成功${newGoalSteps}"
                                )
                            },
                            onError = {
                                showDialog.value = ShowDialog(false, "")
                                Log.d(
                                    "Debug",
                                    "PetCompany_MainPage:上傳失敗"
                                )
                            },
                            onInternetError = {
                                showDialog.value = ShowDialog(false, "")
                                Log.d(
                                    "Debug",
                                    "PetCompany_MainPage:上傳失敗"
                                )
                            }
                        )
                    },
                    reset = "resetGoalSteps",
                    newGoalSteps = newGoalSteps,
                    newName = newName,
                    onUpdateGoalSteps = { newGoalSteps = it },
                    onUpdateName = { newName = it },
                    alertMessage = "請輸入至少1000的目標步數"
                )

            showDialog.value.isShowDialog && showDialog.value.case == "resetPetName" ->
                resetSomething(
                    onDismiss = { showDialog.value = ShowDialog(false, "") },
                    onConfirm = {
                        showDialog.value = ShowDialog(true, "waiting")
                    },
                    reset = "resetPetName",
                    newGoalSteps = newGoalSteps,
                    newName = newName,
                    onUpdateGoalSteps = { newGoalSteps = it },
                    onUpdateName = { newName = it },
                    alertMessage = "寵物名稱不可為空"
                )

            showDialog.value.isShowDialog && showDialog.value.case == "usingItem" ->
                twoButtonDialog(
                    title = "提示",
                    content = "目前使用道具不會有任何效果,但是仍會消耗道具，請問是否仍要使用道具?",
                    buttonYes = "是",
                    buttonNo = "否",
                    showDialog = true,
                    onDismiss = { showDialog.value = ShowDialog(false, "") },
                    onConfirm =
                    {
                        showDialog.value = ShowDialog(true, "waiting2")
                        ApiUSR.postUserUseItem(
                            SessionManager(context).getUserToken()
                                .toString(),
                            postUserUseItem(
                                itemID = showDialog.value.itemID!!,
                                quantity = 1,
                            ),
                            onSuccess = {
                                showDialog.value = ShowDialog(false, "")
                                Log.d(
                                    "Debug",
                                    "PetCompany_MainPage:使用道具成功"
                                )
                            },
                            onError = {
                                showDialog.value = ShowDialog(false, "")
                                Log.d(
                                    "Debug",
                                    "PetCompany_MainPage:使用道具失敗"
                                )
                            },
                            onInternetError = {
                                showDialog.value = ShowDialog(false, "")
                                Log.d(
                                    "Debug",
                                    "PetCompany_MainPage:使用道具失敗"
                                )
                            }
                        )

                    },
                    color = colorResource(id = R.color.btnPetcompanyColor),
                    backgroundColor = colorResource(id = R.color.bgPetCompany),
                )
        }
    }
}

//重新設定使用者相關資訊時的function
@Composable
fun resetSomething(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    reset: String,
    newGoalSteps: String,
    onUpdateGoalSteps: (String) -> Unit,
    newName: String,
    onUpdateName: (String) -> Unit,
    alertMessage: String
) {
    val alert = remember {
        mutableStateOf(false)
    }
    AlertDialog(
        modifier = Modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(
                5.dp,
                colorResource(id = R.color.btnPetcompanyColor),
                shape = RoundedCornerShape(16.dp)
            ),
        backgroundColor = colorResource(id = R.color.bgPetCompany),
        onDismissRequest = { onDismiss() },
        title = {
            Column {
                FixedSizeText(
                    text =
                    if (reset == "resetGoalSteps") {
                        "重新設定目標步數"
                    } else if (reset == "resetPetName") {
                        "為寵物重新取名"
                    } else "錯誤",
                    size = 80.dp,
                    fontWeight = FontWeight.Bold
                )
                TextField(
                    value =
                    when (reset) {
                        "resetGoalSteps" -> newGoalSteps
                        "resetPetName" -> newName
                        else -> "錯誤"
                    },
                    onValueChange = {
                        when (reset) {
                            "resetGoalSteps" -> {
                                //檢查是否全數字
                                if (it.all { char -> char.isDigit() }) {
                                    onUpdateGoalSteps(it)
                                }
                            }

                            "resetPetName" -> onUpdateName(it)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.White),
                    keyboardOptions =
                    if (reset == "resetGoalSteps") {
                        KeyboardOptions(keyboardType = KeyboardType.Number)
                    } else if (reset == "resetPetName") {
                        KeyboardOptions.Default
                    } else KeyboardOptions.Default,
                    textStyle = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight(700),
                        color = Color(0xFF2F3032),
                    ),
                    placeholder = {
                        FixedSizeText(
                            text = when (reset) {
                                "resetGoalSteps" -> "請輸入新目標"
                                "resetPetName" -> "請輸入新名稱"
                                else -> "輸入值"
                            },
                            size = 60.dp,
                        )
                    },
                    singleLine = true,
                )
            }
        },
        text = {
            if (alert.value) {
                Spacer(modifier = Modifier.height(10.dp))
                FixedSizeText(alertMessage, size = 50.dp, color = Color.Red)
            }
        },
        buttons = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = { onDismiss() },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(id = R.color.btnPetcompanyColor),
                        contentColor = Color.White
                    ), modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(8.dp)
                ) {
                    FixedSizeText("取消", size = 60.dp, color = Color.White)
                }
                Button(
                    onClick = {
                        if (
                            when (reset) {
                                "resetGoalSteps" -> newGoalSteps.toInt() >= 1000
                                "resetPetName" -> newName.isNotEmpty()
                                else -> false
                            }
                        ) {
                            onConfirm();onDismiss();alert.value = false
                        } else {
                            alert.value = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(id = R.color.btnPetcompanyColor),
                        contentColor = Color.White
                    ), modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(8.dp)
                ) {
                    FixedSizeText("確認", size = 60.dp, color = Color.White)
                }
            }
        },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    )
}

@Composable
fun alertDialogForPetCompany(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    showDialog: Boolean,
    title: String,
    buttonText: String,
    nextStage: Boolean = true,
    view: @Composable () -> Unit,
    color: Color,
    backgroundColor: Color,
    alertMessage: String,
) {
    val alert = remember {
        mutableStateOf(false)
    }
    if (showDialog) {
        AlertDialog(
            modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(5.dp, color, shape = RoundedCornerShape(16.dp)),
            backgroundColor = backgroundColor,
            onDismissRequest = { onDismiss() },
            title = { FixedSizeText(text = title, size = 80.dp, fontWeight = FontWeight.Bold) },
            text = { view() },
            buttons = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (alert.value) {
                        FixedSizeText(alertMessage, size = 50.dp, color = Color.Red)
                    }
                    Button(
                        onClick = {
                            if (nextStage) {
                                onConfirm();onDismiss();alert.value = false
                            } else {
                                alert.value = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = color,
                            contentColor = Color.White
                        ), modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        FixedSizeText(buttonText, size = 60.dp, color = Color.White)
                    }
                }
            },
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        )
    }
}

//上傳資料後產生的loading畫面
@Composable
fun waitingDialog(
    onDismiss: () -> Unit,
    title: String,
    color: Color,
    backgroundColor: Color,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
    ) {
        AlertDialog(
            modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(5.dp, color, shape = RoundedCornerShape(16.dp)),
            backgroundColor = backgroundColor,
            onDismissRequest = { onDismiss() },
            title = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    FixedSizeText(text = title, size = 80.dp, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Loading(isVisible = true)
                }
            },
            buttons = {
            },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        )
    }
}


