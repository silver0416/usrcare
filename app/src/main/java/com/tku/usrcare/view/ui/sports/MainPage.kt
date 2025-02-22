package com.tku.usrcare.view.ui.sports

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.ActivityInfo
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.DestinationStyle.Dialog.Default.properties
import com.tku.usrcare.R
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.MainActivity
import com.tku.usrcare.view.SportsActivity
import com.tku.usrcare.view.component.AutoSizedText
import com.tku.usrcare.view.component.FixedSizeText
import com.tku.usrcare.view.component.Loading
import com.tku.usrcare.view.component.TitleBox
import com.tku.usrcare.view.component.findActivity
import com.tku.usrcare.view.component.missionCompleteDialog
import com.tku.usrcare.view.component.normalAlertDialog
import com.tku.usrcare.view.ui.clock.TypeButton
import com.tku.usrcare.view.ui.sports.destinations.DownloadPageDestination
import com.tku.usrcare.view.ui.sports.destinations.WebViewContainerDestination
import com.tku.usrcare.viewmodel.SportsViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

private lateinit var sportsViewModel: SportsViewModel

@Destination(
    route = "Sports",
    start = true
)
@Composable
fun MainPage(navigator: DestinationsNavigator) {
    fun Context.findActivity(): Activity? {
        var context = this
        while (context is ContextWrapper) {
            if (context is Activity) {
                return context
            }
            context = context.baseContext
        }
        return null
    }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        context.findActivity()?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    val viewModelFactory = com.tku.usrcare.viewmodel.ViewModelFactory(
        sessionManager = SessionManager(context)
    )

    sportsViewModel = viewModel(
        viewModelStoreOwner = context as ViewModelStoreOwner, factory = viewModelFactory
    )
    val activity = context as? SportsActivity
    val isWaiting: StateFlow<Boolean> = activity?.waiting ?: MutableStateFlow(false)
    val waiting by isWaiting.collectAsState()
    @Composable
    fun waitingDialog(
        onDismiss: () -> Unit,
        //showDialog: Boolean,
        title: String,
        color: Color,
        backgroundColor: Color,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
        ) {
            AlertDialog(
                modifier = Modifier
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(5.dp, color, shape = RoundedCornerShape(16.dp)),
                backgroundColor = backgroundColor,
                onDismissRequest = { onDismiss() },
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        FixedSizeText(text = title, size = 80.dp, fontWeight = FontWeight.Bold)
                    }
                },
                text = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Loading(isVisible = true)
                    }
                },
                buttons = {
                },
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                )
            )
        }
    }

    //運動攝影的按鈕
    @Composable
    fun CameraButton(
        name: String,
        route: String,
        iconName: Int,
        activity: SportsActivity,
    ) {
        val isWaiting: StateFlow<Boolean> = activity?.waiting ?: MutableStateFlow(false)
        val waiting by isWaiting.collectAsState()
        val isMissionComplete: StateFlow<Boolean> =
            activity?.missionComplete ?: MutableStateFlow(false)
        val missionComplete by isMissionComplete.collectAsState()
        var showUseInformation by remember { mutableStateOf(false) }
        val screenWidthDp = LocalConfiguration.current.screenWidthDp
        val widthFraction = 0.8//物件寬度佔螢幕寬度的比例
        val boxWidth = (screenWidthDp * widthFraction).dp
        androidx.compose.material3.Button(
            onClick = {
                showUseInformation = true
            },
            modifier = Modifier
                .width(boxWidth)
                .wrapContentHeight()
                .padding(4.dp)
                .clip(MaterialTheme.shapes.medium)
                .border(
                    width = 4.dp,
                    color = colorResource(id = R.color.btnAiVitalityDetection),
                    shape = MaterialTheme.shapes.medium
                ), colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = colorResource(id = R.color.black),
            ),
            shape = MaterialTheme.shapes.medium,
            contentPadding = PaddingValues(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = iconName),
                    contentDescription = route,
                    modifier = Modifier
                        .padding(end = 8.dp, top = 4.dp, bottom = 4.dp)
                        .size(50.dp),
                    tint = Color.Unspecified
                )
                AutoSizedText(
                    text = name,
                    size = 30,
                )
            }
        }
        normalAlertDialog(
            title = "運動攝影使用說明",
            content = stringResource(id = R.string.sportCameraDialog),
            buttonText = "我知道了",
            showDialog = showUseInformation,
            onDismiss = { showUseInformation = false },
            onConfirm = { activity.startNativeCamera() },
            color = colorResource(id = R.color.btnAiVitalityDetection),
            backgroundColor = colorResource(id = R.color.bgSports)
        )
        missionCompleteDialog(
            onDismiss = { },
            onConfirm = { },
            showDialog = missionComplete,
            content = "多多運動 有益健康!",
            icon=painterResource(id = R.drawable.good_job),
            content2 = "影片分析完成後會再通知您，請耐心等候",
            color = colorResource(id = R.color.btnAiVitalityDetection),
            backgroundColor = colorResource(id = R.color.bgSports)
        )
    }

    //上傳功能按鈕
    @Composable
    fun UploadButton(name: String, route: String, iconName: Int, onClick: () -> Unit = {}) {
        val screenWidthDp = LocalConfiguration.current.screenWidthDp
        val widthFraction = 0.4//物件寬度佔螢幕寬度的比例
        val boxWidth = (screenWidthDp * widthFraction).dp
        androidx.compose.material3.Button(
            onClick = {
                onClick()
            },
            modifier = Modifier
                .width(boxWidth)
                .wrapContentHeight()
                .padding(4.dp)
                .clip(MaterialTheme.shapes.medium)
                .border(
                    width = 4.dp,
                    color = colorResource(id = R.color.btnAiVitalityDetection),
                    shape = MaterialTheme.shapes.medium
                ), colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = colorResource(id = R.color.black),
            ),
            shape = MaterialTheme.shapes.medium,
            contentPadding = PaddingValues(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = iconName),
                    contentDescription = route,
                    modifier = Modifier
                        .padding(end = 8.dp, top = 4.dp, bottom = 4.dp)
                        .size(50.dp),
                    tint = Color.Unspecified
                )
                AutoSizedText(
                    text = name,
                    size = 30,
                )
            }
        }
    }

    @Composable
    fun DownloadButton(name: String, route: String, iconName: Int, onClick: () -> Unit = {}) {
        val screenWidthDp = LocalConfiguration.current.screenWidthDp
        val widthFraction = 0.4//物件寬度佔螢幕寬度的比例
        val boxWidth = (screenWidthDp * widthFraction).dp
        androidx.compose.material3.Button(
            onClick = {
                onClick()
            },
            modifier = Modifier
                .width(boxWidth)
                .wrapContentHeight()
                .padding(4.dp)
                .clip(MaterialTheme.shapes.medium)
                .border(
                    width = 4.dp,
                    color = colorResource(id = R.color.btnAiVitalityDetection),
                    shape = MaterialTheme.shapes.medium
                ), colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = colorResource(id = R.color.black),
            ),
            shape = MaterialTheme.shapes.medium,
            contentPadding = PaddingValues(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = iconName),
                    contentDescription = route,
                    modifier = Modifier
                        .padding(end = 8.dp, top = 4.dp, bottom = 4.dp)
                        .size(50.dp),
                    tint = Color.Unspecified
                )
                AutoSizedText(
                    text = name,
                    size = 30,
                )
            }
        }
    }

    @Composable
    fun VideoItem(title: String, imgUrl: String, vdUrl: String, navigator: DestinationsNavigator) {

        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = androidx.compose.ui.text.TextStyle(fontSize = 25.sp))
            Spacer(modifier = Modifier.padding(8.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable {
                        val destination = WebViewContainerDestination(
                            videoId = sportsViewModel.getYtVideoId(vdUrl),
                        )
                        navigator.navigate(destination)
                    },
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Image(
                    painter = rememberImagePainter(imgUrl),
                    contentDescription = "YouTube Thumbnail",
                    modifier = Modifier.size(300.dp, 168.dp),
                    contentScale = ContentScale.Crop,
                )
                Image(
                    painter = painterResource(id = com.tku.usrcare.R.drawable.ic_play),
                    contentDescription = "Play Icon",
                    modifier = Modifier.size(80.dp),
                )
            }
        }
    }

    @Composable
    fun VideoLists(navigator: DestinationsNavigator, activity: SportsActivity) {
        Column(
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CameraButton(
                    name = "運動攝影",
                    route = "CameraButton",
                    iconName = R.drawable.ic_camera,
                    activity = activity,
                )

            }
            Row()
            {
                UploadButton(
                    name = "上傳影片",
                    route = "UploadButton",
                    iconName = R.drawable.ic_upload,
                    onClick = { activity.pickVideoFromGallery() })
                DownloadButton(
                    name = "觀看成果",
                    route = "DownloadButton",
                    iconName = R.drawable.ic_download,
                    onClick = { navigator.navigate(DownloadPageDestination(url = null)) })
            }

            LazyColumn(content =
            {
                items(sportsViewModel.vdlist.size) { index ->

                    VideoItem(
                        title = sportsViewModel.vdlist[index].title,
                        imgUrl = sportsViewModel.getYtThumbnailUrl(sportsViewModel.vdlist[index].url),
                        vdUrl = sportsViewModel.vdlist[index].url,
                        navigator = navigator
                    )
                }
            }
            )
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = com.tku.usrcare.R.color.bgSports))
    ) {
        TitleBox(
            color = colorResource(id = com.tku.usrcare.R.color.btnAiVitalityDetection),
            title = stringResource(id = com.tku.usrcare.R.string.AI_vitality_detection),
            icon = painterResource(id = com.tku.usrcare.R.drawable.ic_aivitalitydetection)
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            VideoLists(navigator = navigator, activity = context.findActivity() as SportsActivity)
        }
    }
    if (waiting)
    {
        waitingDialog(
            title = "上傳中，請稍後",
            onDismiss = { /*利用waiting控制*/ },
            color = colorResource(id = R.color.btnAiVitalityDetection),
            backgroundColor = colorResource(id = R.color.bgSports)
        )
    }

}

















