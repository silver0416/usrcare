package com.tku.usrcare.view.ui.setting

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.tku.usrcare.R
import com.tku.usrcare.view.component.AutoSizedText
import com.tku.usrcare.view.component.findActivity

@SuppressLint("UnspecifiedRegisterReceiverFlag")
@Composable
fun UpdateCheckerDialog(
    showUpdateCheckerDialog: MutableState<Boolean>,
    skipNoUpdateDialog: Boolean = false
) {

    // 取得目前版本
    val currentVersion = LocalContext.current.packageManager.getPackageInfo(
        LocalContext.current.packageName,
        0
    ).versionName

    // 於google play最新版本
    var latestVersion by remember { mutableStateOf("") }

    // 是否需要更新
    val updateNeeded = remember { mutableStateOf(false) }

    // 用於存儲下載進度
    val downloadProgress = remember { mutableFloatStateOf(0f) }

    var showDownloadProcess by remember { mutableStateOf(false) }

    var updateDownloaded by remember { mutableStateOf(false) }

    val isStartUpdate = remember { mutableStateOf(false) }


    val context = LocalContext.current
    val appUpdateManager = AppUpdateManagerFactory.create(context)

    val title = if (showDownloadProcess) {
        "正在下載更新"
    } else {
        "是否更新"
    }

    // 監聽更新狀態和進度
    val listener = { installState: InstallState ->
        if (installState.installStatus() == InstallStatus.DOWNLOADING) {
            val progress =
                installState.bytesDownloaded() * 100f / installState.totalBytesToDownload()
            downloadProgress.floatValue = progress
            if (!progress.isNaN()){
                if (progress >= 1) {
                    isStartUpdate.value = true
                }
            }
        } else if (installState.installStatus() == InstallStatus.DOWNLOADED) {
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

    val appUpdateInfoTask = appUpdateManager.appUpdateInfo
    // 檢查是否有更新
    try {
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                // 有更新
                updateNeeded.value = true
                latestVersion = appUpdateInfo.availableVersionCode().toString()
            } else {
                // 無更新
                updateNeeded.value = false
                latestVersion = currentVersion
            }
        }
    } catch (
        e: Exception
    ) {
        e.printStackTrace()
        Log.d("UpdateCheckerDialog", "檢查更新失敗, ${e.message}")
    }

    // 顯示AlertDialog
    if (updateNeeded.value) {

        AlertDialog(
            onDismissRequest = { },
            title = {
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_update),
                        contentDescription = "Download",
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(35.dp),
                    )
                    Text(text = title, fontSize = 32.sp)
                }
            },
            text = {
                Column {
                    if (!showDownloadProcess && !updateDownloaded) {
                        Text(
                            "偵測到新版本，是否要現在更新？\n本機版本:(${currentVersion}) -> 最新版本:(${latestVersion})",
                            style = androidx.compose.ui.text.TextStyle(fontSize = 20.sp)
                        )
                    } else if (showDownloadProcess && !updateDownloaded) {
                        Text("請稍後...", fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            if (isStartUpdate.value) {
                                LinearProgressIndicator(
                                    progress = { downloadProgress.floatValue / 100f },
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text = "${downloadProgress.floatValue.toInt()}%", fontSize = 18.sp)
                            } else {
                                LinearProgressIndicator()
                            }
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
                        }, 3000)
                    }

                }
            },
            confirmButton = {
                if (!showDownloadProcess && !updateDownloaded) {
                    Button(
                        onClick = {
                            // 開始更新
                            context.findActivity()?.let { activity ->
                                val appUpdateOptions =
                                    AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build()
                                appUpdateManager.startUpdateFlowForResult(
                                    appUpdateInfoTask.result,
                                    activity,
                                    appUpdateOptions,
                                    1
                                )
                            }
                            showDownloadProcess = true
                        },
                        modifier = Modifier.size(100.dp, 50.dp)
                    ) {
                        AutoSizedText(text = "更新", size = 24, color = Color.White)
                    }
                }
            },
            dismissButton = {
                if (!showDownloadProcess && !updateDownloaded) {
                    TextButton(
                        onClick = {
                            // 不更新
                            showUpdateCheckerDialog.value = false
                        },
                        modifier = Modifier.size(100.dp, 50.dp)
                    ) {
                        Text("稍後", fontSize = 21.sp)
                    }
                }
            }
        )
    } else {
        if (!skipNoUpdateDialog) {
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
}