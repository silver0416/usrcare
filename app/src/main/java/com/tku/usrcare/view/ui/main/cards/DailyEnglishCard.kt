package com.tku.usrcare.view.ui.main.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tku.usrcare.R
import com.tku.usrcare.view.component.AutoSizedText

@Composable
fun DailyEnglishCard() {
    Card(
        modifier = Modifier.fillMaxSize(), colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.white).copy(alpha = 1f),
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AutoSizedText(text = stringResource(id = R.string.daily_english), size = 35)
            Spacer(modifier = Modifier.size(10.dp))
            AutoSizedText(text = "fingers crossed!", size = 20)
            Spacer(modifier = Modifier.size(4.dp))
            AutoSizedText(text = "希望一切順利！", size = 20)
        }
    }
}

@Composable
@Preview
fun DailyEnglishCardPreview() {
    DailyEnglishCard()
}