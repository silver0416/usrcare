package com.tku.usrcare.view.ui.scale

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tku.usrcare.R
import com.tku.usrcare.api.ApiUSR
import com.tku.usrcare.model.Sheets
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.component.Loading
import com.tku.usrcare.view.ui.theme.UsrcareTheme


@Composable
fun ScaleList(navController: NavHostController) {
    val context = LocalContext.current
    val isOk = remember { mutableStateOf(false) }
    val isLoadingVisible = remember { mutableStateOf(true) }
    val scaleList = remember { mutableStateListOf<Sheets>() }


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
        })
    }
    if (isOk.value) {
        isLoadingVisible.value = false
        LazyColumn(
            content = {
                val colors = listOf(
                    Color(0xFFFF7B43),
                    Color(0xFFCE43FF),
                    Color(0xFF438EFF),
                    Color(0xFF00CB6A)
                )
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
                .padding(start = 40.dp, end = 40.dp)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        )
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Loading(isLoadingVisible.value)
        }
    }
}


@Composable
fun ScaleBox(item: Sheets, borderColor: Color, navController: NavHostController) {

    val context = LocalContext.current

    val modifier = Modifier
        .padding(start = 0.dp)
        .border(width = 3.dp, color = borderColor, shape = RoundedCornerShape(size = 16.dp))
        .width(330.dp)
        .height(119.dp)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp),
        contentAlignment = Alignment.Center,
        propagateMinConstraints = true
    ) {
        Button(
            onClick = {
                SessionManager(context).saveNowMainColor(
                    String.format(
                        "#%06X",
                        (borderColor.toArgb() and 0xFFFFFF)
                    )
                )
                navController.navigate("Scale/${item.sheetId}")
            },
            modifier = modifier,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.white))
        ) {
            Text(
                textAlign = TextAlign.Center,
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