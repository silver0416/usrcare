package com.tku.usrcare.view.ui.ktv

import android.content.Context
import android.hardware.SensorEventListener
import android.util.Log
import android.widget.Space
import android.widget.Switch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.tku.usrcare.R
import com.tku.usrcare.api.ApiUSR
import com.tku.usrcare.model.ShoppingImformations
import com.tku.usrcare.model.getItemsResponse
import com.tku.usrcare.model.item

import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.KtvActivity
import com.tku.usrcare.view.component.AutoSizedText
import com.tku.usrcare.view.component.FixedSizeText
import com.tku.usrcare.view.component.Loading
import com.tku.usrcare.view.component.TitleBox
import com.tku.usrcare.view.component.normalAlertDialog
import com.tku.usrcare.viewmodel.PetCompanyViewModel
import javax.annotation.meta.When

@Composable
fun Store() {
    val activity = LocalContext.current as KtvActivity
    val sessionManager = SessionManager(activity)
    val points = remember {
        mutableIntStateOf(sessionManager.getPoints())
    }
    var items by remember { mutableStateOf(getItemsResponse(listOf())) }
    val isWaiting = remember {
        mutableStateOf(false)
    }
    val goUpdatePoints = remember {
        mutableStateOf(false)
    }

    data class ShowDialog(
        val isShowDialog: Boolean,
        val case: String,
        val price: Int? = 0,
        val itemID: Int? = 0,
        val itemName: String = ""
    )

    val showDialog = remember {
        mutableStateOf(ShowDialog(false, ""))
    }

    ApiUSR.getPoints(
        SessionManager(activity).getUserToken().toString(),
        onSuccess = {
            points.intValue = it
        },
        onError = {
            points.intValue = 0
        },
        onInternetError = {
            points.intValue = 0
        }
    )
    ApiUSR.getItems(
        SessionManager(activity).getUserToken().toString(),
        onSuccess = {
            if (it != null) {
                items = it
                Log.d("Store", it.toString())
            } else {
                Log.d("Store", "API回傳null")
            }
        },
        onError = {
            Log.d("Store", "Error")
        },
        onInternetError = {
            Log.d("Store", "InternetError")
        }
    )
    if (goUpdatePoints.value) {
        ApiUSR.getPoints(
            SessionManager(activity).getUserToken().toString(),
            onSuccess = {
                points.intValue = it
            },
            onError = {
                points.intValue = 0
            },
            onInternetError = {
                points.intValue = 0
            }
        )
        goUpdatePoints.value = false
    }

    @Composable
    fun waitingDialog(
        onDismiss: () -> Unit,
        showDialog: Boolean,
        title: String,
        color: Color,
        backgroundColor: Color,
    ) {
        if (showDialog) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
            )
            {
                AlertDialog(
                    modifier = Modifier
                        .padding(16.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(5.dp, color, shape = RoundedCornerShape(16.dp)),
                    backgroundColor = backgroundColor,
                    onDismissRequest = { onDismiss() },
                    title = {
                        FixedSizeText(
                            text = title,
                            size = 80.dp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    text = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Loading(isVisible = true)
                        }
                    },
                    buttons = {
                    },
                    properties = DialogProperties(
                        dismissOnBackPress = false,
                        dismissOnClickOutside = false
                    )
                )
            }
        }
    }

    @Composable
    fun moneyFrame(points: Int) {
        Box(
            modifier = Modifier
                .padding(30.dp)
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(36.dp))
                .border(
                    width = 7.dp,
                    color = colorResource(id = R.color.btnSatKTVColor),
                    shape = RoundedCornerShape(36.dp)
                ),
            contentAlignment = Alignment.Center
        )
        {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_coin),
                    contentDescription = "代幣圖案",
                    tint = Color.Unspecified,
                )
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = points.toString(),
                    fontSize = 40.sp
                )
            }
        }
    }

    @Composable
    fun ShoppingDialog(
        onDismiss: () -> Unit,
        onConfirm: () -> Unit,
        showDialog: Boolean,
        title: String,
        content: String,
        buttonYes: String,
        buttonNo: String,
        color: Color,
        backgroundColor: Color,
        quantity: Int,
        onQuantityChange: (Int) -> Unit
    ) {
        if (showDialog) {
            AlertDialog(
                modifier = Modifier
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(5.dp, color, shape = RoundedCornerShape(16.dp)),
                backgroundColor = backgroundColor,
                onDismissRequest = { onDismiss() },
                title = { FixedSizeText(text = title, size = 80.dp, fontWeight = FontWeight.Bold) },
                text = {
                    Column() {
                        FixedSizeText(
                            text = content,
                            size = 70.dp
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(
                                onClick = { if (quantity > 0) onQuantityChange(quantity - 1) },
                                enabled = quantity > 0,
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = color,
                                    contentColor = Color.White
                                )
                            ) {
                                Text("-1", color = Color.White)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            FixedSizeText(
                                text = quantity.toString(),
                                size = 70.dp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Button(
                                onClick = { onQuantityChange(quantity + 1) },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = color,
                                    contentColor = Color.White
                                )
                            ) {
                                Text("+1", color = Color.White)
                            }
                        }
                    }
                },
                buttons = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(//取消按鈕
                            onClick = { onDismiss() },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = color,
                                contentColor = Color.White
                            ), modifier = Modifier.padding(3.dp)
                        ) {
                            FixedSizeText(buttonNo, size = 60.dp, color = Color.White)
                        }
                        Button(//確認按鈕
                            onClick = {
                                onConfirm()
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = color,
                                contentColor = Color.White
                            ), modifier = Modifier.padding(3.dp)
                        ) {
                            FixedSizeText(buttonYes, size = 60.dp, color = Color.White)
                        }
                    }
                },
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                )
            )
        }
    }
    var quantity by remember { mutableStateOf(1) }
    @Composable
    fun shopFrame() {
        @Composable
        fun SingleLineButton(Item: item) {

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
                Button(
                    modifier = Modifier
                        .width(250.dp)
                        .height(100.dp)
                        .clip(RoundedCornerShape(24.dp)),
                    onClick = {
                        showDialog.value = ShowDialog(true, "buyCheck", Item.price, Item.itemID, Item.name?:"")
                              },
                    contentPadding = PaddingValues(1.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                )
                {
                    Image(
                        modifier = Modifier
                            .size(50.dp)
                            .padding(3.dp),
                        painter = rememberAsyncImagePainter(Item.image_path),
                        contentDescription = "道具圖片",
                    )
                    Box(modifier = Modifier.padding(start = 10.dp)) {
                        AutoSizedText(
                            text = Item.name,
                            size = 30,
                            color = Color.Black
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {//這裡是商品兌換區
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                items(items.items) { item ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        SingleLineButton(item)
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                modifier = Modifier.fillMaxSize(),
                                painter = painterResource(id = R.drawable.ic_coin),
                                contentDescription = "Coin Icon",
                                tint = Color.Unspecified,
                            )
                            Text(text = item.price.toString(), fontSize = 40.sp)//顯示物品價格
                        }
                    }
                }
            }
        }
    }
    //mainpage
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(colorResource(id = R.color.bgSatKTV))
    )
    {
        TitleBox(
            color = colorResource(id = R.color.btnSatKTVColor),
            title = stringResource(id = R.string.good_things_store),
            icon = painterResource(id = R.drawable.ic_ktv)
        )
        moneyFrame(points.value)
        shopFrame()
    }
    //所有dialog由同一個變數控制
    when {
        showDialog.value.isShowDialog && showDialog.value.case == "pointsNotEnough" ->
            normalAlertDialog(
                title = "購買資訊",
                content = "金幣不足!!購買失敗。",
                buttonText = "好",
                showDialog = showDialog.value.isShowDialog,
                onDismiss = { showDialog.value = ShowDialog(false, "") },
                onConfirm = { },
                color = colorResource(id = R.color.btnSatKTVColor),
                backgroundColor = colorResource(id = R.color.bgSatKTV)
            )

        showDialog.value.isShowDialog && showDialog.value.case == "purchaseCompleted" ->
            normalAlertDialog(
                title = "購買資訊",
                content = "購買完成!",
                buttonText = "好",
                showDialog = showDialog.value.isShowDialog,
                onDismiss = { showDialog.value = ShowDialog(false, "") },
                onConfirm = { },
                color = colorResource(id = R.color.btnSatKTVColor),
                backgroundColor = colorResource(id = R.color.bgSatKTV)
            )

        showDialog.value.isShowDialog && showDialog.value.case == "notWorking" ->
            normalAlertDialog(
                title = "錯誤",
                content = "出現錯誤，請檢查網路之後重新嘗試。",
                buttonText = "我知道了",
                showDialog = showDialog.value.isShowDialog,
                onDismiss = { showDialog.value = ShowDialog(false, "") },
                onConfirm = { },
                color = colorResource(id = R.color.btnSatKTVColor),
                backgroundColor = colorResource(id = R.color.bgSatKTV)
            )

        showDialog.value.isShowDialog && showDialog.value.case == "buyCheck" ->
            ShoppingDialog(
                onDismiss = { showDialog.value = ShowDialog(false, "") },
                onConfirm = {
                    val price = showDialog.value.price?:0
                    val ItemID = showDialog.value.itemID?:-1
                    //判斷購買所需金額是否足夠
                    if (points.intValue >= price*quantity && ItemID!=-1) {
                        showDialog.value = ShowDialog(true, "waiting");/*執行點數扣除API和獲取道具API*/
                        ApiUSR.shopping(
                            SessionManager(activity).getUserToken().toString(),
                            shoppingInformation = ShoppingImformations(
                                ItemID,
                                quantity
                            ),
                            onSuccess = {
                                showDialog.value = ShowDialog(true, "purchaseCompleted")
                            },
                            onError = {
                                showDialog.value = ShowDialog(true, "notWorking")
                            },
                            onInternetError = {
                                showDialog.value = ShowDialog(true, "notWorking")
                            }
                        )
                    } else {
                        showDialog.value = ShowDialog(true, "pointsNotEnough")
                    }
                },
                showDialog = showDialog.value.isShowDialog,
                title = "購買資訊",
                content = if(showDialog.value.price!=null){
                    val price = showDialog.value.price?:0
                    "是否花費"+price*quantity+"金幣購買" + quantity+"個"+showDialog.value.itemName + "?";
                }
                else{"price=null"},
                buttonYes = "確認",
                buttonNo = "取消",
                color = colorResource(id = R.color.btnSatKTVColor),
                backgroundColor = colorResource(id = R.color.bgSatKTV),
                quantity = quantity,
                onQuantityChange = { newQuantity ->
                    quantity = newQuantity // 更新狀態
                }
            )

        showDialog.value.isShowDialog && showDialog.value.case == "waiting" -> {
            waitingDialog(
                onDismiss = {
                    showDialog.value = ShowDialog(false, "")
                },
                showDialog = showDialog.value.isShowDialog,
                title = "購買中，請稍後",
                color = colorResource(id = R.color.btnSatKTVColor),
                backgroundColor = colorResource(id = R.color.bgSatKTV)
            )
            Log.d("Store", showDialog.value.case + showDialog.value.isShowDialog)
        }

        else -> {
        /*什麼都不做*/
        }
    }
}










