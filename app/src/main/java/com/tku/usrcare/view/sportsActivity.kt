package com.tku.usrcare.view

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import com.ramcosta.composedestinations.DestinationsNavHost
import com.tku.usrcare.R
import com.tku.usrcare.view.ui.sports.NavGraphs


class SportsActivity : ComponentActivity() {
    // 申請相機權限的 launcher
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // 如果權限被授予，什麼都不要做
            } else {
                // 如果權限被拒絕
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.bgSports)
        setContent {
            Sports()
        }
        // 檢查並申請相機權限
        if (androidx.core.content.ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            // 如果已經有權限，什麼都不要做
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA) // 否則申請權限
        }
    }
    // 啟動原生相機應用程序
    fun startNativeCamera() {
        val cameraIntent = Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE)
        cameraLauncher.launch(cameraIntent)
    }

    // 處理攝影完成後的結果
    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // 在這裡處理攝影完成後的操作
            }
        }
    override fun onResume() {
        super.onResume()
        window.statusBarColor = ContextCompat.getColor(this, R.color.bgSports)
    }
}


@Composable
fun Sports() {
    MaterialTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            DestinationsNavHost(navGraph = NavGraphs.root)
        }
    }
}



