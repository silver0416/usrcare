package com.tku.usrcare.view.ui.petcompany

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.draw.shadow
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
import androidx.compose.ui.Alignment
import org.checkerframework.common.subtyping.qual.Bottom

@Composable
fun TopBar() {
    Column(
        Modifier
            .background(colorResource(id = R.color.bgSatKTV))
            .fillMaxWidth()
    ) {
        TitleBox(
            color = colorResource(id = R.color.btnPetcompanyColor),
            title = stringResource(id = R.string.pet_company),
            icon = painterResource(id = R.drawable.ic_petcompany)
        )
    }
}

@Composable
fun HealthAndStore(petCompanyViewModel: PetCompanyViewModel, navController: NavHostController) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Icon(//可能不要用icon寫
            painter = painterResource(id = R.drawable.ic_pet_health_bar),
            contentDescription = "親密度",
            modifier = Modifier
                .size(219.dp, 45.dp),
            tint = Color.Unspecified
        )
        Box(
            modifier = Modifier
                .size(100.dp)
                .shadow(elevation = 8.dp, shape = CircleShape)
                .border(
                    width = 5.dp,
                    color = colorResource(id = R.color.purple_200),
                    shape = CircleShape
                )
                .background(color = colorResource(id = R.color.bgSatKTV), CircleShape),
            contentAlignment = Alignment.Center
        )
        {
            Button(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                onClick = { navController.navigate("Store") },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorResource(id = R.color.bgSatKTV),
                ), contentPadding = PaddingValues(1.dp)
            ) {
                Icon(
                    modifier = Modifier
                        .size(50.dp),
                    painter = painterResource(id = R.drawable.ic_coin),
                    contentDescription = "寵物商店",
                    tint = Color.Unspecified
                )
            }
        }

    }
    data class PetCompanyItem(val title: String, val icon: Int)
    // 定義列表的項目
    val items = listOf(
        PetCompanyItem("商店", R.drawable.ic_coin),
        PetCompanyItem("道具1", R.drawable.ic_mood1),
        PetCompanyItem("道具2", R.drawable.ic_mood2),
    )

    fun navigator(item: PetCompanyItem) {
        when (item.title) {
            "商店" -> {
                navController.navigate("store")
            }
            "道具1" -> {
                navController.navigate("item1")
            }
            "道具2" -> {
                navController.navigate("item2")
            }
        }
    }
}

@Composable
fun PetImage() {
    val screenHeightDp = LocalConfiguration.current.screenHeightDp
    val heightFraction = 0.4//圖片高度佔螢幕高度的比例
    val boxHeight = (screenHeightDp * heightFraction).dp
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(boxHeight)
            .padding(18.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.pet),
            contentDescription = "寵物本人",
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
        )
    }
}

@Composable
fun UseItem() {
    val screenHeightDp = LocalConfiguration.current.screenHeightDp
    val heightFraction = 0.2//圖片高度佔螢幕高度的比例
    val boxHeight = (screenHeightDp * heightFraction).dp
    data class PetItem(val title: String, val image: Painter)

    val PetItems = listOf(
        PetItem("道具1", painterResource(id = R.drawable.ball)),
        PetItem("道具2", painterResource(id = R.drawable.food)),
        PetItem("道具3", painterResource(id = R.drawable.clean_item)),
    )
    Box(//不知道為什麼沒辦法讓物件置中對齊
        modifier = Modifier
            .fillMaxWidth()
            .height(boxHeight),
        contentAlignment = Alignment.Center,
    )
    {
        LazyRow(modifier = Modifier.fillMaxSize()) {
            items(PetItems) { item ->
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .padding(12.dp)
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(5.dp, colorResource(id = R.color.purple_200), CircleShape),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(id = R.color.bgSatKTV),
                    )
                ) {
                    Image(painter = item.image, contentDescription = "道具圖片",Modifier.size(50.dp))
                }
            }
        }
    }
}

@Composable
fun MainPage(petCompanyViewModel: PetCompanyViewModel, navController: NavHostController) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .background(color = Color(ContextCompat.getColor(context, R.color.bgSatKTV))),
        contentAlignment = Alignment.TopCenter
    ) {
        Column (){
            TopBar()
            HealthAndStore(petCompanyViewModel = petCompanyViewModel, navController = navController)
            PetImage()
            UseItem()
            //這邊可以添加新的function
        }
    }
}