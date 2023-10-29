package com.tku.usrcare.view.ui.main.cards

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import com.tku.usrcare.R
import com.tku.usrcare.view.component.AutoSizedText
import com.tku.usrcare.view.ui.main.DefaultAvatar
import com.tku.usrcare.view.ui.main.MainPage
import com.tku.usrcare.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MainCard(mainViewModel: MainViewModel) {
    val name = mainViewModel.userName.toString()
//    val name = "馬蓋仙"
    Card(
        modifier = Modifier.fillMaxSize(),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.white).copy(alpha = 0.0f),
        ),
    ) {
        Row(
            modifier = Modifier
                .padding(start = 5.dp)
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.3f)
            ) {
                DefaultAvatar(name = name)
            }
            Column(
                modifier = Modifier
                    .weight(0.4f)
                    .padding(start = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(modifier = Modifier.weight(0.5f), contentAlignment = Alignment.Center) {
                    AutoSizedText(text = name, size = 35)
                }
                Box(modifier = Modifier.weight(0.5f)) { CoinBox(mainViewModel) }
            }
        }
    }
}

@Composable
fun CoinBox(mainViewModel: MainViewModel) {
    Button(
        onClick = { /*TODO*/ },
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(5.dp),
        shape = RoundedCornerShape(37.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.btnCoin)
        ),
        border = BorderStroke(3.dp, color = colorResource(id = R.color.MainButtonColor)),
    ) {
        val context = LocalContext.current
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 5.dp, end = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            val scale = remember { mutableFloatStateOf(1f) }
            val coroutineScope = rememberCoroutineScope()
            LaunchedEffect(Unit) {
                mainViewModel.points.observe(context as LifecycleOwner) {
                    coroutineScope.launch {
                        scale.floatValue = 1.2f // 放大
                        // 使用協程延遲，然後縮小
                        delay(300)
                        scale.floatValue = 1f
                    }
                }
            }

            val animatedScale by animateFloatAsState(
                targetValue = scale.floatValue,
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                ), label = ""
            )
            Image(
                painter = painterResource(id = R.drawable.ic_coin),
                contentDescription = null,
                modifier = Modifier
                    .weight(0.3f)
                    .graphicsLayer {
                        scaleX = animatedScale
                        scaleY = animatedScale
                    }
            )
            Spacer(
                modifier = Modifier
                    .size(5.dp)
                    .weight(0.03f)
            )
            Box(modifier = Modifier.weight(0.7f), contentAlignment = Alignment.Center) {
                val points = remember { mutableStateOf("") }
                mainViewModel.points.observe(context as androidx.lifecycle.LifecycleOwner) {
                    points.value = it
                }
                AutoSizedText(
                    text = points.value,
                    size = 30,
                    color = colorResource(id = R.color.black),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
@Preview
fun MainCardPreview() {
    MainPage()
}