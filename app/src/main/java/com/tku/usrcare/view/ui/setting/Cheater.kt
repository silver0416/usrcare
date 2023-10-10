package com.tku.usrcare.view.ui.setting

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.lifecycle.Observer
import com.tku.usrcare.model.MoodTime
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.component.Loading
import com.tku.usrcare.viewmodel.MainFragmentViewModel
import com.tku.usrcare.viewmodel.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Locale


private lateinit var mainFragmentViewModel: MainFragmentViewModel
@Composable
fun Cheater(activity: Activity, showCheaterDialog: MutableState<Boolean>) {
    val timeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.TAIWAN)
    val moodTime = MoodTime(timeFormat.format(System.currentTimeMillis()))
    val sessionManager = SessionManager(activity)
    val viewModelFactory = ViewModelFactory(sessionManager)
    val mainFragmentViewModel = viewModelFactory.create(MainFragmentViewModel::class.java)
    fun postMoodMultipleTimes(times: Int) {
        var times = times
        // 只添加一次觀察者
        mainFragmentViewModel.postComplete.observeForever { isComplete ->
            if (isComplete) {
                // 重置狀態
                mainFragmentViewModel.postComplete.value = false
                if (times > 0) {
                    // 繼續執行
                    mainFragmentViewModel.postMood(1, moodTime)
                    times -= 1
                    Log.d("Cheater", "postMoodMultipleTimes: $times")
                    activity.finish()
                } else {
                    // 完成後移除觀察者
                    mainFragmentViewModel.postComplete.removeObserver(Observer { })
                }
                val intent = Intent("com.tku.usrcare.view.ui.main.MainFragment")
                intent.putExtra("points", true)
                activity.sendBroadcast(intent)
            }
        }
        // 初始呼叫
        mainFragmentViewModel.postMood(1, moodTime)
    }
    // 初始呼叫，指定要執行50次
    postMoodMultipleTimes(50)

    AlertDialog(
        onDismissRequest = {
        },
        title = {
        },
        text = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Loading(true)
            }
        },
        confirmButton = {

        }
    )
}