import android.content.Intent
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.tku.usrcare.R
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.LoginActivity
import com.tku.usrcare.view.findActivity
import com.tku.usrcare.view.ui.theme.UsrcareTheme

@Composable
fun TopBar(){
    val context = LocalContext.current
    val activity = context.findActivity()
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(top =50.dp, start = 20.dp, end = 20.dp), verticalAlignment = Alignment.CenterVertically) {
            Button(
                onClick = {
                    activity?.finish()
                },
                modifier = Modifier
                    .size(43.dp)
                    .clip(CircleShape)
                ,
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
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "設定"
            )
        }
    }
}

@Composable
fun SettingsList() {
    val context = LocalContext.current
    val sessionManager = SessionManager(context)
    // 定義列表的項目
    val items = listOf(
        "通知管理",
        "常見問題",
        "意見回饋",
        "密碼與帳號安全",
        "關於app",
        "隱私權政策",
        "服務條款",
        "登出"
    )

    Box(modifier = Modifier.padding(16.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth() // 使列表填滿螢幕的寬度
                .clip(RoundedCornerShape(16.dp)) // 設定圓角
                .background(Color.White) // 設定背景顏色為白色
        ) {
            LazyColumn { // 使用LazyColumn來創建列表
                items(items) { item ->
                    Button(
                        onClick = {
                                  if (item == "登出") {
                                        val intent = Intent(context, LoginActivity::class.java)
                                        sessionManager.clearAll()
                                        sessionManager.delAllClock(context = context)
                                        startActivity(context, intent, null)
                                  }
                                  else{

                                  }
                        },
                        modifier = Modifier
                            .align(Alignment.CenterStart) // 靠左對齊
                            .padding(if (item == "登出") 20.dp else 1.dp), // 為按鈕添加內間距
                        colors = ButtonDefaults.buttonColors( // 設定按鈕的顏色
                            backgroundColor = Color.Transparent, // 設定按鈕背景為透明
                            contentColor = if (item == "登出") Color.Red else Color.Black // 如果項目是"登出"，則設定文字顏色為紅色，否則為黑色
                        ),
                        elevation = ButtonDefaults.elevation( // 移除按鈕的陰影
                            defaultElevation = 0.dp,
                            pressedElevation = 0.dp,
                            disabledElevation = 0.dp
                        )
                    ) {
                        Text(text = item)
                    }
                    Divider(color = Color.Black) // 在每個按鈕之間添加一條黑色的分隔線
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
            .background(color = Color(ContextCompat.getColor(context, R.color.bgMain)))
    ) {
        Column {
            TopBar()
            SettingsList()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSettingsList() {
    UsrcareTheme {
        SettingMain()
    }
}