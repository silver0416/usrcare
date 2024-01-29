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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tku.usrcare.R
import com.tku.usrcare.view.component.AutoSizedText
import com.tku.usrcare.view.component.FixedSizeText
import com.tku.usrcare.viewmodel.MainViewModel

@Composable
fun DailyEnglishCard(
    mainViewModel: MainViewModel,
    showContent: Boolean = false,
    isExpanded: MutableState<Boolean>,
    clickedCard: MutableState<Int>
) {

    val english = remember { mutableStateOf("Loading...") }
    val phoneticNotation = remember { mutableStateOf("/ˈloʊdɪŋ/") }
    val sessionManager = mainViewModel.mSessionManager
    val noRippleInteractionSource = remember { MutableInteractionSource() }
    mainViewModel.vocabularyComplete.observeForever {
        if (it) {
            val vocabulary = sessionManager.getVocabulary()
            english.value = vocabulary?.english.toString()
            phoneticNotation.value = vocabulary?.phoneticNotation.toString()
        }
    }
    Card(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = noRippleInteractionSource,
                indication = null
            ) {
                clickedCard.value = 1
                isExpanded.value = !isExpanded.value
            }, colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.white).copy(alpha = 1f),
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (!showContent) {
                Spacer(modifier = Modifier
                    .size(15.dp)
                    .weight(0.1f))
                Box(modifier = Modifier.weight(0.3f), contentAlignment = Alignment.Center)
                {
                    AutoSizedText(text = stringResource(id = R.string.daily_english), size = 30)
                }
            }
            Spacer(
                modifier = Modifier
                    .size(4.dp)
                    .weight(0.01f)
            )
            Column(modifier = Modifier.weight(0.5f), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier.weight(0.6f), contentAlignment = Alignment.Center
                )
                {
                    AutoSizedText(text = english.value, size = 30)
                }
                Spacer(
                    modifier = Modifier
                        .size(4.dp)
                        .weight(0.01f)
                )
                Box(
                    modifier = Modifier.weight(0.4f), contentAlignment = Alignment.Center
                )
                {
                    AutoSizedText(text = phoneticNotation.value, size = 20)
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
                        text = "點擊查看中文...",
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

@Composable
fun DailyEnglishCardContent(mainViewModel: MainViewModel) {
    val chinese = remember { mutableStateOf("載入中...") }
    val sessionManager = mainViewModel.mSessionManager
    mainViewModel.vocabularyComplete.observeForever {
        if (it) {
            val vocabulary = sessionManager.getVocabulary()
            chinese.value = vocabulary?.chinese.toString()
        }
    }
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = chinese.value,
        style = TextStyle(
            fontSize = 28.sp,
            textAlign = TextAlign.Center
        )
    )
}