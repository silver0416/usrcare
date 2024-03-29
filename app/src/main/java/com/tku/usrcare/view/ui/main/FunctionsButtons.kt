package com.tku.usrcare.view.ui.main

import android.content.Intent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tku.usrcare.R
import com.tku.usrcare.view.ClockActivity
import com.tku.usrcare.view.GameLobyActivity
import com.tku.usrcare.view.KtvActivity
import com.tku.usrcare.view.MainActivity
import com.tku.usrcare.view.PetCompanyActivity
import com.tku.usrcare.view.ScaleActivity
import com.tku.usrcare.view.SignSignHappyActivity
import com.tku.usrcare.view.SportsActivity
import com.tku.usrcare.view.component.AutoSizedText
import com.tku.usrcare.view.component.findActivity


@Composable
fun FunctionButtons() {
    data class FunctionsItem(
        val title: String,
        val icon: Int,
        val route: Class<*>,
        val color: Color,
        val enabled: Boolean = true
    )

    val functionsList = listOf(
        FunctionsItem(
            stringResource(id = R.string.sign_sign_happy),
            R.drawable.ic_signsignhappy,
            SignSignHappyActivity::class.java,
            colorResource(id = R.color.btnSignsignhappyColor)
        ),
        FunctionsItem(
            stringResource(id = R.string.brain_game),
            R.drawable.ic_game,
            GameLobyActivity::class.java,
            colorResource(id = R.color.btnBrainGameColor)
        ),
        FunctionsItem(
            stringResource(id = R.string.AI_vitality_detection),
            R.drawable.ic_aivitalitydetection,
            SportsActivity::class.java,
            colorResource(id = R.color.btnAiVitalityDetection)
        ),
        FunctionsItem(
            stringResource(id = R.string.saturday_KTV),
            R.drawable.ic_ktv,
            KtvActivity::class.java,
            colorResource(id = R.color.btnSatKTVColor)
        ),
        FunctionsItem(
            stringResource(id = R.string.daily_task),
            R.drawable.ic_dailytask,
            MainActivity::class.java,
            colorResource(id = R.color.btnDailyTaskColor),
            false
        ),
        FunctionsItem(
            stringResource(id = R.string.pet_company),
            R.drawable.ic_petcompany,
            PetCompanyActivity::class.java,
            colorResource(id = R.color.btnPetcompanyColor),
        ),
        FunctionsItem(
            stringResource(id = R.string.clock_reminder),
            R.drawable.ic_clocknotice,
            ClockActivity::class.java,
            colorResource(id = R.color.btnClockColor)
        ),
        FunctionsItem(
            stringResource(id = R.string.mood_scale),
            R.drawable.ic_moodscale,
            ScaleActivity::class.java,
            colorResource(id = R.color.btnMoodScaleColor)
        ),
    )
    val context = LocalContext.current

    @Composable
    fun SingleLineButton(functionsItem: FunctionsItem) {
        Button(
            onClick = {
                if (functionsItem.enabled)
                    context.findActivity()?.startActivity(
                        Intent(
                            context, functionsItem.route
                        )
                    )
            },
            modifier = Modifier
                .size(148.dp, 50.dp)
                .border(
                    width = 3.dp,
                    color = if (functionsItem.enabled) functionsItem.color else Color.Black,
                    shape = RoundedCornerShape(15.dp)
                ),
            shape = RoundedCornerShape(15.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (functionsItem.enabled) Color.White else colorResource(id = R.color.gray)
            )
        ) {
            Row(
                Modifier
                    .fillMaxSize()
                    .padding(start = 8.dp, end = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(id = functionsItem.icon),
                    contentDescription = functionsItem.title,
                    tint = if (functionsItem.enabled) functionsItem.color else Color.Black,
                )
                Box(modifier = Modifier.padding(start = 8.dp)) {
                    AutoSizedText(
                        text = functionsItem.title,
                        size = 30,
                        color = Color.Black
                    )
                }
            }
        }
    }

    @Composable
    fun DoubleLineButton(functionsItem: FunctionsItem) {
        Button(
            onClick = {
                if (functionsItem.enabled)
                    context.findActivity()?.startActivity(
                        Intent(
                            context, functionsItem.route
                        )
                    )
            },
            modifier = Modifier
                .size(150.dp, 118.dp)
                .border(
                    width = 3.dp, color = if (functionsItem.enabled) functionsItem.color else Color.Black, shape = RoundedCornerShape(15.dp)
                ),
            shape = RoundedCornerShape(15.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (functionsItem.enabled) Color.White else colorResource(id = R.color.gray)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = functionsItem.icon),
                    contentDescription = functionsItem.title,
                    modifier = Modifier
                        .size(60.dp)
                        .weight(0.5f),
                    tint = if (functionsItem.enabled) functionsItem.color else Color.Black,
                )
                Spacer(
                    modifier = Modifier
                        .size(5.dp)
                        .weight(0.01f)
                )
                AutoSizedText(
                    text = functionsItem.title,
                    size = 30,
                    color = Color.Black
                )
            }
        }
    }

    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(0.5f), // 給這個Column設置權重為1
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.weight(0.25f)) {
                SingleLineButton(functionsList[0])
            }
            Spacer(
                modifier = Modifier
                    .size(8.dp)
                    .weight(0.015f)
            )
            Box(modifier = Modifier.weight(0.5f)) {
                DoubleLineButton(functionsList[1])
            }
            Spacer(
                modifier = Modifier
                    .size(8.dp)
                    .weight(0.01f)
            )
            Box(modifier = Modifier.weight(0.5f)) {
                DoubleLineButton(functionsList[2])
            }
            Spacer(
                modifier = Modifier
                    .size(8.dp)
                    .weight(0.01f)
            )
            Box(modifier = Modifier.weight(0.5f)) {
                DoubleLineButton(functionsList[3])
            }
        }
        Spacer(
            modifier = Modifier
                .size(3.dp)
                .weight(0.01f)
        )
        Column(
            modifier = Modifier.weight(0.5f), // 給這個Column設置權重為1
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.weight(0.25f)) {
                SingleLineButton(functionsList[4])
            }
            Spacer(
                modifier = Modifier
                    .size(8.dp)
                    .weight(0.015f)
            )
            Box(modifier = Modifier.weight(0.5f)) {
                DoubleLineButton(functionsList[5])
            }
            Spacer(
                modifier = Modifier
                    .size(8.dp)
                    .weight(0.01f)
            )
            Box(modifier = Modifier.weight(0.5f)) {
                DoubleLineButton(functionsList[6])
            }
            Spacer(
                modifier = Modifier
                    .size(8.dp)
                    .weight(0.01f)
            )
            Box(modifier = Modifier.weight(0.5f)) {
                DoubleLineButton(functionsList[7])
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFunctionButtons() {
    FunctionButtons()
}