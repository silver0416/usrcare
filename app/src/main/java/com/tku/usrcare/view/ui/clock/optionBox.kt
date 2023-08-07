package com.tku.usrcare.view.ui.clock

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OptionBox(options: Array<String>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = Color.Black,
                shape = RoundedCornerShape(12.dp) // 設定圓角大小
            ), contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        // 一排三個
        Column(modifier = Modifier.padding(13.dp), verticalArrangement = Arrangement.Center) {
            for (i in options.indices step 3) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    for (j in i..i + 2) {
                        if (j < options.size) {
                            Options(option = options[j])
                            Spacer(modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 0.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Options(option: String) {
    OutlinedButton(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(20.dp))
            .background(Color.White),
        onClick = { /*TODO*/ }
    ) {
        Text(text = option, fontSize = 20.sp , color = Color.Black)
    }
}


@Preview(showBackground = true)
@Composable
fun OptionBoxPreview() {
    OptionBox(arrayOf("降血壓", "降血糖", "降血脂", "鈣補充", "胃藥","維他命"))
}