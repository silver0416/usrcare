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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tku.usrcare.R
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.component.TitleBox
import io.github.boguszpawlowski.composecalendar.StaticCalendar
import io.github.boguszpawlowski.composecalendar.day.DayState
import io.github.boguszpawlowski.composecalendar.header.MonthState
import io.github.boguszpawlowski.composecalendar.selection.EmptySelectionState
import java.text.SimpleDateFormat
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SignSignHappyMain() {
    Box(
        modifier = androidx.compose.ui.Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.bgSignSignHappy))
    ) {
        TitleBox(
            color = colorResource(id = R.color.btnSignsignhappyColor),
            title = stringResource(id = R.string.sign_sign_happy),
            icon = painterResource(id = R.drawable.ic_signsignhappy)
        )
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            CalendarBox()
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyDay(dayState: DayState<EmptySelectionState>) {
    val context = LocalContext.current
    val sessionManager = SessionManager(context)
    val timeFormat = SimpleDateFormat("yyyy-MM-dd", Locale.TAIWAN)
    val today = timeFormat.format(System.currentTimeMillis())
    var thisDay = "${dayState.date.year}-${dayState.date.monthValue}-${dayState.date.dayOfMonth}"
    //個位數補0
    val thisDayList = thisDay.split("-")
    val thisDayList2 = mutableListOf<String>()
    for (i in thisDayList) {
        if (i.length == 1) {
            thisDayList2.add("0$i")
        } else {
            thisDayList2.add(i)
        }
    }
    thisDay = thisDayList2.joinToString("-")
    val signedDateTimeList = sessionManager.getSignedDateTime()
    var isSigned by remember { mutableStateOf(false) }
    for (i in signedDateTimeList) {
        //將i轉換成日期格式
        val signedDateTime = timeFormat.parse(i[0])
        val signedDate = signedDateTime?.let { timeFormat.format(it) }
        if (signedDate.toString() == thisDay) {
            isSigned = true
        }
    }

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(50.dp)
                .border(
                    0.5.dp, colorResource(id = R.color.bdCalendar),
                )
                .background(
                    if (isSigned) {
                        colorResource(id = R.color.red)
                    } else if (dayState.isFromCurrentMonth) {
                        colorResource(id = R.color.white)
                    } else {
                        colorResource(id = R.color.bgCalendarOtherMonth)
                    }
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                dayState.date.dayOfMonth.toString(), style = androidx.compose.ui.text.TextStyle(
                ), color = if (isSigned) {
                    colorResource(id = R.color.white)
                } else if (dayState.isFromCurrentMonth) {
                    colorResource(id = R.color.black)
                } else {
                    colorResource(id = R.color.gray)
                }
            )
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyYearMonth(year: String, month: String , monthState: MonthState) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = year,
            color = Color.Black,
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 20.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            ),
            modifier = Modifier.padding(0.dp, 2.dp, 0.dp, 0.dp)
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { monthState.currentMonth = monthState.currentMonth.minusMonths(1) }
            ){
                Icon(
                    Icons.Default.KeyboardArrowLeft,
                    contentDescription = null,
                )
            }
            Spacer(modifier = Modifier.width(5.dp))
            IconButton(
                onClick = { monthState.currentMonth = monthState.currentMonth.plusMonths(1) }
            ){
                Icon(
                    Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                )
            }
        }
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, colorResource(id = R.color.bdCalendar))
                ) {
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
                        chineseMonth[it.currentMonth.monthValue - 1],
                        it
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