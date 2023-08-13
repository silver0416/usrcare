package com.tku.usrcare.view.ui.scale

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tku.usrcare.R
import com.tku.usrcare.api.ApiUSR
import com.tku.usrcare.model.Sheets
import com.tku.usrcare.view.ui.Loading
import com.tku.usrcare.view.ui.theme.UsrcareTheme


@Composable
fun ScaleList(navController: NavHostController) {
    val context = LocalContext.current
    val isOk = remember { mutableStateOf(false) }
    val isLoadingVisible = remember { mutableStateOf(true) }
    val scaleList = remember { mutableStateListOf<Sheets>() }

    Loading(isLoadingVisible.value)

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
            items(scaleList.size) { index ->
                ScaleBox(item = scaleList[index],navController = navController)
            }
        })
    }
}

@Composable
fun ScaleBox(item: Sheets,navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Button(onClick = { navController.navigate("Scale/${item.sheetId}") },
            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.btnMoodScaleColor))){
            Text(text = item.sheetTitle)
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