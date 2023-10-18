package com.tku.usrcare.view.ui.ktv

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tku.usrcare.R
import com.tku.usrcare.api.ApiUSR
import com.tku.usrcare.model.PointsDeduction
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.KtvActivity
import com.tku.usrcare.view.component.FixedSizeText
import com.tku.usrcare.view.component.Loading
import com.tku.usrcare.view.component.TitleBox
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun KtvMain() {
    val activity = LocalContext.current as KtvActivity
    val isShowSingingTicketDialog = remember {
        mutableStateOf(false)
    }
    val points = remember {
        mutableIntStateOf(0)
    }
    val goLoading = remember {
        mutableStateOf(false)
    }
    val goUpdatePoints = remember {
        mutableStateOf(false)
    }
    val ticketPrice = 100

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

    if (isShowSingingTicketDialog.value) {
        AlertDialog(
            onDismissRequest = { isShowSingingTicketDialog.value = false },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Ticket()
                }
            },
            text = {
                if (goLoading.value) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    )
                    { Loading(isVisible = true) }
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = stringResource(id = R.string.total),
                                fontSize = 25.sp,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Image(
                                painter = painterResource(id = R.drawable.ic_coin),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(12.dp)
                            )
                            Text(
                                text = "-" + stringResource(id = R.string.singing_ticket_price),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        if (points.intValue >= ticketPrice) {
                            Button(
                                onClick = {
                                    goLoading.value = true
                                    val timeFormat =
                                        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.TAIWAN)
                                    val pointsDeduction = PointsDeduction(
                                        time = timeFormat.format(System.currentTimeMillis()),
                                        deductionType = 1,
                                        deductionAmount = ticketPrice
                                    )
                                    ApiUSR.postPointDeduction(
                                        activity = activity,
                                        pointsDeduction = pointsDeduction,
                                        onSuccess = {
                                            goLoading.value = false
                                            goUpdatePoints.value = true
                                            isShowSingingTicketDialog.value = false
                                        },
                                        onError = {
                                            goLoading.value = false
                                            isShowSingingTicketDialog.value = false
                                        }
                                    )
                                },
                                modifier = Modifier
                                    .size(180.dp, 65.dp)
                                    .border(
                                        4.dp,
                                        colorResource(id = R.color.btnSatKTVColor),
                                        RoundedCornerShape(15.dp)
                                    ),
                                shape = RoundedCornerShape(15.dp),
                                colors = ButtonDefaults.buttonColors(
                                    colorResource(id = R.color.white),
                                ),
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_checkmark),
                                    contentDescription = null
                                )
                                Text(
                                    text = stringResource(id = R.string.confirm_exchange),
                                    fontSize = 23.sp,
                                    color = colorResource(id = R.color.black),
                                )
                            }
                            Text(
                                text = stringResource(id = R.string.today_use_only),
                                fontSize = 20.sp,
                                color = colorResource(id = R.color.red)
                            )
                        } else {
                            Text(
                                text = stringResource(id = R.string.not_enough_points),
                                fontSize = 35.sp,
                                color = colorResource(id = R.color.red)
                            )
                        }
                    }
                }
            },
            confirmButton = {
            },
        )
    }
    Box(
        Modifier
            .background(colorResource(id = R.color.bgSatKTV))
            .fillMaxSize()
    ) {
        TitleBox(
            color = colorResource(id = R.color.btnSatKTVColor),
            title = stringResource(id = R.string.saturday_KTV),
            icon = painterResource(id = R.drawable.ic_ktv)
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp, 180.dp, 30.dp, 40.dp),
            verticalArrangement = Arrangement.Top,
            content = {
                item {
                    Balance(points)
                }
                item {
                    Exchange(isShowSingingTicketDialog)
                }
            }
        )
    }
}

@Composable
fun Balance(points: MutableIntState) {
    val activity = LocalContext.current as KtvActivity
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(20.dp, 0.dp, 20.dp, 0.dp)
                .clip(RoundedCornerShape(32.dp))
                .border(5.dp, colorResource(id = R.color.btnSatKTVColor), RoundedCornerShape(32.dp))
                .background(colorResource(id = R.color.btnSatKTVColor).copy(alpha = 0.2f)),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(painter = painterResource(id = R.drawable.ic_coin), contentDescription = null)
                Text(text = points.intValue.toString(), fontSize = 50.sp)
            }
        }
    }
}

@Composable
fun Exchange(isShowSingingTicketDialog: MutableState<Boolean>) {
    Row(
        Modifier
            .fillMaxSize()
            .padding(0.dp, 50.dp), horizontalArrangement = Arrangement.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(id = R.string.singing_ticket_exchange),
                fontSize = 35.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.clickable {
                    isShowSingingTicketDialog.value = true
                }) {
                    Ticket()
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_coin),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(12.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.singing_ticket_price),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Row {

            }
        }
    }
}

@Composable
fun Ticket() {
    Box(
        modifier = Modifier
            .width(216.dp)
            .height(75.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(4.dp, colorResource(id = R.color.btnSatKTVColor), RoundedCornerShape(16.dp))
            .background(colorResource(id = R.color.white)),
    ) {
        Row(
            Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_ktv),
                contentDescription = null,
                modifier = Modifier
                    .padding(12.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(4.dp)
                    .background(colorResource(id = R.color.white))
            ) {
                Column {
                    // 這裡創建10個小線段，你可以根據需求來調整數量和大小
                    repeat(10) {
                        Surface(
                            modifier = Modifier
                                .width(4.dp)
                                .height(13.dp), // 每個小線段的高度
                            shape = CircleShape,
                            color = colorResource(id = R.color.gray).copy(alpha = 0.4f)
                        ) {}
                        // 線段之間的間隔
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                FixedSizeText(
                    text = stringResource(id = R.string.singing_ticket),
                    size = 92.dp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun KtvMainPreview() {
    KtvMain()
//    Ticket()
}