package com.tku.usrcare.view.ui.sports

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.component.TitleBox
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
    LaunchedEffect(Unit){
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
            VideoLists(navigator = navigator)
        }
    }
}

@Composable
fun VideoLists(navigator: DestinationsNavigator) {
    LazyColumn(content = {
        items(sportsViewModel.vdlist.size) { index ->
            VideoItem(
                title = sportsViewModel.vdlist[index].title,
                imgUrl = sportsViewModel.getYtThumbnailUrl(sportsViewModel.vdlist[index].url),
                vdUrl = sportsViewModel.vdlist[index].url,
                navigator = navigator
            )
        }
    })
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun VideoItem(title: String, imgUrl: String, vdUrl: String,navigator: DestinationsNavigator) {

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
