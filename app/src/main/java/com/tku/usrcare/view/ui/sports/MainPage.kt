package com.tku.usrcare.view.ui.sports

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.ActivityInfo
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.tku.usrcare.R
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.Sports
import com.tku.usrcare.view.SportsActivity
import com.tku.usrcare.view.component.AutoSizedText
import com.tku.usrcare.view.component.FixedSizeText
import com.tku.usrcare.view.component.TitleBox
import com.tku.usrcare.view.ui.clock.TypeButton
import com.tku.usrcare.view.ui.sports.destinations.WebViewContainerDestination
import com.tku.usrcare.viewmodel.SportsViewModel

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
}


@Composable
fun VideoLists(navigator: DestinationsNavigator, activity: SportsActivity) {
    Column(
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        VideoButton(
            name = "運動攝影",
            route = "CameraButton",
            iconName = R.drawable.ic_camera,
            activity = activity
        )
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


@OptIn(ExperimentalCoilApi::class)
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
fun VideoButton(name: String, route: String, iconName: Int, activity: SportsActivity) {
    var showUseInformation by remember { mutableStateOf(false) }
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val widthFraction = 0.7//物件寬度佔螢幕寬度的比例
    val boxWidth = (screenWidthDp * widthFraction).dp
    androidx.compose.material3.Button(
        onClick = {
            showUseInformation = true
            //activity.startNativeCamera()
        },
        modifier = Modifier
            .width(boxWidth)
            .wrapContentHeight()
            .padding(4.dp)
            .clip(MaterialTheme.shapes.medium)
            .border(
                width = 4.dp,
                color = colorResource(id = R.color.btnClockColor),
                shape = MaterialTheme.shapes.medium
            ), colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = colorResource(id = R.color.black),
        ),
        shape = MaterialTheme.shapes.medium,
        contentPadding = PaddingValues(10.dp)
    ) {
        Row(
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
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
                //fontWeight = FontWeight.Bold
            )
        }
    }
    if(showUseInformation)
    {
        AlertDialog(
            modifier = Modifier.padding(16.dp),
            onDismissRequest = { showUseInformation = false },
            title = { FixedSizeText(text = "運動攝影使用說明",size = 80.dp,fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    FixedSizeText(
                        text = "    感謝您使用運動攝影功能，運動攝影功能需要取得相機的權限才可以使用，攝影時長最多為3分鐘，攝影完畢後我們會將影片上傳並進行分析，分析與上傳皆需要一段時間，我們會在計算出您的活力指數後提醒您。",
                        size = 70.dp
                    )
                }
            },
            confirmButton = {
                    Button(
                        onClick = { showUseInformation = false;activity.startNativeCamera() },
                        colors = androidx.compose.material.ButtonDefaults.buttonColors(
                            backgroundColor = colorResource(id = R.color.purple_500),
                            contentColor = Color.White
                        )
                    ) {
                        FixedSizeText("我知道了",size = 60.dp, color = Color.White)
                    }
            }, properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
        )
    }

}

