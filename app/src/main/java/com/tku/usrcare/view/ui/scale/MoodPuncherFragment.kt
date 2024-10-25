package com.tku.usrcare.view.ui.scale

import android.content.Intent
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.android.exoplayer2.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
import com.tku.usrcare.R
import com.tku.usrcare.api.ApiUSR
import com.tku.usrcare.model.MoodPuncher
import com.tku.usrcare.model.MoodPuncherSave
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.component.AutoSizedText
import com.tku.usrcare.view.component.Loading
import com.tku.usrcare.view.component.findActivity
import com.tku.usrcare.view.component.normalAlertDialog
import com.tku.usrcare.viewmodel.ScaleViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MoodPuncherButton(navController: NavHostController) {
    val context = LocalContext.current
    val sessionManager = SessionManager(context)
    val isAiMoodPuncherApprove = remember { mutableStateOf(false) }
    isAiMoodPuncherApprove.value = sessionManager.getApproveAiMoodPuncher()
    val isAiMoodPuncherApproveDialogVisible = remember { mutableStateOf(true) }
    isAiMoodPuncherApproveDialogVisible.value = !isAiMoodPuncherApprove.value

    if (isAiMoodPuncherApproveDialogVisible.value) {
        AlertDialog(onDismissRequest = { /*TODO*/ }, title = {}, text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "您是否願意讓AI協助分析情緒呢?")
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    Button(
                        onClick = {
                            sessionManager.saveApproveAiMoodPuncher(true)
                            isAiMoodPuncherApproveDialogVisible.value = false
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.white),
                            contentColor = colorResource(id = R.color.black),
                        ),
                        border = BorderStroke(
                            width = 2.dp, color = colorResource(id = R.color.red)
                        ),
                    ) {
                        Text(text = "是")
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        onClick = {
                            context.findActivity()?.finish()
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.white),
                            contentColor = colorResource(id = R.color.black),
                        ),
                        border = BorderStroke(
                            width = 2.dp, color = colorResource(id = R.color.red)
                        ),
                    ) {
                        Text(text = "否")
                    }
                }
            }
        }, confirmButton = {})
    }
    val modifier = Modifier
        .border(
            width = 3.dp,
            color = colorResource(id = R.color.btnMoodScaleColor),
            shape = RoundedCornerShape(size = 16.dp)
        )
        .width(330.dp)
        .height(119.dp)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 15.dp, start = 20.dp, end = 20.dp),
        contentAlignment = Alignment.Center,
    ) {
        Button(
            onClick = {
                navController.navigate("MoodPuncher")
            },
            modifier = modifier,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.white))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_mood_puncher),
                    contentDescription = stringResource(id = R.string.mood_puncher),
                    tint = colorResource(id = R.color.btnMoodScaleColor),
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .weight(0.3f)
                )
                Spacer(
                    modifier = Modifier
                        .width(20.dp)
                        .weight(0.1f)
                )
                Text(
                    textAlign = TextAlign.Center,
                    text = stringResource(id = R.string.mood_puncher),
                    style = TextStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight(700),
                        color = Color(0xFF2F3032),
                    ),
                    modifier = Modifier.weight(0.8f)
                )
            }
        }
    }
}

