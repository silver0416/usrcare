package com.tku.usrcare.view.ui.main

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tku.usrcare.R
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.SettingActivity
import com.tku.usrcare.view.component.AutoSizedText
import com.tku.usrcare.view.findActivity
import com.tku.usrcare.view.ui.main.cards.DailyEnglishCard
import com.tku.usrcare.view.ui.main.cards.HistoryCard
import com.tku.usrcare.view.ui.main.cards.MainCard
import com.tku.usrcare.viewmodel.MainViewModel
import com.tku.usrcare.viewmodel.ViewModelFactory
import kotlinx.coroutines.delay


private lateinit var mainViewModel: MainViewModel

@Composable
fun MainPage() {
    val context = LocalContext.current
    val sessionManager = SessionManager(context)
    val viewModelFactory = ViewModelFactory(sessionManager)
    mainViewModel =
        viewModel(
            viewModelStoreOwner = context as ViewModelStoreOwner,
            factory = viewModelFactory
        )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.bgMain))
    ) {
        Column(
            modifier = Modifier
                .padding(25.dp)
                .fillMaxSize(),
        ) {
            Box(
                modifier = Modifier
                    .weight(0.13f)
                    .fillMaxWidth()
            ) {
                TitleBar()
            }
            Box(
                modifier = Modifier
                    .weight(0.45f)
                    .fillMaxWidth()
            ) {
                CardList()
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                FunctionButtons()
            }
        }
    }
}


@Composable
fun TitleBar() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_b),
            contentDescription = null,
            modifier = Modifier
                .weight(0.30f) // 設定權重
                .size(48.dp)
        )
        Box(modifier = Modifier
            .weight(0.6f)
            .padding(start = 10.dp)) {
            AutoSizedText(
                text = stringResource(id = R.string.app_name),
                size = 40,
                color = Color.Black
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.7f), // 設定權重
            horizontalArrangement = Arrangement.End,
        ) {
            val shape = androidx.compose.foundation.shape.CircleShape
            val buttonColors = buttonColors(Color.White)
            val border = BorderStroke(3.dp, color = colorResource(id = R.color.strokeGray))
            val contentPadding = PaddingValues(0.dp)
            val modifier = Modifier.size(48.dp)
            val context = LocalContext.current
            OutlinedButton(
                onClick = { /*TODO*/ },
                shape = shape,
                colors = buttonColors,
                border = border,
                contentPadding = contentPadding,
                modifier = modifier,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_notification),
                    contentDescription = "notification",
                    tint = Color.Black
                )
            }
            Spacer(modifier = Modifier.size(13.dp))
            OutlinedButton(
                onClick = {
                    context.findActivity()?.startActivity(
                        Intent(
                            context, SettingActivity::class.java
                        )
                    )
                },
                shape = shape,
                colors = buttonColors,
                border = border,
                contentPadding = contentPadding,
                modifier = modifier,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_setting),
                    contentDescription = "setting",
                    tint = Color.Black
                )
            }
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardList() {
    Box(modifier = Modifier.padding(6.dp)) {
        val pagerState = rememberPagerState(
            pageCount = { 3 }, // 總共有 5 個頁面（Card）
        )
        // LaunchedEffect 會在這個 Composable 啟動後運行裡面的程式碼
        LaunchedEffect(Unit) { // 使用 Unit 作為 key，確保只會運行一次
            delay(1000) // 延遲 2 秒
            pagerState.animateScrollToPage((1..<pagerState.pageCount).random()) // 切換至第一頁以外的其他隨機頁（假設有三個頁面）
            delay(3000) // 延遲 3 秒
            pagerState.animateScrollToPage(0) // 切換回第一頁
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(top = 10.dp, bottom = 8.dp)
        ) { page ->
            // 這裡的 `page` 是當前頁面的索引
            when (page) {
                0 -> MainCard(mainViewModel = mainViewModel)
                1 -> DailyEnglishCard()
                2 -> HistoryCard()
                else -> MainCard(mainViewModel = mainViewModel)
            }
        }

    }
}

@Composable
fun DefaultAvatar(name: String) {
    val initial = name.firstOrNull()?.toString() ?: ""
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(Color.Gray)
            .fillMaxSize()
    ) {
        AutoSizedText(text = initial, size = 50, color = Color.White, fontWeight = FontWeight.Bold)
    }
}


@Composable
@Preview(showBackground = true)
fun DefaultPreview() {
    MainPage()
}

