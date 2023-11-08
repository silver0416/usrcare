package com.tku.usrcare.view.ui.setting

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.Observer
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.component.Loading
import com.tku.usrcare.viewmodel.MainViewModel
import com.tku.usrcare.viewmodel.ViewModelFactory


private lateinit var mainViewModel: MainViewModel

@Composable
fun Cheater(activity: Activity) {
    val sessionManager = SessionManager(activity)
    val viewModelFactory = ViewModelFactory(sessionManager)
    mainViewModel = viewModelFactory.create(MainViewModel::class.java)
    fun postCheatMultipleTimes(initialTimes: Int) {
        var remainingTimes = initialTimes
        var postCompleteObserver: Observer<Boolean>? = null
        postCompleteObserver = Observer<Boolean> { isComplete ->
            if (isComplete) {
                mainViewModel.postComplete.value = false
                if (remainingTimes > 0) {
                    mainViewModel.postCheat()
                    remainingTimes -= 1
                    activity.finish()
                } else {
                    // 只在這裡移除觀察者
                    postCompleteObserver?.let { mainViewModel.postComplete.removeObserver(it) }
                }
                val intent = Intent("com.tku.usrcare.view.ui.main.MainFragment")
                intent.putExtra("points", true)
                activity.sendBroadcast(intent)
            }
        }

        mainViewModel.postComplete.observeForever(postCompleteObserver)
        // 初始呼叫
        mainViewModel.postCheat()
    }

    // 初始呼叫，指定要執行50次
    postCheatMultipleTimes(50)

    AlertDialog(onDismissRequest = {}, title = {}, text = {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Loading(true)
        }
    }, confirmButton = {

    })
}