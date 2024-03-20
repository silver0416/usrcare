package com.tku.usrcare.view.ui.petcompany

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter.State.Empty.painter
import com.tku.usrcare.R
import com.tku.usrcare.view.component.FixedSizeText
import com.tku.usrcare.view.component.TitleBox
import com.tku.usrcare.view.ui.ktv.Balance
import com.tku.usrcare.view.ui.ktv.Exchange
import com.tku.usrcare.viewmodel.PetCompanyViewModel
import com.tku.usrcare.viewmodel.SettingViewModel


@Composable
fun TopBar(){
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
fun HealthAndStore(petCompanyViewModel: PetCompanyViewModel, navController: NavHostController)
{
    Row (
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
    ){
        Icon(//可能不要用icon寫
            painter = painterResource(id = R.drawable.ic_pet_health_bar),
            contentDescription = "親密度",
            modifier = Modifier
                .size(219.dp,45.dp),
            tint = Color.Unspecified)
        Box(
            modifier = Modifier.size(100.dp).clip(CircleShape).border(width = 5.dp, color = colorResource(id = R.color.purple_200), shape = CircleShape),
            contentAlignment = Alignment.Center
        )
        {
            Button(
                modifier = Modifier.size(50.dp).clip(CircleShape),
                onClick = { navController.navigate("Store") },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorResource(id = R.color.bgSatKTV),
                ),contentPadding = PaddingValues(1.dp)
            ) {
                Icon(
                    modifier = Modifier
                        .size(50.dp),
                    painter = painterResource(id=R.drawable.ic_coin),
                    contentDescription ="寵物商店",
                    tint = Color.Unspecified)
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
fun MainPage(petCompanyViewModel: PetCompanyViewModel, navController: NavHostController) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(ContextCompat.getColor(context, R.color.bgSatKTV))),
        contentAlignment = Alignment.TopCenter
    ) {
        Column {
            TopBar()
            HealthAndStore(petCompanyViewModel=petCompanyViewModel,navController = navController)
            //這邊可以添加新的function
        }
    }
}