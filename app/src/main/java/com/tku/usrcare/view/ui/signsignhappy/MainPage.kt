package com.tku.usrcare.view.ui.signsignhappy

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tku.usrcare.R
import com.tku.usrcare.view.findActivity
import io.github.boguszpawlowski.composecalendar.StaticCalendar
import io.github.boguszpawlowski.composecalendar.day.DayState
import io.github.boguszpawlowski.composecalendar.selection.EmptySelectionState


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SignSignHappyMain() {
    Box(
        modifier = androidx.compose.ui.Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.bgSignSignHappy))
    ) {
        TitleBox()
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            CalendarBox()
        }
    }
}

@Composable
fun TitleBox() {
    val context = LocalContext.current
    val activity = context.findActivity()
    Box(
        modifier = Modifier.fillMaxSize(),
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
                        color = colorResource(id = R.color.btnSignsignhappyColor),
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
                        painter = painterResource(id = R.drawable.ic_signsignhappy),
                        contentDescription = null,
                        modifier = Modifier
                            .size(62.dp),
                        tint = colorResource(id = R.color.btnSignsignhappyColor)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.sign_sign_happy),
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyDay(dayState: DayState<EmptySelectionState>) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(50.dp)
                .border(
                    0.5.dp, colorResource(id = R.color.bdCalendar),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(dayState.date.dayOfMonth.toString())
        }
    }
}

@Composable
fun MyWeek(weekList: List<String>) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
        for (week in weekList) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = week,
                    color = Color.Black,
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 20.sp,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    ),
                )
            }
        }
    }
}

@Composable
fun MyYearMonth(year: String, month: String) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = year,
            color = Color.Black,
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 20.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            ),
            modifier = Modifier.padding(0.dp, 5.dp, 0.dp, 0.dp)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = month,
            color = Color.Black,
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 20.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            ),
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarBox() {
    Box(
        modifier = Modifier
            .width(350.dp)
            .wrapContentHeight()
            .shadow(15.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
    ) {
        CalendarView()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarView() {
    Box(
        modifier = androidx.compose.ui.Modifier
            .padding(20.dp, 20.dp, 20.dp, 30.dp)
            .background(Color.White)
    ) {
        StaticCalendar(
            monthContainer = {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, colorResource(id = R.color.bdCalendar))) {
                    it(PaddingValues(0.dp))
                }
            },
            dayContent = {
                MyDay(it)
            },
            daysOfWeekHeader = {
                val weekList = listOf("日", "一", "二", "三", "四", "五", "六")
                MyWeek(weekList)
            },
            monthHeader = {
                val chineseMonth = listOf(
                    "一月", "二月", "三月", "四月", "五月", "六月",
                    "七月", "八月", "九月", "十月", "十一月", "十二月"
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .padding(10.dp, 0.dp, 0.dp, 0.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MyYearMonth(
                        it.currentMonth.year.toString(),
                        chineseMonth[it.currentMonth.monthValue - 1]
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth(),
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun CalendarViewPreview() {
    SignSignHappyMain()
}