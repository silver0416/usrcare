package com.tku.usrcare.view.ui.petcompany

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImagePainter.State.Empty.painter
import com.tku.usrcare.view.component.FixedSizeText
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
        horizontalArrangement = Arrangement.End,
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        /*Icon(//可能不要用icon寫
            painter = painterResource(id = R.drawable.ic_pet_health_bar),
            contentDescription = "親密度",
            modifier = Modifier
                .size(219.dp, 45.dp),
            tint = Color.Unspecified
        )*/
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
fun PetImage(petCompanyViewModel: PetCompanyViewModel, navController: NavHostController) {
    val screenHeightDp = LocalConfiguration.current.screenHeightDp
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val heightFraction = 0.6//圖片高度佔螢幕高度的比例
    val boxHeight = (screenHeightDp * heightFraction).dp
    var petDetailVisible by remember { mutableStateOf(false) }
    val text = petCompanyViewModel.steps.observeAsState(0)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(boxHeight),
        //.padding(18.dp),
        //.background(color = Color.Blue),
        contentAlignment = Alignment.Center
    ) {
        Column {
            if (petDetailVisible) {
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize()
                        .clip(RoundedCornerShape(32.dp))
                        .background(color = Color.LightGray)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Box (contentAlignment = Alignment.TopEnd){
                            Image(
                                painter = painterResource(id = R.drawable.pet),
                                contentDescription = "寵物圖片",
                                modifier = Modifier
                                    .height((screenHeightDp * 0.2).dp)
                                    .fillMaxWidth()
                                    .zIndex(1f)
                            )
                            Icon(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clickable { petDetailVisible = !petDetailVisible }.zIndex(2f),
                                painter = painterResource(id = R.drawable.ic_back),
                                contentDescription = "返回",
                                tint = Color.Unspecified
                            )
                        }

                        //之後用items寫
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        )
                        {
                            FixedSizeText(
                                text = "寵物名稱",
                                size = 80.dp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Column()
                        {//改items寫
                            FixedSizeText(
                                text = "親密度",
                                size = 60.dp,
                                fontWeight = FontWeight.Bold
                            )
                            FixedSizeText(
                                text = "飽足感",
                                size = 60.dp,
                                fontWeight = FontWeight.Bold
                            )
                            FixedSizeText(
                                text = "清潔度",
                                size = 60.dp,
                                fontWeight = FontWeight.Bold
                            )
                            FixedSizeText(
                                text = "健康值",
                                size = 60.dp,
                                fontWeight = FontWeight.Bold
                            )
                            FixedSizeText(
                                text = "寵物行走里程: ${text.value}步",
                                color = Color.Black,
                                size = 60.dp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            } else {
                HealthAndStore(petCompanyViewModel = petCompanyViewModel, navController = navController)
                Image(
                    painter = painterResource(id = R.drawable.pet),
                    contentDescription = "寵物本人",
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .clickable { petDetailVisible = !petDetailVisible })
            }
        }
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
    Box(
//不知道為什麼沒辦法讓物件置中對齊
        modifier = Modifier
            .fillMaxWidth()
            .height(boxHeight),
        //.background(color= Color.Black),
        contentAlignment = Alignment.Center,
    )
    {

        LazyRow(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center) {
            items(PetItems) { item ->
                Box(contentAlignment = Alignment.BottomEnd){
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
                        Box(modifier = Modifier.zIndex(1f)){
                            Image(
                                modifier = Modifier
                                    .zIndex(1f)
                                    .fillMaxSize()
                                    .padding(3.dp),
                                painter = item.image,
                                contentDescription = "道具圖片",
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(color= Color.Blue)
                            .size(40.dp)
                            .zIndex(2f),
                        contentAlignment = Alignment.Center
                    ){
                        Text(text="0",color= Color.White, fontWeight = FontWeight.Bold, fontSize = 30.sp)
                    }
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
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar()
            //HealthAndStore(petCompanyViewModel = petCompanyViewModel, navController = navController)
            PetImage(petCompanyViewModel, navController = navController)
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter)
            {
                UseItem()
            }

            //這邊可以添加新的function
        }
    }
}