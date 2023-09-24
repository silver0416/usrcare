package com.tku.usrcare.view.ui.setting

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.tku.usrcare.view.findActivity

@Composable
fun UpdateCheckerDialog(showUpdateCheckerDialog: MutableState<Boolean>) {
    // 取得目前版本
    val currentVersion = LocalContext.current.packageManager.getPackageInfo(
        LocalContext.current.packageName,
        0
    ).versionName

    // 於google play最新版本
    var latestVersion by remember { mutableStateOf("") }
    // 用來控制對話框是否顯示
    var updateNeeded by remember { mutableStateOf(false) }
    // 用於存儲下載進度
    var downloadProgress by remember { mutableFloatStateOf(0f) }

    var showDownloadProcess by remember { mutableStateOf(false) }

    var updateDownloaded by remember { mutableStateOf(false) }


    val context = LocalContext.current
    val appUpdateManager = AppUpdateManagerFactory.create(context)


    // 監聽更新狀態和進度
    val listener = { installState: InstallState ->
        if (installState.installStatus() == InstallStatus.DOWNLOADING) {
            val progress =
                installState.bytesDownloaded() * 100f / installState.totalBytesToDownload()
            downloadProgress = progress
        }
        else if (installState.installStatus() == InstallStatus.DOWNLOADED) {
            updateDownloaded = true
        }
    }

    // 監聽更新狀態和進度
    DisposableEffect(Unit) {
        appUpdateManager.registerListener(listener)
        onDispose {
            appUpdateManager.unregisterListener(listener)
        }
    }

    // 檢查是否有更新
    val appUpdateInfoTask = appUpdateManager.appUpdateInfo
    latestVersion = appUpdateInfoTask.result.availableVersionCode().toString()
    appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
        if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
        ) {
            updateNeeded = true  // 如果有可用的更新，則顯示對話框
        }
    }

    // 顯示AlertDialog
    if (updateNeeded) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text(text = "更新可用") },
            text = {
                Column {
                    if (!showDownloadProcess && !updateDownloaded) {
                        Text("新版本可用，是否要現在更新？\n本機版本:(${currentVersion}) -> 最新版本:(${latestVersion})")
                    } else if (showDownloadProcess && !updateDownloaded) {
                        Text("正在下載更新，請稍後...")
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                            LinearProgressIndicator(progress = downloadProgress / 100f)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = "${downloadProgress.toInt()}%")
                        }
                    } else {
                        // 3秒後重新啟動應用程序以完成更新
                        // 倒數計時
                        var countDown by remember { mutableIntStateOf(3) }
                        val handler = Handler(Looper.getMainLooper())
                        val runnable = object : Runnable {
                            override fun run() {
                                countDown--
                                if (countDown > 0) {
                                    handler.postDelayed(this, 1000)
                                }
                            }
                        }
                        handler.postDelayed(runnable, 1000)
                        Text("更新已完成，應用程式將在${countDown}秒後重新啟動。")
                        handler.postDelayed({
                            appUpdateManager.completeUpdate()
                            showUpdateCheckerDialog.value = false
                            context.findActivity()?.finish()
                        }, 3000)
                    }

                }
            },
            confirmButton = {
                if (!showDownloadProcess && !updateDownloaded) {
                    TextButton(
                        onClick = {
                            // 開始更新
                            context.findActivity()?.let {
                                appUpdateManager.startUpdateFlowForResult(
                                    appUpdateInfoTask.result,
                                    AppUpdateType.FLEXIBLE,
                                    it,
                                    1
                                )
                            }
                            showDownloadProcess = true
                        }
                    ) {
                        Text("確定")
                    }
                }
            },
            dismissButton = {
                if (!showDownloadProcess && !updateDownloaded) {
                    TextButton(
                        onClick = {
                            // 不更新
                            showUpdateCheckerDialog.value = false
                        }
                    ) {
                        Text("稍後")
                    }
                }
            }
        )
    } else {
        AlertDialog(
            onDismissRequest = { showUpdateCheckerDialog.value = false },
            title = { Text(text = "已是最新版本(${latestVersion})") },
            text = { Text("目前已是最新版本，無需更新。\n本機版本:(${currentVersion})") },
            confirmButton = {
                TextButton(onClick = { showUpdateCheckerDialog.value = false }) {
                    Text("確定")
                }
            }
        )
    }
}