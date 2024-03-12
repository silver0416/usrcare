package com.tku.usrcare.view.ui.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tku.usrcare.R
import com.tku.usrcare.view.component.FixedSizeText
import com.tku.usrcare.viewmodel.SettingViewModel

@Composable
fun Question(settingViewModel: SettingViewModel, navController: NavHostController)
{
    data class Question(val title: String)
    val Questions = listOf(
        Question("Q: 收不到通知怎麼辦？"),
        Question("Q: 忘記密碼？"),
        Question("Q: 簽簽樂是什麼功能？"),
        Question("Q: 每日任務是什麼功能？"),
        Question("Q: 動腦小遊戲是什麼功能？"),
        Question("Q: 寵物陪伴是什麼功能？"),
        Question("Q: AI活力偵測是什麼功能？"),
        Question("Q: 鬧鐘小提醒是什麼功能？"),
        Question("Q: 週六KTV是什麼功能？"),
        Question("Q: 心情量表是什麼功能？"),
    )
    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight())
    {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
        ) {
            Button(
                onClick = {
                    navController.navigateUp()
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
                Icon(
                    painter = painterResource(id = R.drawable.ic_question),
                    contentDescription = "常見問題",
                    modifier = Modifier
                        .padding(end = 16.dp, top = 8.dp)
                        .size(35.dp),
                    tint = Color.Black,
                )
                FixedSizeText(
                    text = "常見問題", size = 90.dp, color = Color.Black, fontWeight = FontWeight.Bold
                )
            }
        }
        Box(
            modifier = Modifier
                .padding(20.dp)
                .shadow( // 添加陰影
                    elevation = 4.dp, // 陰影的高度
                    shape = RoundedCornerShape(16.dp)
                ) // 陰影的形狀
                .clip(RoundedCornerShape(16.dp)) // 設定圓角
                .background(Color.White) // 設定背景顏色為白色
        )
        {
            LazyColumn()
            {
                items(Questions)
                {item ->
                    Button(onClick = { /*TODO*/ },
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxSize()
                            .size(60.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor =colorResource(id = R.color.TextfieldBoxStrokeColor), contentColor=Color.Black),
                        elevation = ButtonDefaults.elevation( // 移除按鈕的陰影
                            defaultElevation = 0.dp,
                            pressedElevation = 0.dp,
                            disabledElevation = 0.dp
                        ),
                        shape = RoundedCornerShape(0.dp),
                        ) {
                        Row (horizontalArrangement = Arrangement.Start,modifier = Modifier.fillMaxSize())
                        {
                            FixedSizeText(//這裡有文字過長導致後半段看不到的問題
                                text = item.title, size = 70.dp, color = Color.Black, fontWeight = FontWeight.Bold,
                            )
                        }

                }
            }

        }

    }
}}