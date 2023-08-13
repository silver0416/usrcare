package com.tku.usrcare.view.ui.scale

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.tku.usrcare.api.ApiUSR
import com.tku.usrcare.model.Question
import com.tku.usrcare.view.ui.Loading

@Composable
fun Scale(id: Int, navController: NavController) {
    val isLoadingVisible = remember { mutableStateOf(true) }
    val isOk = remember { mutableStateOf(false) }
    val scaleTitle = remember { mutableStateOf("") }
    val specialOption = remember { mutableStateListOf<String>() }
    val questions = remember { mutableStateListOf<Question>() }
    val context = LocalContext.current
    val nowQuestion = remember { mutableIntStateOf(0) }

    Loading(isLoadingVisible.value)

    LaunchedEffect(Unit) {
        ApiUSR.getScale(context as Activity, id, onSuccess = { response ->
            scaleTitle.value = response.sheetTitle

            response.specialOption.forEach { (key, value) ->
                specialOption.add("$key: $value")
            }

            questions.addAll(response.questions) // 將問題列表添加到可記憶的狀態中

            isOk.value = true
        }, onError = {
            println(it)
            isOk.value = true
        })
    }
    if (isOk.value) {
        isLoadingVisible.value = false
        Column {
            Text(text = questions[nowQuestion.intValue].ques)
            Row {
                questions[nowQuestion.intValue].ans.forEach { ans ->
                    Button(onClick = {
                        //TODO: 記錄答案
                    }) {
                        Text(text = ans.toString())
                    }
                    Spacer(modifier = androidx.compose.ui.Modifier.weight(1f))
                }
            }
            // 確認是否為最後一題
            if (nowQuestion.intValue == questions.size - 1) {
                Button(onClick = {
                    //TODO: 送出問卷
                }) {
                    Text(text = "送出")
                }
            } else {
                Button(onClick = {
                    nowQuestion.intValue += 1
                }) {
                    Text(text = "下一題")
                }
            }
        }
    }
}

