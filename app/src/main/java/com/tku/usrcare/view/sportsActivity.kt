package com.tku.usrcare.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.FileUtils
import android.provider.MediaStore
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.firebase.appdistribution.gradle.models.UploadResponse
import com.ramcosta.composedestinations.DestinationsNavHost
import com.tku.usrcare.R
import com.tku.usrcare.api.ApiService
import com.tku.usrcare.model.SportVideoUploadResponse
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.ui.sports.NavGraphs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.Response
import java.io.File


class SportsActivity : ComponentActivity() {
    private lateinit var sessionManager: SessionManager

    // 申請相機權限的 launcher
    private val requestPermissionLauncher =registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            // 如果權限被授予，什麼都不要做
        } else {// 否則，提示用戶打開權限
            val intent=Intent().apply {
                action=Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                data= Uri.fromParts("package", packageName, null)
            }
            startActivity(intent)
        }
    }
    private fun requestPermission() {
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(this)
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
            requestPermission()// 否則申請權限
        }
    }
    // 啟動原生相機應用程序
    fun startNativeCamera() {
        val cameraIntent = Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE)
        cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1)
        cameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0)
        cameraIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 180)//value單位為秒
        cameraLauncher.launch(cameraIntent)
    }

    // 處理攝影完成後的結果
    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                val videoUri: Uri? = data?.data
                videoUri?.let { uri ->
                    lifecycleScope.launch {
                        val token = sessionManager.getUserToken() ?: return@launch
                        val response = uploadMp4File(this@SportsActivity, uri, token, "description")
                        response?.let {
                            // 處理上傳成功的響應
                        } ?: run {
                            // 處理上傳失敗的情況
                        }
                    }
                }
            }
        }

    private suspend fun uploadMp4File(context: Context, videoUri: Uri, token: String, description: String): SportVideoUploadResponse? {
        return withContext(Dispatchers.IO) {
            val file = FileUtils.getFileFromUri(context, videoUri)
            val requestFile = RequestBody.create("video/mp4".toMediaTypeOrNull(), file)
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
            //val descriptionBody = RequestBody.create("text/plain".toMediaTypeOrNull(), description)
            
            try {
                val apiService = ApiService.getApi()
                val call = apiService?.uploadVideo(token, body)
                val response = call?.execute()
                if (response?.isSuccessful == true) {
                    response.body()
                } else {
                    // 处理错误
                    null
                }
            } catch (e: Exception) {
                // 處理錯誤
                null
            }
        }
    }
    object FileUtils {
        fun getFileFromUri(context: Context, uri: Uri): File {
            val inputStream = context.contentResolver.openInputStream(uri)
            val file = File(context.cacheDir, "upload_temp_file.mp4")
            inputStream.use { input ->
                file.outputStream().use { output ->
                    input?.copyTo(output)
                }
            }
            return file
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



