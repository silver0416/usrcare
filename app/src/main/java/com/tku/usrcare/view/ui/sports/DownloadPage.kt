package com.tku.usrcare.view.ui.sports

import android.app.DownloadManager
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.FrameLayout
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter.State.Empty.painter
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.decode.VideoFrameDecoder
import coil.request.ImageRequest
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.video.VideoSize
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.tku.usrcare.R
import com.tku.usrcare.api.ApiUSR
import com.tku.usrcare.model.VideoList
import com.tku.usrcare.model.VideoListResponse
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.KtvActivity
import com.tku.usrcare.view.MainActivity
import com.tku.usrcare.view.SportsActivity
import com.tku.usrcare.view.component.AutoSizedText
import com.tku.usrcare.view.component.FixedSizeText
import com.tku.usrcare.view.component.findActivity
import com.tku.usrcare.view.component.normalAlertDialog
import java.time.Duration
import java.time.Instant

@Destination(
    route = "DownloadPage",
    start = false
)
@Composable
fun DownloadPage(navigator: DestinationsNavigator, url: String?) {
    val isGetVideo = remember {
        mutableStateOf(false)
    }
    val showDialog = remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current as SportsActivity
    var isPlaying by remember { mutableStateOf(false) }
    var playingVideoUri by remember { mutableStateOf("") }
    playingVideoUri = url ?: playingVideoUri
    LaunchedEffect(url)
    {
        if (url != null) isPlaying = true
    }

    var videoInformationList by remember { mutableStateOf(VideoListResponse(listOf())) }
    ApiUSR.getVideoList(
        SessionManager(context).getUserToken().toString(),
        onSuccess = {
            Log.d("DownloadPage", it.toString())
            videoInformationList = it
            isGetVideo.value = true
            //Log.d("DownloadPage", SessionManager(context).getUserToken().toString())
            //Log.d("DownloadPage", videoInformationList.list.toString())
        },
        onError = {
        },
        onInternetError = {
        }
    )



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
                modifier = Modifier.padding(
                    top = 20.dp,
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 20.dp
                ),
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
    fun TopBar(navigator: DestinationsNavigator) {
        Column(
            Modifier
                .background(color = colorResource(id = R.color.bgSports))
                .fillMaxWidth()
        ) {
            TitleBoxForSports(
                color = colorResource(id = R.color.btnAiVitalityDetection),
                title = "觀看成果",
                icon = painterResource(id = R.drawable.ic_download),
                navigator = navigator,
            )
        }
    }

    //播放器
    @Composable
    fun ExoPlayerView(videoUri: String, onClose: () -> Unit) {
        val exoPlayer = remember { ExoPlayer.Builder(context).build() }
        DisposableEffect(exoPlayer) {
            onDispose {
                exoPlayer.release()
                context.findActivity()?.requestedOrientation =
                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                context.disableFullScreen()
                //釋放播放器資源
            }
        }
        LaunchedEffect(videoUri) {
            if (isPlaying) {
                val mediaItem = MediaItem.fromUri(videoUri)
                exoPlayer.setMediaItem(mediaItem)

                exoPlayer.prepare()
                exoPlayer.playWhenReady = true
                context.findActivity()?.requestedOrientation =
                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                context.enableFullScreen()
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            AndroidView(
                factory = {
                    PlayerView(context).apply {
                        player = exoPlayer
                        layoutParams = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        useController = true
                        setBackgroundColor(android.graphics.Color.BLACK)
                    }
                }, modifier = Modifier.fillMaxSize()
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "返回按鈕",
                tint = Color.Unspecified,
                modifier = Modifier
                    .padding(25.dp)
                    .clickable(onClick =
                    {
                        exoPlayer.stop()
                        onClose()
                        /*context.findActivity()?.requestedOrientation =
                            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT*/
                        //navigator.navigate(DownloadPageDestination(url = null))
                    }
                    )
            )
        }
    }

    //利用預覽圖製作button
    @Composable
    fun makeVideoThumbnail(videoUri: String) {
        val painter = rememberAsyncImagePainter(
            ImageRequest.Builder(context)
                .data(videoUri)
                .decoderFactory(VideoFrameDecoder.Factory())
                .build()
        )
        Box(
            modifier = Modifier
                .size(300.dp, 168.dp)
                .padding(12.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(color = colorResource(id = R.color.black)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(300.dp, 168.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable {
                        playingVideoUri = videoUri
                        isPlaying = true
                        //開啟播放器
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(300.dp, 168.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_play),
                    contentDescription = "Play Icon",
                    modifier = Modifier.size(80.dp),
                )
            }
        }
    }

    fun downloadVideo(context: Context, url: String, fileName: String) {
        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle(fileName)
            .setDescription("下載中...")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }

    fun formatDuration(duration: Duration): String {
        val days = duration.toDays()
        val hours = (duration.toHours() % 24)  // 剩餘小時
        val minutes = (duration.toMinutes() % 60)  // 剩餘分鐘
        val seconds = (duration.seconds % 60)  // 剩餘秒數

        return "$days 天, $hours 小時, $minutes 分鐘"
    }

    @Composable
    fun ExpandableListItem(videoList: VideoList) {
        var expanded by remember { mutableStateOf(false) }
        androidx.compose.material.Button(
            onClick = { expanded = !expanded },
            modifier = Modifier
                .fillMaxSize()
                .size(50.dp),
            colors = androidx.compose.material.ButtonDefaults.buttonColors(
                backgroundColor = colorResource(id = R.color.bgSports),
                contentColor = Color.Black
            ),
            elevation = androidx.compose.material.ButtonDefaults.elevation( // 移除按鈕的陰影
                defaultElevation = 0.dp,
                pressedElevation = 0.dp,
                disabledElevation = 0.dp
            ),
            shape = RoundedCornerShape(0.dp),
        ) {
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize())
            {
                var text=""
                if(videoList.score==null)
                {
                    text="活力指數:未知"
                }
                else
                {
                    text="活力指數:${videoList.score}分"
                }
                FixedSizeText(
                    text = text,
                    size = 60.dp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        val instantNow = Instant.now()
        AnimatedVisibility(visible = expanded) {
            val instant=Instant.parse(videoList.expiry_time+"Z")
            val duration = Duration.between(instantNow, instant)
            var deadline = ""
            if(duration.seconds>=0)
            {
                deadline = formatDuration(duration)
                Log.d("downloadPage",deadline)
            }
            else
            {
                deadline = "已過期"
            }
            Column{
                FixedSizeText(
                    text = "●上傳日期:${videoList.upload_time.substring(0, 10)}",
                    color = colorResource(id = R.color.black),
                    //modifier = Modifier.padding(10.dp),
                    size = 60.dp,
                )
                FixedSizeText(
                    text = "●剩餘時間:$deadline",
                    color = colorResource(id = R.color.black),
                    //modifier = Modifier.padding(10.dp),
                    size = 60.dp,
                )
                Row {
                    FixedSizeText(
                        text = "●下載此影片:  ",
                        color = colorResource(id = R.color.black),
                        //modifier = Modifier.padding(10.dp),
                        size = 60.dp,
                    )
                    Button(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(50.dp),
                        onClick = {
                            if(videoList.url.toString()!="null")
                            {
                                downloadVideo(
                                    context,
                                    videoList.url.toString(),
                                    videoList.videoID.toString()
                                )
                            }
                            else
                            {
                                showDialog.value = true
                            }
                        }, contentPadding = PaddingValues(1.dp)
                    ) {
                        Icon(//下載功能可能會需要一個反應或回潰
                            painter = painterResource(id = R.drawable.ic_download),
                            contentDescription = "下載按鈕",
                            modifier = Modifier
                                .size(50.dp),
                            tint = Color.Unspecified
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun videoList() {
        LazyColumn(
            modifier = Modifier
                .background(color = colorResource(id = R.color.bgSports))
                //.fillMaxWidth()
                //.fillMaxHeight(),
        )
        {
            items(videoInformationList.list) { item ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    //modifier = Modifier.background(color = colorResource(id = R.color.black))
                ) {
                    makeVideoThumbnail(videoUri = item.url.toString())
                    ExpandableListItem(item)
                    /*Row {
                        var deadline = ""
                        if(item.expiry_time!=null&&item.expiry_time.length>=10)
                        {
                            deadline = item.expiry_time.substring(0,10)
                        }
                        else
                        {
                            deadline = "未知"
                        }
                        Text(
                            text = "有效日期:$deadline",
                            color = colorResource(id = R.color.black),
                            //modifier = Modifier.padding(10.dp),
                            fontSize = 25.sp
                        )
                        Button(
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(50.dp),
                            onClick = {
                                downloadVideo(context, item.url.toString(), item.videoID.toString())
                            }, contentPadding = PaddingValues(1.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_download),
                                contentDescription = "下載按鈕",
                                modifier = Modifier
                                    .size(50.dp),
                                tint = Color.Unspecified
                            )
                        }
                    }*/
                }
                /*Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    makeVideoThumbnail(videoUri = item.url.toString())
                    Button(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(50.dp),
                        onClick = {
                            downloadVideo(context, item.url.toString(), item.videoID.toString())
                        }, contentPadding = PaddingValues(1.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_download),
                            contentDescription = "下載按鈕",
                            modifier = Modifier
                                .size(50.dp),
                            tint = Color.Unspecified
                        )
                    }
                }*/
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize()) {
            TopBar(navigator)
            if (isGetVideo.value) {
                videoList()
            }
        }
        if (isPlaying) {
            //Log.d("DownloadPage", "Playing$playingVideoUri")
            if (playingVideoUri != "null")
            {
                ExoPlayerView(playingVideoUri, onClose = { isPlaying = false;playingVideoUri = "" })

            }
            else
            {
                isPlaying = false
                showDialog.value = true
            }
        }
        normalAlertDialog(
            onDismiss = { showDialog.value = false },
            onConfirm = { showDialog.value = false },
            showDialog = showDialog.value,
            title = "通知",
            content = "已過期的影片無法觀看和下載，您若希望隨時能夠觀看影片,可以考慮將影片下載到您的手機裡。",
            buttonText = "我知道了。",
            color = Color.Red,
            backgroundColor = Color.White
        )
    }
}





