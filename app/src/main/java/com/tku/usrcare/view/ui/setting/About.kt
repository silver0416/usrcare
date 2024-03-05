package com.tku.usrcare.view.ui.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.tku.usrcare.R
import com.tku.usrcare.view.component.FixedSizeText
import com.tku.usrcare.viewmodel.SettingViewModel

@Composable
fun About(settingViewModel: SettingViewModel, navController: NavHostController)
{
    val scrollState = rememberScrollState()
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
                    painter = painterResource(id = R.drawable.ic_info),
                    contentDescription = "關於APP",
                    modifier = Modifier
                        .padding(end = 16.dp,top=8.dp)
                        .size(35.dp),
                    tint = Color.Black,
                )
                FixedSizeText(
                    text = "關於APP", size = 90.dp, color = Color.Black, fontWeight = FontWeight.Bold
                )
            }
        }
        Box(
            modifier = Modifier.verticalScroll(scrollState)//給box增加滑動功能
                .padding(20.dp)
                .shadow( // 添加陰影
                    elevation = 4.dp, // 陰影的高度
                    shape = RoundedCornerShape(16.dp)
                ) // 陰影的形狀
                .clip(RoundedCornerShape(16.dp)) // 設定圓角
                .background(Color.White) // 設定背景顏色為白色
        )
        {
            Text(
                text = stringResource(id = R.string.about_app),
                modifier = Modifier.padding(16.dp),
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp)
        }

    }
}