@Composable
fun MoodPuncherPage(
    navController: NavHostController, scaleViewModel: ScaleViewModel
) {
    val isFloatingActionButtonExtended = remember { mutableStateOf(true) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.bgScale)),
        contentAlignment = Alignment.TopCenter,
    ) {
        val lazyListState = remember { LazyListState() }
        val firstVisibleItemIndex =
            remember { derivedStateOf { lazyListState.firstVisibleItemIndex } }
        isFloatingActionButtonExtended.value = firstVisibleItemIndex.value == 0
        val moodPuncherList = remember {
            scaleViewModel.getMoodPuncherList() ?: arrayListOf()
        }
        LaunchedEffect(Unit) {
            scaleViewModel.moodPuncherList.observeForever {
                moodPuncherList.clear()
                moodPuncherList.addAll(it ?: arrayListOf())
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                contentAlignment = Alignment.Center, modifier = Modifier
                    .border(
                        2.dp,
                        colorResource(id = R.color.btnMoodScaleColor),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .background(
                        color = colorResource(id = R.color.white), shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Text(
                    text = "歷史紀錄",
                    color = colorResource(id = R.color.btnMoodScaleColor),
                    fontSize = 30.sp,
                    modifier = Modifier.padding(
                        top = 4.dp, start = 15.dp, end = 15.dp, bottom = 4.dp
                    )
                )
            }
            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 15.dp, start = 20.dp, end = 20.dp, bottom = 15.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                items(moodPuncherList.size) { index ->
                    val moodPuncherSave = moodPuncherList[index]
                    MoodPuncherItem(
                        moodPuncherSave = moodPuncherSave, scaleViewModel, navController,index
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = {
                scaleViewModel.saveMoodNowText("")
                scaleViewModel.saveMoodNowResponse("")
                navController.navigate("MoodPuncherEditor")
            },
            content = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(10.dp)
                ) {
                    Icon(
                        Icons.Filled.Edit,
                        "Extended floating action button.",
                    )
                    AnimatedVisibility(visible = isFloatingActionButtonExtended.value) {
                        Row {
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = "紀錄今天的心情")
                        }
                    }
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 40.dp, end = 25.dp),
            containerColor = colorResource(id = R.color.SecondaryButtonColor),
            contentColor = colorResource(id = R.color.btnMoodScaleColor),
        )
    }
}


@Composable
fun MoodPuncherItem(
    moodPuncherSave: MoodPuncherSave,
    scaleViewModel: ScaleViewModel,
    navController: NavHostController,
    index:Int
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(colorResource(id = R.color.white))
            .fillMaxWidth()
            .height(55.dp)
            .clickable {//點擊提取文字後翻頁
                scaleViewModel.saveMoodNowText(moodPuncherSave.moodText)
                scaleViewModel.saveMoodNowResponse(moodPuncherSave.moodResponse)
                navController.navigate("MoodPuncherView/$index")
            },
        contentAlignment = Alignment.Center,

        ) {
        //將時間格式自"yyyy-MM-dd'T'HH:mm:ss"轉為"yyyy年MM月dd日 HH時"
        val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.TAIWAN)

        // 建立一個 SimpleDateFormat 物件來格式化為新的格式
        val targetFormat = SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.TAIWAN)

        // 使用 originalFormat 解析傳入的字符串
        val date: Date =
            originalFormat.parse(moodPuncherSave.dateTime, java.text.ParsePosition(0)) ?: Date()
        val dateTime = targetFormat.format(date)
        AutoSizedText(text = dateTime, color = colorResource(id = R.color.black), size = 20)
    }
}

//心情打字機編輯頁面
@Composable
fun MoodPuncherEditorPage(
    navController: NavHostController,
    scaleViewModel: ScaleViewModel,
    startVoiceInput: ActivityResultLauncher<Intent>,
    isHistory:Boolean,
    index:Int,
) {
    val sessionManager = SessionManager(LocalContext.current)
    val timeFormat = scaleViewModel.timeFormat
    val now: String = timeFormat.format(System.currentTimeMillis())
    var isShowDialog = remember { mutableStateOf(false) }
    normalAlertDialog(
        onDismiss = { /*TODO*/ },
        onConfirm = { isShowDialog.value = false},
        showDialog = isShowDialog.value,
        title = "提示",
        content = "您似乎忘記分析結果了，請先按下立即分析，產生分析結果後才能儲存。",
        buttonText = "我知道了",
        color = colorResource(id = R.color.btnMoodScaleColor),
        backgroundColor =colorResource(id = R.color.bgSatKTV)
    )
    Column {
        Box(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxHeight(0.8f)
                .clip(
                    RoundedCornerShape(
                        size = 16.dp
                    )
                )
        ) {
            if(isHistory)
            {
                MoodPuncherViewTypeBox(scaleViewModel, navController)
            }
            else
            {
                MoodPuncherEditorTypeBox(scaleViewModel, startVoiceInput, navController)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f),
            horizontalArrangement = Arrangement.Center
        ) {
            if(isHistory)
            {
                Button(
                    onClick = { navController.navigateUp();sessionManager.removeMoodPuncherAt(index) },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.white),
                        contentColor = colorResource(id = R.color.black),
                    ),
                    border = BorderStroke(
                        width = 2.dp, color = colorResource(id = R.color.red)
                    ),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "delete",
                            modifier = Modifier.size(30.dp),
                            tint = colorResource(id = R.color.red)
                        )
                        AutoSizedText(
                            text = "刪除此紀錄", color = colorResource(id = R.color.black), size = 20
                        )
                    }
                }
            }
            else
            {
                Button(
                    onClick = { navController.navigateUp() },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.white),
                        contentColor = colorResource(id = R.color.black),
                    ),
                    border = BorderStroke(
                        width = 2.dp, color = colorResource(id = R.color.red)
                    ),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "delete",
                            modifier = Modifier.size(30.dp),
                            tint = colorResource(id = R.color.red)
                        )
                        AutoSizedText(
                            text = "取消", color = colorResource(id = R.color.black), size = 20
                        )
                    }
                }
                Spacer(modifier = Modifier.width(20.dp))
                Button(
                    onClick = {
                        //Log.d("MoodPuncherFragment",scaleViewModel.getMoodNowResponse())
                        if(scaleViewModel.getMoodNowResponse()=="")
                        {
                            isShowDialog.value = true
                        }
                        else
                        {
                            scaleViewModel.addMoodPuncherList(
                                MoodPuncherSave(
                                    moodText = scaleViewModel.getMoodNowText(),
                                    moodResponse = scaleViewModel.getMoodNowResponse(),
                                    dateTime = now,
                                    negativeScore = 0,
                                    positiveScore = 0,
                                    srs = 0
                                )
                            )
                            navController.navigateUp()
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.white),
                        contentColor = colorResource(id = R.color.btnMoodScaleColor),
                    ),
                    border = BorderStroke(
                        width = 2.dp, color = colorResource(id = R.color.MainButtonColor)
                    ),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_checkmark),
                            contentDescription = stringResource(id = R.string.confirm),
                            modifier = Modifier.size(30.dp),
                        )
                        AutoSizedText(text = "儲存", color = colorResource(id = R.color.black))
                    }
                }
            }
        }
    }
}

