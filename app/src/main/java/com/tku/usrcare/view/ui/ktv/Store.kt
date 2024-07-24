package com.tku.usrcare.view.ui.ktv

import android.content.Context
import android.hardware.SensorEventListener
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.tku.usrcare.R
import com.tku.usrcare.view.component.AutoSizedText
import com.tku.usrcare.view.component.FixedSizeText
import com.tku.usrcare.view.component.TitleBox
import com.tku.usrcare.view.component.normalAlertDialog
import com.tku.usrcare.viewmodel.PetCompanyViewModel

@Composable
fun Store()
{
    var showUseInformation by remember { mutableStateOf(true) }
    normalAlertDialog(
        title = "此區功能尚未完整",
        content = "敬請期待",
        buttonText = "我知道了",
        showDialog=showUseInformation,
        onDismiss = { showUseInformation = false },
        onConfirm = {  },
        color = colorResource(id = R.color.btnSatKTVColor),
        backgroundColor = colorResource(id = R.color.bgSatKTV))

    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .background(colorResource(id = R.color.bgSatKTV)))
    {
        TitleBox(
            color = colorResource(id = R.color.btnSatKTVColor),
            title = stringResource(id = R.string.good_things_store),
            icon = painterResource(id = R.drawable.ic_ktv)
        )
        moneyFrame()
        shopFrame()
    }
}

@Composable
fun moneyFrame(money: Int = 0)
{
    Box(
        modifier = Modifier
            .padding(30.dp)
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(36.dp))// 設定圓角程度
            .border(
                width = 7.dp,
                color = colorResource(id = R.color.btnSatKTVColor),
                shape = RoundedCornerShape(36.dp)
            ),
        contentAlignment = Alignment.Center
    )
    {
        Column() {//這裡是擁有代幣的框
            Icon(
                painter = painterResource(id=R.drawable.ic_coin),
                contentDescription ="代幣圖案",
                tint = Color.Unspecified,
            )
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = money.toString(),
                fontSize = 40.sp)//獲取實際代幣數量之後取代即可
        }
    }
}
@Composable
fun shopFrame(){
    data class Items(
        val title: String,
        val image: Painter,
        val color: Color,
        val price: Int,
        val enabled: Boolean = true
    )

    val StoreItemList = listOf(
        Items(
            stringResource(id = R.string.pet_food),
            painterResource(id = R.drawable.food),
            colorResource(id = R.color.bgSatKTV),
            10
        ),
        Items(
            stringResource(id = R.string.pet_toy),
            painterResource(id = R.drawable.ball),
            colorResource(id = R.color.bgSatKTV),
            20
        ),
        Items(
            stringResource(id = R.string.pet_clean_item),
            painterResource(id = R.drawable.clean_item),
            colorResource(id = R.color.bgSatKTV),
            5
        ))

    @Composable
    fun SingleLineButton(StoreItemList: Items) {
        Row(
            Modifier
                .width(250.dp)
                .height(100.dp)
                .padding(5.dp)
                .background(color = Color.White, shape = RoundedCornerShape(24.dp))
                .border(
                    width = 5.dp,
                    color = colorResource(id = R.color.btnSatKTVColor),
                    shape = RoundedCornerShape(24.dp)
                ),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                modifier = Modifier
                    .size(50.dp)
                    .padding(3.dp),
                painter = StoreItemList.image,
                contentDescription = "道具圖片",
            )
            Box(modifier = Modifier.padding(start = 10.dp)) {
                AutoSizedText(
                    text = StoreItemList.title,
                    size = 30,
                    color = Color.Black
                )
            }
        }
    }

    Box (
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        contentAlignment = Alignment.Center){//這裡是商品兌換區
        LazyColumn (
                modifier = Modifier
                .fillMaxWidth(),
                verticalArrangement = Arrangement.Center){
                items(StoreItemList){ item ->
                    Row (
                        Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center){
                        SingleLineButton(item)
                        Column (verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                            Button(
                                modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape),
                                onClick = { /*TODO*/ },
                                contentPadding = PaddingValues(1.dp))
                            {
                                Icon(
                                    modifier = Modifier.fillMaxSize(),
                                    painter = painterResource(id=R.drawable.ic_coin),
                                    contentDescription ="Coin Icon",
                                    tint = Color.Unspecified,
                                )
                            }
                            Text(text = item.price.toString(),fontSize = 40.sp)//顯示物品價格
                        }
                    }
                }
            }
        }
}



