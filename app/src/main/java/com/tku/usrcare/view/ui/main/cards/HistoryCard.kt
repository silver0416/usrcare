package com.tku.usrcare.view.ui.main.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tku.usrcare.R
import com.tku.usrcare.view.component.AutoSizedText
import com.tku.usrcare.view.component.FixedSizeText
import com.tku.usrcare.viewmodel.MainViewModel


@Composable
fun HistoryCard(mainViewModel: MainViewModel) {
    val sessionManager = mainViewModel.mSessionManager
    val historyEvent = remember { mutableStateOf("") }
    val historyDate = remember { mutableStateOf("") }
    val historyContent = remember { mutableStateOf("") }
    val historyTitle = remember { mutableStateOf("") }

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
        modifier = Modifier.fillMaxSize(), colors = CardDefaults.cardColors(
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
                        .weight(0.9f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(modifier = Modifier.weight(0.25f)) {
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
                    Box(modifier = Modifier.weight(0.25f)) {
                        //historyEvent.value
                        fun Char.isHalfWidth(): Boolean {
                            // 半形字元範圍
                            return this in '\u0020'..'\u007E' ||
                                    this in '\uFF61'..'\uFFDC' ||
                                    this in '\uFFE8'..'\uFFEE'
                        }

                        val totalLength = historyEvent.value.length
                        val halfLength = historyEvent.value.filter { it.isHalfWidth() }.length
                        AutoSizedText(
                            text = if ((totalLength + halfLength) * 2 > 20) {
                                val frontHalfLength = historyEvent.value.substring(0, 10)
                                    .filter { it.isHalfWidth() }.length
                                val frontTotalLength = historyEvent.value.substring(
                                    0,
                                    10 + frontHalfLength / 2
                                ).length
                                historyEvent.value.substring(0, frontTotalLength) + "..."
                            } else {
                                historyEvent.value
                            }, size = 24, fontWeight = FontWeight.Bold
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.15f),
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
                        .weight(0.9f),
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
                        .weight(0.15f),
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