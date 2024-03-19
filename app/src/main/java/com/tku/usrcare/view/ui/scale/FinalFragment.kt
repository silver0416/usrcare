package com.tku.usrcare.view.ui.scale

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tku.usrcare.R
import com.tku.usrcare.view.component.AutoSizedText
import com.tku.usrcare.view.component.findActivity

@Composable
fun FinalFragment(result: String = "A", navHostController: NavHostController) {
    val resultA = "親愛的朋友，\n" +
            "\n" +
            "感謝您完成心情量表，希望一同維護您的健康和幸福。\n" +
            "讓我們一起保持好狀態！\n" +
            "\n" +
            "愛陪伴團隊關心您"
    val resultB = "親愛的朋友，\n" +
            "\n" +
            "感謝您完成心情量表，希望一同維護您的健康和幸福。\n" +
            "鼓勵您與醫療專業人員分享，以獲得更多的支持。\n" +
            "\n" +
            "愛陪伴團隊關心您" +
            "\n" +
            "電話諮詢：衛生福利部嘉南療養院-失智共同照護中心 (06)2795019-3590。"

    BackHandler {
        navHostController.navigate("ScaleList")
    }
    val context = LocalContext.current
    // 讓StatusBar顯示白色
    context.findActivity()
        ?.let {
            val windowInsetsController = WindowInsetsControllerCompat(it.window, it.window.decorView)
            windowInsetsController.isAppearanceLightStatusBars = true
            it.window.statusBarColor = android.graphics.Color.WHITE // Set status bar color to white
        }


    Column(
        modifier = Modifier
            .background(color = colorResource(id = R.color.white))
            .fillMaxWidth()
            .padding(25.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.3f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_moodscale),
                contentDescription = "",
                modifier = Modifier.weight(0.35f)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.55f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_checkmark),
                    contentDescription = ""
                )
                AutoSizedText(
                    text = "已完成",
                    size = 35,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f)
        ) {
            Text(
                text = if (result == "A") resultA else resultB,
                fontSize = 25.sp,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(align = Alignment.CenterVertically)
                .weight(0.3f)
                .clip(shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp))
                .clickable {
                    navHostController.navigate("ScaleList")
                }
                .background(color = colorResource(id = R.color.btnMoodScaleColor).copy(alpha = 0.5f))
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_congratulations_a),
                    contentDescription = ""
                )
                Column(
                    modifier = Modifier
                        .padding(start = 8.dp),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_coin),
                        contentDescription = ""
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(
                        text = "+10",
                        fontSize = 20.sp,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.ic_congratulations_b),
                    contentDescription = ""
                )
            }
            Text(
                text = "-返回並獲得獎勵-",
                fontSize = 28.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
        }
    }
}

@Composable
@Preview
fun FinalFragmentPreview() {
    FinalFragment(navHostController = rememberNavController())
}