package com.tku.usrcare.view.ui.petcompany

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import androidx.navigation.NavHostController
import com.tku.usrcare.R
import com.tku.usrcare.view.component.FixedSizeText
import com.tku.usrcare.view.component.TitleBox
import com.tku.usrcare.viewmodel.PetCompanyViewModel


@Composable
fun Store(petCompanyViewModel: PetCompanyViewModel, navController: NavHostController)
{
    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .background(colorResource(id = R.color.bgSatKTV)))
    {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
        ) {
            Button(//返回按鈕
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
            Row(//topbar的代幣兌換和圖案
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 50.dp),
            ) {
                FixedSizeText(
                    text = "\uD83D\uDCB0代幣兌換", size = 90.dp, color = Color.Black, fontWeight = FontWeight.Bold
                )
            }
        }
        Box(
            modifier = Modifier//這裡有點小問題
                .padding(20.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))// 設定圓角
                .border(width = 5.dp, color = colorResource(id = R.color.purple_200)),// 設定背景顏色為KTV紫色
            contentAlignment = Alignment.Center
            )
        {
            Column {//這裡是擁有代幣的框
                Icon(//文字置中，放大
                    painter = painterResource(id=R.drawable.ic_coin),
                    contentDescription ="代幣圖案",
                    tint = Color.Unspecified,
                    )
                Text(text = "暫定")
                Column {//這裡是商品兌換區
                    Row {
                        TitleBox(//不要用titleBox，也不要放在column裡面
                            color = colorResource(id = R.color.purple_200),
                            title = stringResource(id = R.string.food),
                            icon = painterResource(id = R.drawable.ic_petcompany)
                        )
                    }

                }
            }
            
        }
    }
}