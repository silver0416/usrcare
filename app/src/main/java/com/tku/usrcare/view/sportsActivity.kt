package com.tku.usrcare.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.tku.usrcare.R
import com.tku.usrcare.api.ApiUSR
import com.tku.usrcare.model.SportVideoUploadResponse
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.component.enterTransition
import com.tku.usrcare.view.component.exitTransition
import com.tku.usrcare.view.component.popEnterTransition
import com.tku.usrcare.view.component.popExitTransition
import com.tku.usrcare.view.ui.ktv.Store
import com.tku.usrcare.view.ui.petcompany.MainPage
import com.tku.usrcare.view.ui.sports.DownloadPage
import com.tku.usrcare.view.ui.sports.NavGraphs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


class SportsActivity : ComponentActivity() {
    private lateinit var sessionManager: SessionManager

    // 申請相機與其他相關權限的 launcher
    private val requestPermissionsLauncher  =registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        val allGranted = permissions.entries.all { it.value }
        if (allGranted) {
            // 所有權限都被授予，執行相應操作
        } else {// 否則，提示用戶打開權限
            val intent=Intent().apply {
                action=Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                data= Uri.fromParts("package", packageName, null)
            }
            startActivity(intent)
        }
    }
    private fun requestPermission() {
        val permissions= arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_MEDIA_VIDEO,
            //Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED,
        )
        val allPermissionsGranted = permissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
        if (allPermissionsGranted) {
            // 所有權限都已經授予，什麼都不要做
        } else {
            requestPermissionsLauncher.launch(permissions) // 否則申請權限
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(this)
        window.statusBarColor = ContextCompat.getColor(this, R.color.bgSports)
        setContent {
            Sports()
        }
        requestPermission()
    }
    // 啟動原生相機應用程序
    fun startNativeCamera() {
        val cameraIntent = Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE)
        cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1)
        cameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1)
        cameraIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 180)//value單位為秒
        videoLauncher.launch(cameraIntent)
    }

    // 處理攝影完成後的結果
    private val videoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.d("001","進入videoLauncher: ${result.resultCode}")
            if (result.resultCode == RESULT_OK) {
                Log.d("001","resultCode == RESULT_OK確認")
                val data: Intent? = result.data
                val videoUri: Uri? = data?.data
                videoUri?.let { uri ->
                    Log.d("001",uri.toString())
                    lifecycleScope.launch {
                        val response = uploadMp4File(uri, { response ->
                            // 處理上傳成功的響應
                            Log.d("Upload", "Success: ${response}")
                        }, { errorMessage ->
                            // 處理上傳失敗的情況
                            Log.e("Upload", "Failed to upload video: $errorMessage")
                        })
                        response?.let {
                            // 處理上傳成功的響應
                        } ?: run {
                            // 處理上傳失敗的情況
                        }
                    }
                }
            }
        }

    private suspend fun uploadMp4File(
        videoUri: Uri,
        onSuccess: (SportVideoUploadResponse) -> Unit,
        onFail: (errorMessage: String) -> Unit
    ){
    withContext(Dispatchers.IO) {
        try {
            //Log.d("001","進入uploadMp4File")
            val file = FileUtils.getFileFromUri(this@SportsActivity, videoUri)
            ApiUSR.uploadVideo(
                file, { response ->
                    onSuccess(response)
                    //Log.d("001","response成功")
                    file.delete() // 上傳成功後刪除暫存文件
                }, { errorMessage ->
                    onFail(errorMessage)
                    //Log.d("001","response失敗")
                    file.delete() // 上傳失敗後刪除暫存文件
                })
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                onFail(e.message ?: "Unknown error")
                }
            }
        }
    }
    object FileUtils {
        fun getFileFromUri(context: Context, uri: Uri): File {
            //將uri轉換為文件路徑
            val inputStream = context.contentResolver.openInputStream(uri)
            //將影片複製到暫存文件中，並取名為temp_video.mp4
            val tempFile = File(context.cacheDir, "temp_video.mp4")
            inputStream?.use { input ->
                tempFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            return tempFile
        }
    }
    fun pickVideoFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        intent.type = "video/mp4"
        videoLauncher.launch(intent)
    }

    override fun onResume() {
        super.onResume()
        window.statusBarColor = ContextCompat.getColor(this, R.color.bgSports)
    }

    @Composable
    fun Sports() {
        MaterialTheme {
            Surface(color = MaterialTheme.colorScheme.surface) {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}







