package com.tku.usrcare.view.ui.petcompany

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil.setContentView
import androidx.navigation.NavHostController
import com.tku.usrcare.R
import com.tku.usrcare.view.SignSignHappyActivity
import com.tku.usrcare.view.component.AutoSizedText
import com.tku.usrcare.view.component.FixedSizeText
import com.tku.usrcare.view.component.TitleBox
import com.tku.usrcare.view.component.findActivity
import com.tku.usrcare.viewmodel.PetCompanyViewModel

@Composable
fun Store(petCompanyViewModel: PetCompanyViewModel, navController: NavHostController, sensorEventListener: SensorEventListener?=null, context: Context, listener: SensorEventListener?=null)
{
    var money=0
    data class Items(
        val title: String,
        val icon: Int,
        val color: Color,
        val enabled: Boolean = true
    )
    val StoreItemList = listOf(
        Items(
            stringResource(id = R.string.pet_food),
            R.drawable.ic_petcompany,
            colorResource(id = R.color.bgSatKTV)
        ),
        Items(
            stringResource(id = R.string.pet_toy),
            R.drawable.ic_petcompany,
            colorResource(id = R.color.bgSatKTV)
        ),
        Items(
            stringResource(id = R.string.pet_clean_item),
            R.drawable.ic_petcompany,
            colorResource(id = R.color.bgSatKTV)
        ))
    @Composable
    fun SingleLineButton(StoreItemList: Items) {
            Row(
                Modifier
                    //.fillMaxSize()
                    .padding(start = 8.dp, end = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(id = StoreItemList.icon),
                    contentDescription = StoreItemList.title,
                    tint = if (StoreItemList.enabled) StoreItemList.color else Color.Black,
                )
                Box(modifier = Modifier.padding(start = 8.dp)) {
                    AutoSizedText(
                        text = StoreItemList.title,
                        size = 30,
                        color = Color.Black
                    )
                }
            }
        }


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
                .height(200.dp)
                .clip(RoundedCornerShape(48.dp))// 設定圓角程度
                .border(width = 5.dp, color = colorResource(id = R.color.purple_200),shape = RoundedCornerShape(48.dp)),// 設定背景顏色為KTV紫色//邊線要設定shape
            contentAlignment = Alignment.Center
            )
        {
            Column() {//這裡是擁有代幣的框
                Icon(//文字置中，放大
                    painter = painterResource(id=R.drawable.ic_coin),
                    contentDescription ="代幣圖案",
                    tint = Color.Unspecified,
                    )
                Text(modifier = Modifier.align(Alignment.CenterHorizontally),text = money.toString())//獲取實際代幣數量之後取代即可
            }
        }
        Column (
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center
                )
        {
            /*Box (modifier = Modifier.fillMaxWidth(),contentAlignment = Alignment.Center){//這裡是商品兌換區
                Row (verticalAlignment = Alignment.CenterVertically){
                    SingleLineButton(StoreItemList[0])
                    Column {
                        Button(onClick = { /*TODO*/ },shape = CircleShape,//點擊消耗代幣獲得物品
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent,
                                )
                        )
                        {
                            Icon(
                                //modifier = Modifier.fillMaxSize(),
                                painter = painterResource(id=R.drawable.ic_coin),
                                contentDescription ="代幣圖案",
                                tint = Color.Unspecified,
                            )
                        }
                        Text(text = "這裡寫價格")
                    }
                }
            }*/

        }
    }

    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

    if (stepCounterSensor != null) {
        sensorManager.registerListener(listener, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL)
    } else {
        // Handle case when step counter sensor is not available
    }
}

fun stopStepCounter(context: Context, listener: SensorEventListener) {
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    sensorManager.unregisterListener(listener)
}


