package com.tku.usrcare.view.ui.signsignhappy

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.tku.usrcare.view.component.AutoSizedText
import com.tku.usrcare.view.component.TitleBox
import io.github.boguszpawlowski.composecalendar.StaticCalendar
import io.github.boguszpawlowski.composecalendar.day.DayState
import io.github.boguszpawlowski.composecalendar.header.MonthState
import io.github.boguszpawlowski.composecalendar.selection.EmptySelectionState
import java.time.LocalDate
import java.time.temporal.ChronoUnit


// 函數計算連續簽到天數
fun countConsecutiveSignIns(dates: List<String>, endDate: String): Int {
    // 將日期字串轉換為 LocalDate 類別
    val parsedDates = dates.map { LocalDate.parse(it) }.sorted()

    // 將結束日期字串轉換為 LocalDate，且統一格式為 yyyy-MM-dd
    var endLocalDate = LocalDate.parse(endDate)

    // 初始連續簽到天數為 1
    var consecutiveDays = 1

    // 從最後一個日期開始往回計算
    for (date in parsedDates.asReversed()) {
        if (date != endLocalDate) { //第一天不算
            // 如果日期連續（相差一天），增加連續簽到天數
            if (ChronoUnit.DAYS.between(date, endLocalDate) == 1.toLong()) {  //當相差一天
                consecutiveDays++
            } else {
                // 如果日期不連續(不是差一天)，終止循環
                break
            }
            // 更新下一次迭代的結束日期為當前日期
            endLocalDate = date
        }
    }
    return consecutiveDays
}

@Composable
fun SignSignHappyMain() {
    val sessionManager = SessionManager(LocalContext.current)
    val signedDateTimeList = sessionManager.getSignedDateTime()
    val signedDateSet = mutableSetOf<String>()
    for (signedDateTime in signedDateTimeList) {
        signedDateSet.add(signedDateTime[0]) // Set 會自動處理重複問題
    }
    Box(
        modifier = androidx.compose.ui.Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.bgSignSignHappy))
    ) {
        Column {
            Box(modifier = Modifier.weight(0.2f)){
                TitleBox(
                    color = colorResource(id = R.color.btnSignsignhappyColor),
                    title = stringResource(id = R.string.sign_sign_happy),
                    icon = painterResource(id = R.drawable.ic_signsignhappy)
                )
            }
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    CalendarBox(signedDateSet)
                }
                item {
                    Spacer(modifier = Modifier.height(40.dp))
                }
                item {
                    ContinueSignInBox(signedDateSet)
                }
                item {
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}

@Composable
fun ContinueSignInBox(signedDateSet: Set<String>) {
    val today = LocalDate.now().toString()
    Box(
        modifier = Modifier
            .width(350.dp)
            .wrapContentHeight()
            .border(
                3.dp, colorResource(id = R.color.btnSignsignhappyColor), RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White),
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)) {
            val consecutiveDays = countConsecutiveSignIns(signedDateSet.toList(), today)
            Row(
                modifier = Modifier
                    .padding(20.dp, 20.dp, 20.dp, 30.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                AutoSizedText(
                    text = if (consecutiveDays > 1) "已連續簽到${consecutiveDays}天" else "連續簽到有加碼代幣!",
                    size = 100,
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(id = R.drawable.ic_congratulations_a),
                    contentDescription = null,
                    modifier = Modifier
                        .weight(0.3f)
                        .padding(end = 10.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_coin), contentDescription = null,
                    modifier = Modifier.weight(0.3f)
                )
                Box(modifier = Modifier.weight(0.3f)){ AutoSizedText(text = "+???", size = 100) }
                Image(
                    painter = painterResource(id = R.drawable.ic_congratulations_b),
                    contentDescription = null,
                    modifier = Modifier
                        .weight(0.3f)
                        .padding(start = 10.dp)
                )
            }
        }
    }
}

@Composable
fun MyDay(
    dayState: DayState<EmptySelectionState>, signedDateSet: Set<String>
) {
    // 將 dayState 的日期部分建構成 LocalDate 對象。
    val thisDate = remember(dayState.date) {
        dayState.date.run {
            LocalDate.of(year, monthValue, dayOfMonth)
        }
    }

    // 緩存日期的字符串表示形式，避免重複計算。
    val thisDateString = remember(thisDate) {
        thisDate.toString()
    }

    // 判斷當日是否已簽到。
    val isSigned = thisDateString in signedDateSet
    val boxBackgroundColor = when {
        isSigned -> colorResource(id = R.color.red)
        dayState.isFromCurrentMonth -> colorResource(id = R.color.white)
        else -> colorResource(id = R.color.bgCalendarOtherMonth)
    }
    val textColor = when {
        isSigned -> colorResource(id = R.color.white)
        dayState.isFromCurrentMonth -> colorResource(id = R.color.black)
        else -> colorResource(id = R.color.gray)
    }

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(50.dp)
                .border(0.5.dp, colorResource(id = R.color.bdCalendar))
                .background(boxBackgroundColor),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = dayState.date.dayOfMonth.toString(), color = textColor
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
                        fontSize = 20.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    ),
                )
            }
        }
    }
}

@Composable
fun MyYearMonth(year: String, month: String, monthState: MonthState) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = year, color = Color.Black, style = androidx.compose.ui.text.TextStyle(
                fontSize = 20.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            ), modifier = Modifier.padding(0.dp, 2.dp, 0.dp, 0.dp)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = month,
            color = Color.Black,
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 20.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            ),
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                monthState.currentMonth = monthState.currentMonth.minusMonths(1)
            }) {
                Icon(
                    Icons.Default.KeyboardArrowLeft,
                    contentDescription = null,
                )
            }
            Spacer(modifier = Modifier.width(5.dp))
            IconButton(onClick = {
                monthState.currentMonth = monthState.currentMonth.plusMonths(1)
            }) {
                Icon(
                    Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                )
            }
        }
    }
}

@Composable
fun CalendarBox(signedDateSet: Set<String> = setOf()) {

    Box(
        modifier = Modifier
            .width(350.dp)
            .wrapContentHeight()
            .shadow(15.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
    ) {
        CalendarView(signedDateSet = signedDateSet)
    }
}

@Composable
fun CalendarView(signedDateSet: Set<String> = setOf()) {
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
                MyDay(it, signedDateSet)
            },
            daysOfWeekHeader = {
                val weekList = listOf("日", "一", "二", "三", "四", "五", "六")
                MyWeek(weekList)
            },
            monthHeader = {
                val chineseMonth = listOf(
                    "一月",
                    "二月",
                    "三月",
                    "四月",
                    "五月",
                    "六月",
                    "七月",
                    "八月",
                    "九月",
                    "十月",
                    "十一月",
                    "十二月"
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
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CalendarViewPreview() {
    SignSignHappyMain()
}