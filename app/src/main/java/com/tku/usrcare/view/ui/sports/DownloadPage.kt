package com.tku.usrcare.view.ui.sports

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.webkit.WebView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.tku.usrcare.R
import com.tku.usrcare.view.component.AutoSizedText
import com.tku.usrcare.view.component.TitleBox
import com.tku.usrcare.view.component.findActivity
import com.tku.usrcare.view.ui.sports.destinations.DownloadPageDestination
import com.tku.usrcare.view.ui.sports.destinations.MainPageDestination

@Destination(
    route = "DownloadPage",
    start = false
)
@Composable
fun DownloadPage(navigator: DestinationsNavigator){
    TopBar(navigator)
    videoList()
}

@Composable
fun TopBar(navigator: DestinationsNavigator){
    Column(
        Modifier
            .background(color = colorResource(id = R.color.bgSports))
            .fillMaxWidth()
    ) {
        TitleBoxForSports(
            color = colorResource(id = R.color.btnAiVitalityDetection),
            title = "觀看成果",
            icon = painterResource(id = R.drawable.ic_upload),
            navigator=navigator,
        )
    }
}

@Composable
fun TitleBoxForSports(
    color: Color,
    title: String,
    icon: Painter,
    navigator: DestinationsNavigator
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter,
    ) {
        Row(
            modifier = Modifier.padding(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    navigator.popBackStack()
                },
                modifier = Modifier
                    .size(43.dp)
                    .clip(CircleShape),
                colors = ButtonDefaults.buttonColors(Color.White),
                contentPadding = PaddingValues(1.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(20.dp),
                    tint = colorResource(id = R.color.black)
                )
            }
            Spacer(modifier = Modifier.width(15.dp))
            Card(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .shadow(15.dp, RoundedCornerShape(16.dp))
                    .border(
                        width = 3.dp,
                        color = color,
                        shape = RoundedCornerShape(15.dp)
                    ),
                colors = CardDefaults.cardColors(colorResource(id = R.color.white))
            ) {
                Row(
                    modifier = Modifier
                        .width(240.dp)
                        .padding(10.dp, 10.dp, 10.dp, 10.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(7.dp))
                    Icon(
                        painter = icon,
                        contentDescription = null,
                        modifier = Modifier
                            .size(52.dp),
                        tint = Color.Unspecified
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        AutoSizedText(
                            text = title,
                            size = 29,
                            color = colorResource(id = R.color.black),
                        )
                    }
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun videoList(){
    Column(
        Modifier
            .background(color = colorResource(id = R.color.bgSports))
            .fillMaxWidth()
            .fillMaxHeight()
    ){
        //ExoPlayerView(videoUri =  "https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/720/Big_Buck_Bunny_720_10s_1MB.mp4", modifier = Modifier.size(300.dp, 168.dp))
        //放按鈕和預覽影片
        //點擊啟動撥放器
    }
}

//開啟播放器
@Composable
fun ExoPlayerView(videoUri: String, modifier: Modifier = Modifier.size(300.dp, 168.dp)) {
    val context = LocalContext.current
    val exoPlayer = remember { ExoPlayer.Builder(context).build() }

    DisposableEffect(
        AndroidView(factory = {
            PlayerView(context).apply {
                player = exoPlayer
            }
        }, modifier = modifier)
    ) {
        onDispose {
            exoPlayer.release()
        }
    }

    LaunchedEffect(videoUri) {
        val mediaItem = MediaItem.fromUri(Uri.parse(videoUri))
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
    }
}

//取得預覽圖，這裡還沒弄好
@Composable
fun VideoThumbnail(videoUri: Uri, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var thumbnail by remember { mutableStateOf<Bitmap?>(null) }
    var player: ExoPlayer? = remember { null }
    LaunchedEffect(videoUri) {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(context, videoUri)
        thumbnail = retriever.getFrameAtTime(1_000_000) // 获取1秒处的帧，单位为微秒
        retriever.release()
    }

    thumbnail?.let {
        Image(
            bitmap = it.asImageBitmap(),
            contentDescription = null,
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    } ?: run {
        // 显示占位符图像
        Image(
            painter = painterResource(id = R.drawable.ic_play),
            contentDescription = null,
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    }
}