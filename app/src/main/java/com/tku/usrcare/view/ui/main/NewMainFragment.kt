package com.tku.usrcare.view.ui.main

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tku.usrcare.R
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.SettingActivity
import com.tku.usrcare.view.component.AutoSizedText
import com.tku.usrcare.view.findActivity
import com.tku.usrcare.view.ui.main.cards.DailyEnglishCard
import com.tku.usrcare.view.ui.main.cards.DailyEnglishCardContent
import com.tku.usrcare.view.ui.main.cards.HistoryCard
import com.tku.usrcare.view.ui.main.cards.HistoryStoryContent
import com.tku.usrcare.view.ui.main.cards.MainCard
import com.tku.usrcare.viewmodel.MainViewModel
import com.tku.usrcare.viewmodel.ViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


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

    var isExpanded = remember { mutableStateOf(false) }
    val positionInDp = remember { mutableStateOf(0.dp) }
    val positionInDpGot = remember { mutableStateOf(false) }
    val noRippleInteractionSource = remember { MutableInteractionSource() }
    val clickedCard = remember { mutableIntStateOf(0) }

    if (isExpanded.value) {
        BackHandler {
            isExpanded.value = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.bgMain))
    ) {
        Column(
            modifier = Modifier
                .padding(top = 15.dp, start = 25.dp, end = 25.dp, bottom = 25.dp)
                .fillMaxSize(),
        ) {
            val transition =
                updateTransition(targetState = isExpanded.value, label = "BoxExpandTransition")
            val boxOffsetY by transition.animateDp(label = "BoxOffsetY",
                transitionSpec = {
                    tween(durationMillis = 300)
                }
            ) { state ->
                if (state) with(LocalDensity.current) { -(positionInDp.value) } else 0.dp
            }
            val zIndex by transition.animateFloat(label = "BoxZIndex") { state ->
                if (state) 1f else 0f
            }

            val density = LocalDensity.current
            val statusBarHeight = WindowInsets.systemBars.asPaddingValues().calculateTopPadding()

            Box(
                modifier = Modifier
                    .weight(0.15f)
                    .fillMaxWidth()
            ) {
                TitleBar()
            }
            Spacer(
                modifier = Modifier
                    .size(10.dp)
                    .weight(0.03f)
            )
            Box(
                modifier = Modifier
                    .weight(0.45f)
                    .fillMaxSize()
                    .zIndex(zIndex)
                    .absoluteOffset(y = boxOffsetY)
                    .onGloballyPositioned { layoutCoordinates ->
                        if (!positionInDpGot.value) {
                            val positionInWindow = layoutCoordinates.positionInWindow()
                            positionInDp.value = with(density) { positionInWindow.y.toDp() }
                        }
                    }
            ) {
                CardList(isExpanded = isExpanded, clickedCard = clickedCard)
            }

            Spacer(
                modifier = Modifier
                    .size(10.dp)
                    .weight(0.03f)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                FunctionButtons()
            }
        }
        AnimatedVisibility(
            visible = isExpanded.value,
            enter = fadeIn(animationSpec = tween(durationMillis = 200)) +
                    expandVertically(
                        animationSpec = tween(durationMillis = 200),
                        expandFrom = Alignment.Top
                    ),
            exit = fadeOut(animationSpec = tween(durationMillis = 300)) +
                    shrinkVertically(
                        animationSpec = tween(durationMillis = 300),
                        shrinkTowards = Alignment.Top
                    ),
        ) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(25.dp)
                    .clickable(
                        interactionSource = noRippleInteractionSource,
                        indication = null
                    ) {
                        isExpanded.value = !isExpanded.value
                    },
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(id = R.color.white),
                )
            ) {
                Box {
                    val lazyListState = remember { LazyListState() }
                    val coroutineScope = rememberCoroutineScope()
                    val firstVisibleItemIndex =
                        remember { derivedStateOf { lazyListState.firstVisibleItemIndex } }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        state = lazyListState,
                    ) {
                        item {
                            Column {
                                Image(
                                    painterResource(id = R.drawable.ic_back),
                                    contentDescription = "back",
                                    Modifier
                                        .padding(top = 10.dp, start = 10.dp)
                                        .clickable(
                                            interactionSource = noRippleInteractionSource,
                                            indication = null
                                        ) {
                                            isExpanded.value = !isExpanded.value
                                        },
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(280.dp)
                                ) {
                                    when (clickedCard.intValue) {
                                        1 -> DailyEnglishCard(
                                            mainViewModel = mainViewModel,
                                            isExpanded = isExpanded,
                                            showContent = true,
                                            clickedCard = clickedCard
                                        )

                                        2 -> HistoryCard(
                                            mainViewModel = mainViewModel,
                                            isExpanded = isExpanded,
                                            showContent = true,
                                            clickedCard = clickedCard
                                        )

                                        else -> isExpanded.value = false
                                    }
                                }
                            }
                        }
                        item {
                            Row {
                                Spacer(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .weight(0.1f)
                                )
                                Box(
                                    modifier = Modifier
                                        .padding(
                                            bottom = 8.dp
                                        )
                                        .weight(0.8f)
                                ) {
                                    when (clickedCard.intValue) {
                                        1 -> DailyEnglishCardContent(mainViewModel = mainViewModel)
                                        2 -> HistoryStoryContent(mainViewModel = mainViewModel)
                                        else -> isExpanded.value = false
                                    }
                                }
                                Spacer(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .weight(0.1f)
                                )
                            }
                        }
                    }
                    val showToTopButton = remember { mutableStateOf(false) }
                    showToTopButton.value = firstVisibleItemIndex.value != 0
                    androidx.compose.animation.AnimatedVisibility(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(bottom = 20.dp, end = 30.dp)
                            .size(60.dp),
                        visible = showToTopButton.value,
                        enter = fadeIn(animationSpec = tween(durationMillis = 200)) + scaleIn(
                            animationSpec = tween(durationMillis = 200)
                        ),
                        exit = fadeOut(animationSpec = tween(durationMillis = 200)) + scaleOut(
                            animationSpec = tween(durationMillis = 200)
                        ),
                    ) {
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    lazyListState.animateScrollToItem(
                                        0
                                    )
                                }
                            },
                            colors = buttonColors(
                                containerColor = colorResource(id = R.color.MainButtonColor)
                            ),
                            shape = CircleShape,
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Image(
                                Icons.Filled.KeyboardArrowUp,
                                contentDescription = "up",
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                }
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
        Box(
            modifier = Modifier
                .weight(0.6f)
                .padding(start = 10.dp)
        ) {
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
fun CardList(isExpanded: MutableState<Boolean>, clickedCard: MutableState<Int>) {
    Box() {
        val realTotalPage = 3 // 總共有 3 個頁面（Card）
        val startPage = 999 // 起始頁面為第 99 頁
        val pagerState = rememberPagerState(
            pageCount = { Int.MAX_VALUE },
            initialPage = startPage,
        )
        val isTouched = remember { mutableStateOf(false) }
//         LaunchedEffect 會在這個 Composable 啟動後運行裡面的程式碼
        LaunchedEffect(Unit) { // 使用 Unit 作為 key，確保只會運行一次
            delay(1000) // 延遲 1 秒
            if (!isTouched.value) {
                pagerState.animateScrollToPage((1..< realTotalPage ).random()+startPage)
            } // 切換至第一頁以外的其他隨機頁（假設有三個頁面）
            delay(3000) // 延遲 3 秒
            if (!isTouched.value) {
                pagerState.animateScrollToPage(startPage)
            } // 切換回第一頁
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            isTouched.value = true
                        },
                    )
                },

            ) { page ->
            // 這裡的 `page` 是當前頁面的索引
            when (page % 3) {
                0 -> MainCard(mainViewModel = mainViewModel)
                1 -> DailyEnglishCard(
                    mainViewModel = mainViewModel,
                    isExpanded = isExpanded,
                    clickedCard = clickedCard
                )

                2 -> HistoryCard(
                    mainViewModel = mainViewModel,
                    isExpanded = isExpanded,
                    clickedCard = clickedCard
                )
            }
        }

    }
}


@Composable
@Preview(showBackground = true)
fun DefaultPreview() {
    MainPage()
}

