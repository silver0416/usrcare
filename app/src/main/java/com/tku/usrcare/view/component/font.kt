package com.tku.usrcare.view.component


import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
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

@Composable
fun AutoSizedText(text: String, size: Int = 20, color: Color = Color.Black, fontWeight: FontWeight = FontWeight.Normal) {
    var textStyle by remember {
        mutableStateOf(
            TextStyle(
                fontSize = size.sp, color = color, fontWeight = fontWeight
            )
        )
    }
    var readyToDraw by remember { mutableStateOf(false) }
    Text(
        text = text,
        maxLines = 1,
        softWrap = false,
        style = textStyle,
        modifier = Modifier.drawWithContent {
            if (readyToDraw) drawContent()
        },
        onTextLayout = { textLayoutResult ->
            if (textLayoutResult.didOverflowWidth) {
                textStyle = textStyle.copy(fontSize = textStyle.fontSize * 0.9)
            } else {
                readyToDraw = true
            }
        }
    )
}