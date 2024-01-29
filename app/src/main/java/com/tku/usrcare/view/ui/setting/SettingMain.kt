import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavHostController
import com.tku.usrcare.R
import com.tku.usrcare.repository.ImageSaver
import com.tku.usrcare.repository.ReminderBuilder
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.LoginActivity
import com.tku.usrcare.view.component.AutoSizedText
import com.tku.usrcare.view.component.FixedSizeText
import com.tku.usrcare.view.component.findActivity
import com.tku.usrcare.view.ui.setting.Cheater
import com.tku.usrcare.view.ui.setting.CheaterLock
import com.tku.usrcare.view.ui.setting.UpdateCheckerDialog
import com.tku.usrcare.view.ui.theme.UsrcareTheme
import com.tku.usrcare.viewmodel.SettingViewModel

@Composable
fun TopBar() {
    val context = LocalContext.current
    val activity = context.findActivity()
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
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
                imageVector = Icons.Default.ArrowBack,
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
                text = "設定", size = 90.dp, color = Color.Black, fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
fun SettingsList(settingViewModel: SettingViewModel, navController: NavHostController) {
    val context = LocalContext.current
    val sessionManager = SessionManager(context)
    val showUpdateCheckerDialog = remember { mutableStateOf(false) }
    val showCheaterDialog = remember { mutableStateOf(false) }
    val showLogoutCheckDialog = remember { mutableStateOf(false) }
    val startOAuth = remember { mutableStateOf(false) }
    val googleText = remember { mutableStateOf("綁定Google快速登入") }
    val lineText = remember { mutableStateOf("綁定LINE快速登入") }

    if (sessionManager.getOAuthCheck().google) {
        googleText.value = "取消綁定Google"
    }
    if (sessionManager.getOAuthCheck().line) {
        lineText.value = "取消綁定LINE"
    }

    data class SettingItem(val title: String, val icon: Int)
    // 定義列表的項目
    val items = listOf(
        SettingItem(googleText.value, R.drawable.ic_google),
        SettingItem(lineText.value, R.drawable.ic_line),
        SettingItem("個人檔案", R.drawable.ic_profile),
        SettingItem("常見問題", R.drawable.ic_question),
        SettingItem("聯絡我們", R.drawable.ic_contact),
        SettingItem("密碼與帳號安全", R.drawable.ic_password),
        SettingItem("關於APP", R.drawable.ic_info),
        SettingItem("隱私政策", R.drawable.ic_privacy),
        SettingItem("服務條款", R.drawable.ic_service),
        SettingItem("輸入獎勵代碼", R.drawable.ic_gift),
        SettingItem("檢查更新", R.drawable.ic_update),
        SettingItem("登出", R.drawable.ic_logout),
    )


    fun navigator(item: SettingItem) {
        when (item.title) {
            "綁定Google快速登入" -> {
                navController.navigate("google")
            }

            "取消綁定Google" -> {
                navController.navigate("unbind/google")
            }

            "綁定LINE快速登入" -> {
                navController.navigate("line")
            }

            "取消綁定LINE" -> {
                navController.navigate("unbind/line")
            }

            "個人檔案" -> {
                //todo
            }

            "常見問題" -> {
                //todo
            }

            "聯絡我們" -> {
                //todo
            }

            "密碼與帳號安全" -> {
                //todo
            }

            "關於APP" -> {
                //todo
            }

            "隱私政策" -> {
                //todo
            }

            "服務條款" -> {
                //todo
            }

            "輸入獎勵代碼" -> {
                showCheaterDialog.value = true
            }

            "檢查更新" -> {
                showUpdateCheckerDialog.value = true
            }

            "登出" -> {
                showLogoutCheckDialog.value = true
            }
        }
    }

    if (showLogoutCheckDialog.value) {
        AlertDialog(onDismissRequest = {
            showLogoutCheckDialog.value = false
        }, title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_logout),
                    contentDescription = "登出",
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(35.dp),
                    tint = Color.Red,
                )
                Text(
                    text = "登出", fontSize = 32.sp, fontWeight = FontWeight.Bold
                )
            }
        }, text = {
            Text(text = "確定要登出嗎？", fontSize = 24.sp)
        }, confirmButton = {
            androidx.compose.material3.Button(
                onClick = {
                    //這裡處理登出
                    val intent = Intent(context, LoginActivity::class.java)
                    settingViewModel.unsubscribeAllFirebaseTopic()
                    ReminderBuilder(context.applicationContext).cancelAllAlarm()
                    sessionManager.clearAll(context = context)
                    ImageSaver().clearAllImageFromInternalStorage(context)
                    startActivity(context, intent, null)
                }, modifier = Modifier.size(100.dp, 50.dp)
            ) {
                AutoSizedText(text = "確定", size = 21, color = Color.White)
            }
        }, dismissButton = {
            TextButton(
                onClick = {
                    showLogoutCheckDialog.value = false
                }, modifier = Modifier.size(100.dp, 50.dp)
            ) {
                Text(text = "取消", fontSize = 21.sp)
            }
        })
    }

    if (showUpdateCheckerDialog.value) {
        UpdateCheckerDialog(showUpdateCheckerDialog)
    }

    if (showCheaterDialog.value) {
        if (sessionManager.getCheatAccess()) {
            context.findActivity()?.let { Cheater(it) }
        } else {
            CheaterLock(
                activity = context.findActivity()!!,
                showCheaterDialog,
                settingViewModel
            )
        }
    }


    Box(modifier = Modifier.padding(20.dp)) {
        Box(
            modifier = Modifier
                .shadow( // 添加陰影
                    elevation = 4.dp, // 陰影的高度
                    shape = RoundedCornerShape(16.dp)
                ) // 陰影的形狀
                .fillMaxWidth() // 使列表填滿螢幕的寬度
                .clip(RoundedCornerShape(16.dp)) // 設定圓角
                .background(Color.White) // 設定背景顏色為白色
        ) {
            LazyColumn { // 使用LazyColumn來創建列表
                items(items) { item ->
                    Button(
                        onClick = {
                            navigator(item)
                        },
                        modifier = Modifier
                            .align(Alignment.Center) // 靠左對齊
                            .fillMaxSize()
                            .size(if (item.title == "登出") 90.dp else 60.dp), // 為按鈕添加內間距
                        colors = ButtonDefaults.buttonColors( // 設定按鈕的顏色
                            backgroundColor = if (item.title == "已綁定Google" || item.title == "已綁定LINE") {
                                colorResource(id = R.color.TextfieldBoxStrokeColor)
                            } else {
                                Color.Transparent
                            },
                            contentColor = if (item.title == "登出") Color.Red else Color.Black // 如果項目是"登出"，則設定文字顏色為紅色，否則為黑色
                        ),
                        elevation = ButtonDefaults.elevation( // 移除按鈕的陰影
                            defaultElevation = 0.dp,
                            pressedElevation = 0.dp,
                            disabledElevation = 0.dp
                        ),
                        shape = RoundedCornerShape(0.dp), // 設定按鈕的圓角
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = if (item.title == "登出") Arrangement.Center else Arrangement.Start,
                            ) {
                                if (item.title != "綁定Google快速登入" && item.title != "綁定LINE快速登入" && item.title != "取消綁定Google" && item.title != "取消綁定LINE") {
                                    Icon(
                                        painter = painterResource(id = item.icon),
                                        contentDescription = item.title,
                                        modifier = Modifier
                                            .padding(end = 16.dp)
                                            .size(if (item.title == "登出") 35.dp else 30.dp),
                                        tint = if (item.title == "登出") Color.Red else Color.Black,
                                    )
                                } else {
                                    Image(
                                        painter = if (item.title == "綁定Google快速登入" || item.title == "取消綁定Google") painterResource(
                                            id = R.drawable.ic_google
                                        ) else painterResource(id = R.drawable.ic_line),
                                        contentDescription = item.title,
                                        modifier = Modifier
                                            .padding(end = 16.dp)
                                            .size(30.dp),
                                    )
                                }
                                FixedSizeText(
                                    text = item.title,
                                    size = if (item.title == "登出") 95.dp else 76.dp,
                                    color = if (item.title == "登出") Color.Red else Color.Black,
                                    fontWeight = FontWeight.Bold
                                )
                                when (item.title) {
                                    "登出" -> {
                                    }

                                    else -> {
                                        Row(
                                            horizontalArrangement = Arrangement.End,
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.fillMaxSize()
                                        ) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_arrow_right),
                                                contentDescription = item.title,
                                                modifier = Modifier
                                                    .padding(start = 16.dp)
                                                    .size(15.dp),
                                                tint = Color.Black,
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (item.title != "登出") {
                        HorizontalDivider(color = Color.Black)
                    }
                }
            }
        }
    }
}

@Composable
fun SettingMain(settingViewModel: SettingViewModel, navController: NavHostController) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(ContextCompat.getColor(context, R.color.bgMain))),
        contentAlignment = Alignment.TopCenter
    ) {
        Column {
            TopBar()
            SettingsList(settingViewModel = settingViewModel, navController = navController)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview(showBackground = true)
@Composable
fun PreviewSettingsList() {
    UsrcareTheme {

    }
}