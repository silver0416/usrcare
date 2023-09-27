
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
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
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.tku.usrcare.R
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.LoginActivity
import com.tku.usrcare.view.component.FixedSizeText
import com.tku.usrcare.view.findActivity
import com.tku.usrcare.view.ui.setting.UpdateCheckerDialog
import com.tku.usrcare.view.ui.theme.UsrcareTheme

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
                text = "設定",
                size = 90.dp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }
    }
}



@Composable
fun SettingsList() {
    val context = LocalContext.current
    val sessionManager = SessionManager(context)
    val showUpdateCheckerDialog = remember { mutableStateOf(false) }

    data class SettingItem(val title: String, val icon: Int)
    // 定義列表的項目
    val items = listOf(
        SettingItem("個人檔案", R.drawable.ic_profile),
        SettingItem("常見問題", R.drawable.ic_question),
        SettingItem("聯絡我們", R.drawable.ic_contact),
        SettingItem("密碼與帳號安全", R.drawable.ic_password),
        SettingItem("關於APP", R.drawable.ic_info),
        SettingItem("隱私政策", R.drawable.ic_privacy),
        SettingItem("服務條款", R.drawable.ic_service),
        SettingItem("檢查更新", R.drawable.ic_update),
        SettingItem("登出", R.drawable.ic_logout),
    )

    fun logout() {
        val intent = Intent(context, LoginActivity::class.java)
        sessionManager.clearAll(context = context)
        startActivity(context, intent, null)
    }


    fun navigator(item: SettingItem) {
        when (item.title) {
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
            "檢查更新" -> {
                showUpdateCheckerDialog.value = true
            }
            "登出" -> {
                logout()
            }
        }
    }

    if (showUpdateCheckerDialog.value) {
        UpdateCheckerDialog(showUpdateCheckerDialog)
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
                            backgroundColor = Color.Transparent, // 設定按鈕背景為透明
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
                                Icon(
                                    painter = painterResource(id = item.icon),
                                    contentDescription = item.title,
                                    modifier = Modifier
                                        .padding(end = 16.dp)
                                        .size(if (item.title == "登出") 35.dp else 30.dp),
                                    tint = if (item.title == "登出") Color.Red else Color.Black,
                                )
                                FixedSizeText(
                                    text = item.title,
                                    size = if (item.title == "登出") 95.dp else 76.dp,
                                    color = if (item.title == "登出") Color.Red else Color.Black,
                                    fontWeight = FontWeight.Bold
                                )
                                if (item.title != "登出") {
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
                    if (item.title != "登出") {
                        Divider(
                            color = Color.Black,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SettingMain() {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(ContextCompat.getColor(context, R.color.bgMain))),
        contentAlignment = Alignment.TopCenter
    ) {
        Column {
            TopBar()
            SettingsList()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview(showBackground = true)
@Composable
fun PreviewSettingsList() {
    UsrcareTheme {
        SettingMain()
    }
}