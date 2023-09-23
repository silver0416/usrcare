package com.tku.usrcare.view.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp

@Composable
fun FixedSizeText(text: String, size: Dp, color: Color = Color.Black, fontWeight: FontWeight = FontWeight.Normal) {
    // 獲取當前的像素密度（density）
    val density = LocalDensity.current.density

    // 計算固定的字體尺寸
    val fixedFontSize = size.value / density

    Text(
        text = text,
        style = androidx.compose.ui.text.TextStyle(
            fontSize = fixedFontSize.sp,
            fontWeight = fontWeight
        ),
        color = color,
    )
}