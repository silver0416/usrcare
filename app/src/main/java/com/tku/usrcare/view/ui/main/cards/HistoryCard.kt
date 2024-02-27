package com.tku.usrcare.view.ui.main.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tku.usrcare.R
import com.tku.usrcare.view.component.AutoSizedText
import com.tku.usrcare.view.component.FixedSizeText
import com.tku.usrcare.viewmodel.MainViewModel


@Composable
fun HistoryCard(
    mainViewModel: MainViewModel,
    showContent: Boolean = false,
    isExpanded: MutableState<Boolean>,
    clickedCard: MutableState<Int>
) {
    val sessionManager = mainViewModel.mSessionManager
    val historyEvent = remember { mutableStateOf("") }
    val historyDate = remember { mutableStateOf("") }
    val historyContent = remember { mutableStateOf("") }
    val historyTitle = remember { mutableStateOf("") }
    val noRippleInteractionSource = remember { MutableInteractionSource() }

    mainViewModel.historyStoryComplete.observeForever() {
        if (it) {
            val historyStory = sessionManager.getHistoryStory()
            if (historyStory != null) {
                historyEvent.value = historyStory.event
            }
            if (historyStory != null) {
                historyDate.value = historyStory.date
            }
            if (historyStory != null) {
                historyContent.value = historyStory.detail
            }
            if (historyStory != null) {
                historyTitle.value = historyStory.title
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = noRippleInteractionSource,
                indication = null
            ) {
                clickedCard.value = 2
                isExpanded.value = !isExpanded.value
            },
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.white).copy(alpha = 1f),
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column {
                Spacer(
                    modifier = Modifier
                        .size(15.dp)
                        .weight(0.2f)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.8f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(modifier = Modifier.weight(0.23f)) {
                        AutoSizedText(
                            text = historyTitle.value, size = 30
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .size(10.dp)
                            .weight(0.01f)
                    )
                    if (historyTitle.value == stringResource(id = R.string.history_today)) {
                        Box(modifier = Modifier.weight(0.2f)) {
                            AutoSizedText(
                                text = historyDate.value, size = 20
                            )
                        }
                    }
                    Spacer(
                        modifier = Modifier
                            .size(20.dp)
                            .weight(0.01f)
                    )
                    Box(modifier = Modifier.weight(0.22f)) {
                        //historyEvent.value
                        fun Char.isHalfWidth(): Boolean {
                            // 半形字元範圍
                            return this in '\u0020'..'\u007E' || this in '\uFF61'..'\uFFDC' || this in '\uFFE8'..'\uFFEE'
                        }

                        val totalLength = historyEvent.value.length
                        val halfLength = historyEvent.value.filter { it.isHalfWidth() }.length
                        if (!showContent) {
                            AutoSizedText(
                                text =
                                if ((totalLength + halfLength) * 2 > 20) {
                                    if (totalLength < 20) {
                                        historyEvent.value
                                    } else {
                                        var nowLength = 0
                                        var nowIndex = 0
                                        var result = ""
                                        while (nowLength < 20) {
                                            if (historyEvent.value[nowIndex].isHalfWidth()) {
                                                result += historyEvent.value[nowIndex]
                                                nowLength += 1
                                                nowIndex += 1
                                            } else {
                                                result += historyEvent.value[nowIndex]
                                                nowLength += 2
                                                nowIndex += 1
                                            }
                                        }
                                        "$result..."
                                    }
                                } else {
                                    historyEvent.value
                                }, size = 24, fontWeight = FontWeight.Bold
                            )
                        } else {
                            Text(text = historyEvent.value, style = androidx.compose.ui.text.TextStyle(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            ), textAlign = androidx.compose.ui.text.style.TextAlign.Center, modifier = Modifier.fillMaxWidth())
                        }
                    }
                }
                if (!showContent) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.2f),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        FixedSizeText(
                            text = "點擊閱讀更多...",
                            size = 50.dp,
                            color = colorResource(id = R.color.black).copy(alpha = 0.7f)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                    }
                    Spacer(
                        modifier = Modifier
                            .size(15.dp)
                            .weight(0.05f)
                    )
                }
            }
        }
    }
}

@Composable
fun HistoryStoryContent(mainViewModel: MainViewModel) {
    val historyContent = remember { mutableStateOf("") }
    val sessionManager = mainViewModel.mSessionManager
    mainViewModel.historyStoryComplete.observeForever() {
        if (it) {
            val historyStory = sessionManager.getHistoryStory()
            if (historyStory != null) {
                historyContent.value = historyStory.detail
            }
        }
    }
    Text(
        modifier = Modifier.fillMaxWidth().padding(bottom = 60.dp),
        text = historyContent.value,
        style = androidx.compose.ui.text.TextStyle(
            fontSize = 25.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    )
}

@Composable
@Preview()
fun PreviewHistoryCard() {
    Card(
        modifier = Modifier.size(290.dp, 150.dp), colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.white).copy(alpha = 1f),
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column {
                Spacer(
                    modifier = Modifier
                        .size(10.dp)
                        .weight(0.1f)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.8f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(modifier = Modifier.weight(0.25f)) {
                        AutoSizedText(
                            text = stringResource(id = R.string.history_today), size = 30
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .size(10.dp)
                            .weight(0.01f)
                    )
                    Box(modifier = Modifier.weight(0.2f)) {
                        AutoSizedText(
                            text = "2021/10/10", size = 20
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .size(20.dp)
                            .weight(0.01f)
                    )
                    val historyEvent = remember { mutableStateOf("媽媽跟小陳說不要指月亮") }
                    Box(modifier = Modifier.weight(0.25f)) {//historyTitle.value
                        AutoSizedText(
                            text = if (historyEvent.value.length > 10) {
                                historyEvent.value.substring(0, 10) + "..."
                            } else {
                                historyEvent.value
                            }
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.2f),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Bottom
                ) {
                    FixedSizeText(
                        text = "點擊閱讀更多...",
                        size = 50.dp,
                        color = colorResource(id = R.color.black).copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                }
                Spacer(
                    modifier = Modifier
                        .size(5.dp)
                        .weight(0.01f)
                )
            }
        }
    }
}