package com.tku.usrcare.view.ui.sports

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import android.os.Build
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.tku.usrcare.R


@Destination(
    route = "VideoEmbedPlayer/{videoId}",
    start = false
)
@Composable
fun WebViewContainer(
    videoId: String,
    navigator: com.ramcosta.composedestinations.navigation.DestinationsNavigator
) {
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

    var youTubePlayerView: YouTubePlayerView? = null
    DisposableEffect(Unit) {
        onDispose {
            // 釋放 YouTubePlayerView
            youTubePlayerView?.release()
        }
    }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            context.findActivity()?.window?.insetsController?.hide(WindowInsets.Type.statusBars())
            context.findActivity()?.window?.insetsController?.hide(WindowInsets.Type.systemBars())
        } else {
            context.findActivity()?.window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            context.findActivity()?.window?.decorView?.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_IMMERSIVE
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                    )
        }
        context.findActivity()?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(androidx.compose.ui.graphics.Color.Black)
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (player, back) = createRefs()
            Log.d("videoId", videoId)
            AndroidView(modifier = Modifier
                .padding(78.dp)   // 調整影片邊界
                .constrainAs(player) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .aspectRatio(16f / 9f),
                factory = { ctx ->
                    YouTubePlayerView(ctx).apply {
                        addYouTubePlayerListener(
                            object : AbstractYouTubePlayerListener() {
                                override fun onReady(youTubePlayer: YouTubePlayer) {
                                    super.onReady(youTubePlayer)
                                    youTubePlayer.loadVideo(videoId, 0f)
                                }
                            }
                        )
                        youTubePlayerView = this
                    }
                },)
        }
        Image(painter = painterResource(id = R.drawable.ic_back),
            contentDescription = null,
            modifier = Modifier
                .padding(25.dp)
                .clickable {
                    context.findActivity()?.requestedOrientation =
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        // For API level 30 and above
                        context.findActivity()?.window?.insetsController?.show(WindowInsets.Type.statusBars())
                        context.findActivity()?.window?.insetsController?.show(WindowInsets.Type.systemBars())
                    } else {
                        // For below API level 30
                        context.findActivity()?.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                        // Show the system bar
                        context.findActivity()?.window?.decorView?.systemUiVisibility = (
                                View.SYSTEM_UI_FLAG_IMMERSIVE
                                )
                    }
                    navigator.popBackStack()
                }
        )
    }
}

fun Modifier.aspectRatio(ratio: Float): Modifier {
    return this.layout { measurable, constraints ->
        val width = constraints.maxWidth
        val height = (width / ratio).toInt()
        val placeable = measurable.measure(constraints.copy(maxHeight = height))

        layout(placeable.width, placeable.height) {
            placeable.placeRelative(0, 0)
        }
    }
}
