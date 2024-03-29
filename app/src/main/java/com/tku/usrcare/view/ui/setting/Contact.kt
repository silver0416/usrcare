package com.tku.usrcare.view.ui.setting

import androidx.compose.foundation.Image
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
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tku.usrcare.R
import com.tku.usrcare.view.component.FixedSizeText
import com.tku.usrcare.viewmodel.SettingViewModel
import androidx.compose.ui.unit.sp
@Composable
fun Contact(settingViewModel: SettingViewModel, navController: NavHostController)
{
    data class Contact(var title: String)
    val Contacts = listOf(
        Contact("計劃官網"),
        Contact("FB粉絲專頁"),
        Contact("若有任何寶貴的意見，歡迎來信至:tkuusrcare@gmail.com"),
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
                    painter = painterResource(id = R.drawable.ic_contact),
                    contentDescription = "聯絡我們",
                    modifier = Modifier
                        .padding(end = 16.dp, top = 8.dp)
                        .size(35.dp),
                    tint = Color.Black,
                )
                FixedSizeText(
                    text = "聯絡我們", size = 90.dp, color = Color.Black, fontWeight = FontWeight.Bold
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
                items(Contacts)
                {item ->
                    if (item.title=="計劃官網"||item.title=="FB粉絲專頁")
                    {
                        Button(onClick = { /*TODO*/ },
                            modifier = Modifier
                                .align(Alignment.Center)
                                .fillMaxSize()
                                .size(60.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = colorResource(id = R.color.TextfieldBoxStrokeColor), contentColor= Color.Black),
                            elevation = ButtonDefaults.elevation( // 移除按鈕的陰影
                                defaultElevation = 0.dp,
                                pressedElevation = 0.dp,
                                disabledElevation = 0.dp
                            ),
                            shape = RoundedCornerShape(0.dp),
                        ) {
                            Row (modifier = Modifier
                                .fillMaxSize()
                                .align(Alignment.CenterVertically))
                            {
                                if (item.title=="計劃官網")
                                {
                                    Image(painter =  painterResource(id = R.drawable.logo_b), contentDescription = "USRlogo",
                                        modifier = Modifier
                                            .padding(end = 4.dp, top = 2.dp)
                                            .size(50.dp),
                                    )
                                }
                                else if (item.title=="FB粉絲專頁")
                                {
                                    Image(painter =  painterResource(id = R.drawable.fb_icon), contentDescription = "FBlogo",
                                        modifier = Modifier
                                            .padding(end = 4.dp, top = 2.dp)
                                            .size(50.dp),
                                    )
                                }
                                FixedSizeText(
                                    text = item.title, size = 60.dp ,color = Color.Black, fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    else
                        Box(modifier = Modifier.padding(16.dp))
                        {
                            Text(//這裡有毛病
                                text = item.title, color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 24.sp
                            )
                        }
                }
            }
        }
    }
}