package com.tku.usrcare.view.ui.scale

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tku.usrcare.R
import com.tku.usrcare.api.ApiUSR
import com.tku.usrcare.model.Sheets
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.findActivity
import com.tku.usrcare.view.ui.Loading
import com.tku.usrcare.view.ui.theme.UsrcareTheme

@Composable
fun TitleBox() {
    val context = LocalContext.current
    val activity = context.findActivity()
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        Row(modifier = Modifier.padding(top =50.dp, start = 20.dp, end = 20.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            androidx.compose.material3.Button(
                onClick = {
                    activity?.finish()
                },
                modifier = Modifier
                    .size(43.dp)
                    .clip(CircleShape),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(Color.White),
                contentPadding = PaddingValues(1.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(20.dp),
                    tint = colorResource(id = R.color.black)
                )
            }
            Spacer(modifier = Modifier.width(15.dp))
            Card(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .shadow(15.dp, RoundedCornerShape(16.dp))
                    .border(width = 3.dp, color = colorResource(id = R.color.btnMoodScaleColor), shape = RoundedCornerShape(15.dp))
                ,
                colors = CardDefaults.cardColors(colorResource(id = R.color.white))
            ) {
                Row(
                    modifier = Modifier
                        .width(240.dp)
                        .padding(10.dp, 10.dp, 10.dp, 10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(Modifier.weight(1f))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_moodscale),
                        contentDescription = null,
                        modifier = Modifier
                            .size(62.dp),
                        tint = colorResource(id = R.color.btnMoodScaleColor)
                    )
                    Spacer(modifier = Modifier.width(7.dp))
                    Text(
                        text = stringResource(R.string.mood_scale),
                        fontSize = 28.sp,
                        color = colorResource(id = R.color.black),
                    )
                    Spacer(Modifier.weight(1f))
                }
            }
        }

    }
}


@Composable
fun ScaleList(navController: NavHostController) {
    val context = LocalContext.current
    val isOk = remember { mutableStateOf(false) }
    val isLoadingVisible = remember { mutableStateOf(true) }
    val scaleList = remember { mutableStateListOf<Sheets>() }



    Box(modifier = Modifier.size(80.dp), contentAlignment = Alignment.Center){
        Loading(isLoadingVisible.value)
    }

    // 使用LaunchedEffect確保API調用只執行一次
    LaunchedEffect(Unit) {
        ApiUSR.getScaleList(context as Activity, onSuccess = { response ->
            scaleList.clear() // 清除舊列表
            val sheets = response.sheets
            Log.d("ScaleList", "Sheets size: ${sheets.size}") // 打印sheets大小
            for (sheet in sheets) {
                scaleList.add(sheet)
            }
            Log.d("ScaleList", scaleList.toString()) // 打印整個列表
            isOk.value = true
        }, onError = {
            println(it)
            isOk.value = true
        })
    }
    if (isOk.value) {
        isLoadingVisible.value = false
        LazyColumn(content = {
            val colors = listOf(Color(0xFFFF7B43), Color(0xFFCE43FF), Color(0xFF438EFF), Color(0xFF00CB6A))
            items(scaleList.size) { index ->
                // 使用模數運算選擇顏色
                val color = colors[index % colors.size]
                ScaleBox(
                    item = scaleList[index],
                    borderColor = color,
                    navController = navController
                )
            }
        },
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 150.dp, start = 40.dp, end = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
            )
    }
}


@Composable
fun ScaleBox(item: Sheets,borderColor: Color, navController: NavHostController) {

    val context = LocalContext.current

    val modifier = Modifier
        .padding(start = 0.dp)
        .border(width = 3.dp, color = borderColor, shape = RoundedCornerShape(size = 18.dp))
        .width(330.dp)
        .height(119.dp)

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(top = 40.dp), contentAlignment = Alignment.Center, propagateMinConstraints = true) {
        Button(
            onClick = {
                SessionManager(context).saveNowMainColor(String.format("#%06X", (borderColor.toArgb() and 0xFFFFFF)))
                navController.navigate("Scale/${item.sheetId}") },
            modifier = modifier,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.white))
        ) {
            Text(
                text = item.sheetTitle,
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight(700),
                    color = Color(0xFF2F3032),
                )
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ScaleListPreview() {
    UsrcareTheme {
        ScaleList(navController = rememberNavController())
    }
}