package com.tku.usrcare.view.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.tku.usrcare.R
import com.tku.usrcare.view.findActivity

@Composable
fun TitleBox(color: Color, title: String, icon: Painter) {
    val context = LocalContext.current
    val activity = context.findActivity()
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter,
    ) {
        Row(
            modifier = Modifier.padding(top = 50.dp, start = 20.dp, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    activity?.finish()
                },
                modifier = Modifier
                    .size(43.dp)
                    .clip(CircleShape),
                colors = ButtonDefaults.buttonColors(Color.White),
                contentPadding = PaddingValues(1.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(20.dp),
                    tint = colorResource(id = R.color.black)
                )
            }
            Spacer(modifier = Modifier.width(15.dp))
            Card(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .shadow(15.dp, RoundedCornerShape(16.dp))
                    .border(
                        width = 3.dp,
                        color = color,
                        shape = RoundedCornerShape(15.dp)
                    ),
                colors = CardDefaults.cardColors(colorResource(id = R.color.white))
            ) {
                Row(
                    modifier = Modifier
                        .width(240.dp)
                        .padding(10.dp, 10.dp, 10.dp, 10.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(7.dp))
                    Icon(
                        painter = icon,
                        contentDescription = null,
                        modifier = Modifier
                            .size(52.dp),
                        tint = color
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = title,
                            fontSize = with(LocalDensity.current) { 29.dp.toSp() },
                            color = colorResource(id = R.color.black),
                        )
                    }
                    Spacer(Modifier.weight(1f))
                }
            }
        }

    }
}