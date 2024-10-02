package com.tku.usrcare.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.navigate
import com.tku.usrcare.R
import com.tku.usrcare.api.ApiUSR
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.ui.sports.MainPage
import com.tku.usrcare.view.ui.sports.NavGraphs
import com.tku.usrcare.view.ui.sports.destinations.DownloadPageDestination
import com.tku.usrcare.view.ui.sports.destinations.MainPageDestination
import com.tku.usrcare.viewmodel.SportsViewModel
import com.unity3d.player.a.a
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File

class SportsActivity : ComponentActivity() {
    private val sportsViewModel: SportsViewModel by viewModels()
    private lateinit var sessionManager: SessionManager
    private val _waiting = MutableStateFlow(false)
    val waiting: StateFlow<Boolean> get() = _waiting
    private val _missionComplete = MutableStateFlow(false)
    val missionComplete: StateFlow<Boolean> get() = _missionComplete
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

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            handleIntent(it)
        }
    }

    private fun handleIntent(intent: Intent) {
        val action = intent.getStringExtra("action")
        val url = intent.getStringExtra("url")
        sportsViewModel.action = action?:""
        sportsViewModel.url = url?:""
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
    var action: String? by mutableStateOf(null)
    var url: String? by mutableStateOf(null)
    override fun onCreate(savedInstanceState: Bundle?) {
        //action = intent?.getStringExtra("action")
        //url = intent?.getStringExtra("url")
        super.onCreate(savedInstanceState)
        handleIntent(intent)
        sessionManager = SessionManager(this)
        window.statusBarColor = ContextCompat.getColor(this, R.color.bgSports)
        setContent {
            Sports(sportsViewModel = sportsViewModel)
            //Log.d("fcm1", action?:"")
            //Log.d("fcm1", url?:"")
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
            //Log.d("sportsActivity","進入videoLauncher: ${result.resultCode}")
            if (result.resultCode == RESULT_OK) {
                //Log.d("sportsActivity","resultCode == RESULT_OK確認")
                val data: Intent? = result.data
                val videoUri: Uri? = data?.data
                videoUri?.let { uri ->
                    //Log.d("sportsActivity",uri.toString())
                    lifecycleScope.launch {
                        _waiting.value = true
                        val response = uploadMp4File(sessionManager,uri, { response ->
                            //這邊有問題，無論結果如何都會產生failed相關的log
                            // 處理上傳成功的響應
                            Log.d("sportsActivity", "Success: $response")
                        }, { errorMessage ->
                            // 處理上傳失敗的情況
                            Log.e("sportsActivity", "Failed to upload video: $errorMessage")
                        })
                        response?.let {
                            _waiting.value=false
                            _missionComplete.value=true
                            delay(3500)
                            _missionComplete.value=false
                            // 處理上傳成功的響應
                        }
                    }
                }
            }
        }

    private suspend fun uploadMp4File(
        sessionManager: SessionManager,
        videoUri: Uri,
        onSuccess: (successMessage: String) -> Unit,
        onFail: (errorMessage: String) -> Unit
    ){
    withContext(Dispatchers.IO) {
        try {
            //Log.d("sportsActivity","進入uploadMp4File")
            val file:File=try{
                // 優化處理，將 URI 轉換為文件
                FileUtils.getFileFromUri(this@SportsActivity, videoUri)
            } catch (e: Exception) {
                Log.d("sportsActivity","uploadVideoFile:${e}")
                return@withContext
            }
            // 進行上傳
            ApiUSR.uploadVideo(
                sessionManager,file, {
                    Log.d("sportsActivity","uploadVideo:OK")
                    file.delete() // 上傳成功後刪除暫存文件
                }, {
                    file.delete() // 上傳失敗後刪除暫存文件
                    Log.d("sportsActivity","uploadVideo:不OK")
                },)
        } catch (e: Exception) {
            Log.d("sportsActivity","uploadVideoCatchFail:${e}")
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

    fun enableFullScreen() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    fun disableFullScreen() {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(window, window.decorView).show(WindowInsetsCompat.Type.systemBars())
    }


    @Composable
    fun Sports(sportsViewModel:SportsViewModel) {
        val navController = rememberNavController()
        val action = sportsViewModel.action
        val url = sportsViewModel.url
        Log.d("001",action.toString()+" "+url.toString())
        MaterialTheme {
            Surface(color = MaterialTheme.colorScheme.surface) {
                DestinationsNavHost(navGraph = NavGraphs.root,navController = navController)
                    LaunchedEffect(action, url) {
                        if(action=="video" && url!=null) {
                            navController.navigate(DownloadPageDestination(url = url))
                        }
                    }
            }
        }
    }
}







