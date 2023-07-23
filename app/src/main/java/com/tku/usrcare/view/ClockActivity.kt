package com.tku.usrcare.view

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.icu.text.CaseMap.Title
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.darkColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.google.android.material.color.utilities.MaterialDynamicColors.background
import com.tku.usrcare.R
import com.tku.usrcare.view.ui.theme.UsrcareTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import com.google.android.material.bottomsheet.BottomSheetDialogFragment as BottomSheetDialogFragment1

class ClockActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UsrcareTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Main()
                }
            }
        }
    }
}

fun Context.findActivity(): Activity? {
    var currentContext = this
    while (currentContext is ContextWrapper) {
        if (currentContext is Activity) {
            return currentContext
        }
        currentContext = currentContext.baseContext
    }
    return null
}

@Composable
fun Main(){
    Box(
        Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.bgClock))
    ) {
        TitleBox()
        CenterButtons()
        NoticeList()
    }
}

@Composable
fun TitleBox() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val activity = context.findActivity()
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        Card(modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
            colors = CardDefaults.cardColors(colorResource(id = R.color.btnClockColor))
        ) {
            Row(
                modifier = Modifier
                    .width(320.dp)
                    .padding(3.dp, 10.dp, 10.dp, 10.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                Button(
                    onClick = {
                              activity?.finish()
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
                Spacer(Modifier.weight(1f))
                Icon(painter = painterResource(id = R.drawable.ic_clocknotice),
                    contentDescription = null,
                    modifier = Modifier
                        .size(42.dp),
                    tint = colorResource(id = R.color.white))
                Spacer(modifier = Modifier.width(7.dp))
                Text(
                    text = stringResource(R.string.clock_reminder),
                    fontSize = 28.sp,
                    color = colorResource(id = R.color.white),
                )
                Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun CenterButtons() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { /* Handle click */ },
                modifier = Modifier
                    .width(282.dp)
                    .height(93.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.MainButtonColor))
            ) {
                Text(
                    text = stringResource(R.string.drug_reminder),
                    fontSize = 40.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* Handle click */ },
                modifier = Modifier
                    .width(282.dp)
                    .height(93.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.MainButtonColor))
            ) {
                Text(stringResource(R.string.activity_reminder), fontSize = 40.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* Handle click */ },
                modifier = Modifier
                    .width(282.dp)
                    .height(93.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.MainButtonColor))
            ) {
                Text(stringResource(R.string.drink_water_reminder), fontSize = 40.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* Handle click */ },
                modifier = Modifier
                    .width(282.dp)
                    .height(93.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.MainButtonColor))
            ) {
                Text(stringResource(R.string.rest_reminder), fontSize = 40.sp)
            }
        }
    }
}

@Composable
fun NoticeList() {
    val coroutineScope = rememberCoroutineScope()
    val offsetY = remember { Animatable(1200f) } // Initialize at -180f
    val sheetHeight = 600f  // The height of the bottom sheet
    var status = false
    val context = LocalContext.current
    val activity = context.findActivity()

    BackHandler(true) {
        if (status) {
            coroutineScope.launch { offsetY.animateTo(1200f) }
            status = false
        } else {
            activity?.finish()
        }
    }

    Box(
        Modifier
            .fillMaxSize()
    ) {
        Box(
            Modifier
                .offset { IntOffset(x = 0, y = offsetY.value.roundToInt()) }
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(Color.Gray)
                .align(Alignment.BottomCenter)
                .width(350.dp)
                .height(Dp(sheetHeight)) // Set the height of the bottom sheet
                .pointerInput(Unit) {
                    detectDragGestures(onDragEnd = {
                        if (offsetY.value - sheetHeight > 120f) {  // Change this to check the top position of the bottom sheet
                            coroutineScope.launch { offsetY.animateTo(1200f) }
                            status = false
                        } else {
                            coroutineScope.launch { offsetY.animateTo(-300f) }
                            status = true
                        }
                    }) { change, dragAmount ->
                        coroutineScope.launch {
                            val original = offsetY.value
                            val newValue = (original + dragAmount.y).coerceIn(-1200f, 1200f)
                            offsetY.snapTo(newValue)
                        }
                        change.consume()
                    }
                }
        ) {
            Text("這裡放使用者新增的提醒列表", Modifier.align(Alignment.Center))
        }
    }
}








@Preview(showBackground = true)
@Composable
fun Preview() {
    UsrcareTheme {
        Main()
    }
}
