package com.tku.usrcare.view.ui.main.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tku.usrcare.R
import com.tku.usrcare.view.component.AutoSizedText

@Composable
fun HistoryCard() {
    Card(
        modifier = Modifier.fillMaxSize(), colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.white).copy(alpha = 1f),
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AutoSizedText(text = stringResource(id = R.string.history_today), size = 35)
            Spacer(modifier = Modifier.size(10.dp))
            AutoSizedText(text = "敬請期待", size = 20)
        }
    }
}