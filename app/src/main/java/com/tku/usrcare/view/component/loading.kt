package com.tku.usrcare.view.component

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.tku.usrcare.R


@Composable
fun Loading(isVisible: Boolean) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
    if (isVisible) {
        LottieAnimation(composition, iterations = LottieConstants.IterateForever, modifier = Modifier.size(180.dp))
    }
}