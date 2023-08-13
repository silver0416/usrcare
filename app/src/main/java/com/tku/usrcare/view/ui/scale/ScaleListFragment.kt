package com.tku.usrcare.view.ui.scale

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.tku.usrcare.api.ApiUSR
import com.tku.usrcare.model.ScaleListResponse
import com.tku.usrcare.model.Sheets
import com.tku.usrcare.view.ui.Loading
import com.tku.usrcare.view.ui.theme.UsrcareTheme


@Composable
fun ScaleList() {
    val context = LocalContext.current
    val scaleList = ArrayList<Sheets>()
    val isOk = remember { mutableStateOf(false) }
    val isLoadingVisible = remember { mutableStateOf(true) }
    Loading(isLoadingVisible.value)
    ApiUSR.getScaleList(context as Activity, onSuccess = {
        it.sheets.forEach { sheet ->
            scaleList.add(sheet)
        }
        isOk.value = true
    }, onError = {
        println(it)
        isOk.value = true
    })
    if (isOk.value){
        isLoadingVisible.value = false
        LazyColumn(content = {
            items(100) {
                ScaleBox(item = it)
            }
        })
    }
}

@Composable
fun ScaleBox(item: Int) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "Hello World #${item}")
    }
}

@Preview(showBackground = true)
@Composable
fun ScaleListPreview() {
    UsrcareTheme {
        ScaleList()
    }
}