@Composable
fun MoodPuncherEditorTypeBox(
    scaleViewModel: ScaleViewModel,
    startVoiceInput: ActivityResultLauncher<Intent>,
    navController: NavHostController
) {
    val mood = remember { mutableStateOf("") }
    val waiting = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val score = remember { mutableIntStateOf(0) }
    val property = remember { mutableStateOf("") }
    val suggestion = remember { mutableStateOf("") }
    //val isHistory=remember { mutableStateOf(false) }
    //分析結果
    fun analyze() {
        //Log.d("Debug", "進入分析")
        if (scaleViewModel.getMoodNowText() != "") {
            //Log.d("Debug", "進入分析判斷式")
            val moodPuncher = MoodPuncher(
                moodText = scaleViewModel.getMoodNowText()
            )
            waiting.value = true
            ApiUSR.postMoodPuncher(SessionManager(context), moodPuncher, {
                waiting.value = false
                mood.value = it.mood
                score.intValue = it.score
                property.value = it.property
                suggestion.value = it.suggestion

                scaleViewModel.saveMoodNowResponse(suggestion.value)
                Log.d("Debug", "進入分析成功")
            }, { waiting.value = false;Log.d("Debug", "進入分析失敗") })
        }
    }

    fun onVoiceInputButtonClick(startVoiceInput: ActivityResultLauncher<Intent>) {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "請說話")
        }
        startVoiceInput.launch(intent)
    }

    fun recognizeText(
        image: InputImage, scaleViewModel: ScaleViewModel, navHostController: NavHostController
    ) {
        // 初始化文字識別器
        val recognizer = TextRecognition.getClient(ChineseTextRecognizerOptions.Builder().build())

        recognizer.process(image).addOnSuccessListener { visionText ->
            scaleViewModel.saveMoodNowText(visionText.text)
        }.addOnFailureListener { e ->
            Toast.makeText(
                navHostController.context, "文字識別失敗: ${e.message}", Toast.LENGTH_SHORT
            ).show()
            navHostController.navigateUp()
        }
    }

    val takePictureLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicturePreview(),
            onResult = { bitmap ->
                if (bitmap != null) {
                    // 將Bitmap轉換為InputImage
                    val inputImage = InputImage.fromBitmap(bitmap, 0)
                    recognizeText(inputImage, scaleViewModel, navController)
                }
            })

    val cameraPermissionState = remember { mutableStateOf(false) }
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            cameraPermissionState.value = true
            takePictureLauncher.launch(null)
        } else {
            Toast.makeText(
                context, "相機權限被拒絕", Toast.LENGTH_SHORT
            ).show()
        }
    }



    Column {
        val moodNowText = remember {
            mutableStateOf("")
        }
        scaleViewModel.moodNowText.observeForever {
            moodNowText.value = it
        }
        TextField(
            value = moodNowText.value,
            onValueChange = { scaleViewModel.saveMoodNowText(it) },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.45f)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                ),
            textStyle = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight(700),
                color = Color(0xFF2F3032),
            ),
            placeholder = {
                AutoSizedText(
                    text = "您現在心情怎麼樣呢?",
                    color = colorResource(id = R.color.btnMoodScaleColor),
                    size = 20
                )
            },
            colors = TextFieldDefaults.colors(
                cursorColor = colorResource(id = R.color.btnMoodScaleColor),
                focusedIndicatorColor = colorResource(id = R.color.white),
                unfocusedIndicatorColor = colorResource(id = R.color.white),
                disabledIndicatorColor = colorResource(id = R.color.white),
                unfocusedContainerColor = colorResource(id = R.color.white),
                focusedContainerColor = colorResource(id = R.color.white),
            )
        )
        Box(
            modifier = Modifier
                .fillMaxHeight(0.2f)
                .fillMaxWidth()
                .background(color = colorResource(id = R.color.white)),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 15.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = {
                    onVoiceInputButtonClick(startVoiceInput)
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_mic),
                        contentDescription = stringResource(id = R.string.microphone),
                        tint = colorResource(id = R.color.btnMoodScaleColor),
                        modifier = Modifier
                            .padding(5.dp)
                            .size(30.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                IconButton(onClick = {
                    //檢查是否有相機權限
                    if (context.checkSelfPermission(android.Manifest.permission.CAMERA) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                        takePictureLauncher.launch(null)
                    } else {
                        requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                    }
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_text_scan),
                        contentDescription = stringResource(id = R.string.text_scan),
                        tint = colorResource(id = R.color.btnMoodScaleColor),
                        modifier = Modifier
                            .padding(5.dp)
                            .size(30.dp)
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(id = R.color.white).copy(alpha = 0.7f))
        ) {
            Column {
                Spacer(
                    modifier = Modifier
                        .height(5.dp)
                        .fillMaxHeight(0.01f)
                )
                Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (waiting.value) {
                            //Log.d("Debug", "loading判斷式為真")
                            Loading(isVisible = waiting.value)
                        } else {
                            //Log.d("Debug", "loading判斷式為假")
                            if (scaleViewModel.getMoodNowResponse() == "") {
                                //Log.d("Debug", "進入立即分析判斷式")
                                Button(
                                    onClick = {
                                        analyze()
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = colorResource(id = R.color.white),
                                        contentColor = colorResource(id = R.color.btnMoodScaleColor),
                                    ),
                                    border = BorderStroke(
                                        width = 2.dp,
                                        color = colorResource(id = R.color.btnMoodScaleColor)
                                    ),
                                ) {
                                    Text(text = "立即分析")
                                }
                            } else {
                                //Log.d("Debug", "進入再試一次判判斷式")
                                LazyColumn(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    item{
                                        Text(text = scaleViewModel.getMoodNowResponse())
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Button(
                                            onClick = {
                                                //Log.d("Debug",""+scaleViewModel.getMoodNowText())
                                                //scaleViewModel.saveMoodNowResponse("")
                                                suggestion.value = ""
                                                analyze()
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = colorResource(id = R.color.white),
                                                contentColor = colorResource(id = R.color.btnMoodScaleColor),
                                            ),
                                            border = BorderStroke(
                                                width = 2.dp,
                                                color = colorResource(id = R.color.btnMoodScaleColor)
                                            ),
                                        ) {
                                            Text(text = "再試一次")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun MoodPuncherViewTypeBox(
    scaleViewModel: ScaleViewModel,
    navController: NavHostController
) {
    /*val mood = remember { mutableStateOf("") }
    val context = LocalContext.current
    val score = remember { mutableIntStateOf(0) }
    val property = remember { mutableStateOf("") }
    val suggestion = remember { mutableStateOf("") }*/

    Column {
        val moodNowText = remember {
            mutableStateOf("")
        }
        scaleViewModel.moodNowText.observeForever {
            moodNowText.value = it
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.45f)
                .background(color = colorResource(id = R.color.white)),)
        {
            item{
                Text(
                    text = moodNowText.value,
                    color = Color(0xFF2F3032),
                    fontSize = 20.sp,
                    fontWeight = FontWeight(700),
                    modifier = Modifier.padding(15.dp)
                )
            }
        }
        /*TextField(
            value = moodNowText.value,
            enabled=true,
            onValueChange = { scaleViewModel.saveMoodNowText(it) },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.45f)
                .background(color = colorResource(id = R.color.white)),
            textStyle = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight(700),
                color = Color(0xFF2F3032),
            ),
        )*/
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(id = R.color.white).copy(alpha = 0.7f))
        ) {
            Column {
                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                        .fillMaxHeight(0.01f)
                )
                Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LazyColumn(
                            modifier = Modifier.padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            item{
                                Text(text = scaleViewModel.getMoodNowResponse())
                            }
                        }
                    }
                }
            }
        }
    }
}


