package com.tku.usrcare.view.ui.setting

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import com.tku.usrcare.api.ApiUSR
import com.tku.usrcare.model.MoodTime
import com.tku.usrcare.view.component.Loading
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun Cheater(activity : Activity ,showCheaterDialog: MutableState<Boolean>) {
    val timeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.TAIWAN)
    val moodTime = MoodTime(timeFormat.format(System.currentTimeMillis()))
    fun postMoodMultipleTimes(times: Int) {
        // 檢查是否還需要執行
        if (times <= 0) {
            return
        }
        ApiUSR.postMood(
            activity = activity,
            mood = "1",
            moodTime = moodTime,
            onSuccess = {
                // API呼叫成功
                val intent = Intent("com.tku.usrcare.view.ui.main.MainFragment")
                intent.putExtra("points", true)
                activity.sendBroadcast(intent)
                showCheaterDialog.value = false
                // 重新呼叫此函數以執行下一個API呼叫
                postMoodMultipleTimes(times - 1)
                activity.finish()
            },
            onError = {
                // API呼叫失敗
                showCheaterDialog.value = false
            }
        )
    }
    // 初始呼叫，指定要執行50次
    postMoodMultipleTimes(50)

    AlertDialog(
        onDismissRequest = {
        },
        title = {
        },
        text = {
            Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center) {
                Loading(true)
            }
        },
        confirmButton = {

        }
    )
}