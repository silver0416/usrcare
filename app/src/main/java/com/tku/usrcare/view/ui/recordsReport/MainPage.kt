package com.tku.usrcare.view.ui.recordsReport

import android.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import android.util.Log
import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontVariation.weight
import androidx.compose.ui.text.font.FontVariation.width
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter.State.Empty.painter
import com.github.anastr.speedometer.SpeedView
import com.github.anastr.speedometer.components.Section
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.color.utilities.Score.score
import com.google.common.math.Quantiles.scale
import com.tku.usrcare.R
import com.tku.usrcare.api.ApiUSR
import com.tku.usrcare.model.AD8
import com.tku.usrcare.model.LonelinessScale
import com.tku.usrcare.model.abilities
import com.tku.usrcare.model.checkin
import com.tku.usrcare.model.exercise
import com.tku.usrcare.model.game
import com.tku.usrcare.model.healthReport
import com.tku.usrcare.model.mental_record
import com.tku.usrcare.model.pet_companion
import com.tku.usrcare.model.weekly_steps
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.component.AutoSizedText
import com.tku.usrcare.view.component.FixedSizeText
import kotlinx.collections.immutable.immutableListOf
import kotlinx.coroutines.delay
import kotlin.math.round

@Composable
fun MainPage() {
    val context = LocalContext.current
    var healthReport by remember {
        mutableStateOf(
            healthReport(
                checkin = checkin(
                    average_mood_score = 0.0f,
                    feedback = emptyList(),
                    mood_difference_from_last_period = 0.0f,
                    mood_trend = ""
                ),
                exercise = exercise(
                    average_exercise_score = 0.0f,
                    exercise_level = "",
                    feedback = "[]",
                ),
                mental_record = mental_record(
                    AD8 = AD8(
                        average_score = 0,
                        feedback = "[]",
                    ),
                    LonelinessScale = LonelinessScale(
                        average_score = 0.0f,
                        feedback = "[]",
                        mood_level = "",
                    ),
                ),
                pet_companion = pet_companion(
                    total_steps = 0,
                    average_daily_steps = 0,
                    goal_achievement_dates = listOf("12-31"),
                    goal_achievement_days = 0,
                    goal_achievement_rate = 0.0f,
                    daily_step_goal = 0,
                    quarterly_step_goal = 0,
                    step_difference_from_last_period = 0,
                    step_trend = "",
                    weekly_steps = (0..13).associate { index ->
                        index.toString() to weekly_steps(
                            total_steps = index * 100,
                            week_end = "2024-10-${7 + index * 7}",
                            week_start = "2024-10-${1 + index * 7}"
                        )
                    }.toMutableMap(),
                    feedback = "[]",
                ),
                game = abilities(
                    記憶力 = 0.0f,
                    反應力 = 0.0f,
                    邏輯推理能力 = 0.0f,
                    手眼協調與專注力 = 0.0f,
                    持續力 = 0.0f,
                ),
                reportPeriod = "1",
            )
        )
    }
    ApiUSR.getHealthReport(
        SessionManager(context).getUserToken().toString(),
        onSuccess = {
            try {
                healthReport = it
            } catch (e: Exception) {
                Log.d("MainPage", "healthReport_catch: $e")
            }
        },
        onError = {
            Log.d("MainPage", "healthReport_Error: Error")
        },
        onInternetError = {
            Log.d("MainPage", "healthReport_Internet: InternetError")
        }
    )
    data class Items(
        val title: String,
        val icon: Painter,
        val color: androidx.compose.ui.graphics.Color,
        //val graph: Unit,
        val enabled: Boolean = true
    )

    val scaleList = listOf(
        Items(
            stringResource(id = R.string.sign_sign_happy),
            painterResource(id = R.drawable.ic_signsignhappy),
            colorResource(id = R.color.btnSignsignhappyColor),
        ),
        /*Items(
            stringResource(id = R.string.daily_task),
            painterResource(id = R.drawable.ic_dailytask),
            colorResource(id = R.color.btnDailyTaskColor),
            false
        ),*/
        Items(
            stringResource(id = R.string.brain_game),
            painterResource(id = R.drawable.ic_game),
            colorResource(id = R.color.btnBrainGameColor),
        ),
        Items(
            stringResource(id = R.string.pet_company),
            painterResource(id = R.drawable.ic_petcompany),
            colorResource(id = R.color.btnPetcompanyColor),
        ),
        Items(
            stringResource(id = R.string.AI_vitality_detection),
            painterResource(id = R.drawable.ic_aivitalitydetection),
            colorResource(id = R.color.btnAiVitalityDetection),
        ),
        Items(
            stringResource(id = R.string.mood_scale),
            painterResource(id = R.drawable.ic_moodscale),
            colorResource(id = R.color.btnMoodScaleColor),
        )
    )


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        marqueeText(
            text =
            try {
                if (healthReport.reportPeriod != null) {
                    "長情永樂健康單-" + healthReport.reportPeriod.substring(
                        0,
                        4
                    ) + "第" + healthReport.reportPeriod.last().toString() + "期"
                } else {
                    "長情永樂健康單"
                }
            } catch (e: Exception) {
                "長情永樂健康單"
            },
            size = 70,
            marqueeSize = 400.0f
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp, bottom = 10.dp, start = 20.dp, end = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            items(scaleList) { item ->
                titleBar(color = item.color, title = item.title, icon = item.icon)
                when (item.title) {
                    "簽簽樂" -> {
                        score(
                            score = healthReport.checkin.average_mood_score,
                            differenceFromLastPeriod = healthReport.checkin.mood_difference_from_last_period,
                            trend = healthReport.checkin.mood_trend,
                            feedback = "",
                            feedback2 = healthReport.checkin.feedback,
                            color = colorResource(id = R.color.bgSignSignHappy),
                            size = 240,
                        )
                    }

                    "寵物陪伴" -> {
                        FixedSizeText(
                            text = "每週行走步數",
                            size = 50.dp,
                            color = colorResource(id = R.color.black),
                        )
                        //長條圖
                        barChart(weeklySteps = healthReport.pet_companion.weekly_steps)
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            )
                            {
                                //圓餅圖
                                PieChartView(
                                    size = 120,
                                    achievement_rate = healthReport.pet_companion.goal_achievement_rate
                                )
                                score(
                                    score = healthReport.pet_companion.total_steps.toFloat(),
                                    differenceFromLastPeriod = healthReport.pet_companion.step_difference_from_last_period.toFloat(),
                                    trend = healthReport.pet_companion.step_trend,
                                    feedback = healthReport.pet_companion.feedback,
                                    feedback2 = emptyList(),
                                    color = colorResource(id = R.color.bgPetCompany),
                                    size = 90,
                                    needFeedback = false,
                                    isScoreInt = true
                                )
                            }
                            feedBack(
                                feedBack = listOf(healthReport.pet_companion.feedback),
                                feedBackSize = listOf(healthReport.pet_companion.feedback).size,
                                color = colorResource(id = R.color.bgPetCompany)
                            )
                        }
                    }

                    "心情量表" -> {

                        scoreTwo(
                            scoreAD8 = healthReport.mental_record.AD8.average_score.toFloat(),
                            scoreLone = healthReport.mental_record.LonelinessScale.average_score.toFloat(),
                            feedbackAD8 = healthReport.mental_record.AD8.feedback,
                            feedbackLone = healthReport.mental_record.LonelinessScale.feedback,
                            color = colorResource(id = R.color.bgScale)
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                    }

                    "愛來運動" -> {
                        gasGraph(
                            score = healthReport.exercise.average_exercise_score,
                            feedback = healthReport.exercise.feedback,
                            level = healthReport.exercise.exercise_level,
                            color = colorResource(id = R.color.bgSports)
                        )
                    }

                    "動腦小遊戲" -> {
                        //雷達圖
                        RadarChart()
                    }
                }
            }
        }
    }


}

@Composable
fun gasGraph(
    score: Float = 0.0f,
    feedback: String = "",
    color: androidx.compose.ui.graphics.Color,
    level: String
) {
    Column(
        modifier = Modifier.padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    )
    {
        SpeedView(
            modifier = Modifier.size(250.dp),
            speed = score,
            sections = immutableListOf(
                Section(0f, 30f / 100f, androidx.compose.ui.graphics.Color.Red),
                Section(30f / 100f, 60f / 100f, androidx.compose.ui.graphics.Color.Yellow),
                Section(60f / 100f, 100f / 100f, androidx.compose.ui.graphics.Color.Green)
            ),

            unit = "分",
            speedText = {
                FixedSizeText(text = String.format("%.0f", score), size = 50.dp)
            },
            unitText = {
                FixedSizeText(text = level, size = 50.dp)
            },
            minSpeed = 0f,
            maxSpeed = 100f,
            unitUnderSpeed = false,
            centerContent = {

            },
        )
        feedBack(feedBack = listOf(feedback), feedBackSize = listOf(feedback).size, color = color)

    }

}

//折線圖
@Composable
fun lineChart() {
    AndroidView(
        factory = { context ->
            LineChart(context).apply {
                // 設置圖表屬性
                description.isEnabled = false
                setDrawGridBackground(false)

                setScaleEnabled(false) // 完全禁用縮放
                setPinchZoom(false) // 禁用雙指縮放

                // X 軸設定
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawGridLines(false)

                // Y 軸設定
                axisRight.isEnabled = false
                axisLeft.setDrawGridLines(false)

                // 填入數據
                val entries = listOf(
                    Entry(0f, 2f),
                    Entry(1f, 4f),
                    Entry(2f, 1f),
                    Entry(3f, 5f),
                    Entry(4f, 3f)
                )
                val dataSet = LineDataSet(entries, "Sample Data").apply {
                    color = Color.BLUE
                    lineWidth = 2f
                    setDrawCircles(true)
                    setDrawValues(false)
                }
                val lineData = LineData(dataSet)
                data = lineData
                invalidate() // 刷新圖表
            }
        }, modifier = Modifier.size(250.dp)
    )
}

//長條圖
@Composable
fun barChart(size: Int = 300, weeklySteps: Map<String, weekly_steps>) {

    AndroidView(factory = { context ->
        BarChart(context).apply {
            // 設置圖表屬性
            description.isEnabled = false
            setDrawGridBackground(false)
            setFitBars(true) // 確保條形圖在顯示區域內

            setScaleEnabled(false) // 完全禁用縮放
            setPinchZoom(false) // 禁用雙指縮放

            // X 軸設定
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)
            xAxis.valueFormatter = IndexAxisValueFormatter(listOf(""))
            xAxis.labelRotationAngle = -30f  // 調整標籤角度以便更好地顯示
            // Y 軸設定
            axisRight.isEnabled = false // 隱藏右側 Y 軸

            // 將週步數資料整理為 BarEntry 列表
            val entries = weeklySteps.entries.mapIndexed { index, entry ->
                BarEntry(index.toFloat(), entry.value.total_steps.toFloat() / 1000)
            }

            val dataSet = BarDataSet(entries, "行走里程(千步/週)").apply {
                color = Color.GREEN
                valueTextColor = Color.BLACK
                valueTextSize = 16f
                setDrawValues(false)
            }

            val barData = BarData(dataSet)
            data = barData
            invalidate() // 刷新圖表
        }
    }, modifier = Modifier.size(size.dp))
}

//雷達圖
@Composable
fun RadarChart(abilities: abilities = abilities(0.0f, 0.0f, 0.0f, 0.0f, 0.0f)) {
    AndroidView(
        factory = { context ->
            RadarChart(context).apply {
                description.isEnabled = false // 隱藏描述
                webColor = Color.LTGRAY // 設定網格線顏色
                webLineWidth = 1f // 設置網格線的寬度
                webColorInner = Color.LTGRAY
                webLineWidthInner = 1f
                webAlpha = 100 // 設定透明度
                isRotationEnabled = false // 禁用旋轉
                legend.isEnabled = false

                // X軸設置 (對應雷達圖的角度)
                xAxis.apply {
                    textColor = Color.BLACK
                    textSize = 12f
                    setDrawLabels(true) // 顯示標籤
                    position = XAxis.XAxisPosition.BOTTOM_INSIDE
                    valueFormatter =
                        IndexAxisValueFormatter(
                            listOf(
                                "反應力",
                                "手眼協調與專注力",
                                "持續力",
                                "記憶力",
                                "邏輯推理能力"
                            )
                        ) // 標籤
                }

                // Y軸設置 (對應雷達圖的半徑方向)
                yAxis.apply {
                    setDrawLabels(false) // 不顯示數值標籤
                    axisMinimum = 0f // 設置最小值
                    axisMaximum = 100f // 設置最大值
                }

                // 範例數據
                val entries = listOf(
                    RadarEntry(abilities.反應力),
                    RadarEntry(abilities.手眼協調與專注力),
                    RadarEntry(abilities.持續力),
                    RadarEntry(abilities.記憶力),
                    RadarEntry(abilities.邏輯推理能力),
                )

                val dataSet = RadarDataSet(entries, "Attributes").apply {
                    valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            return value.toInt().toString()  // 移除小數點，顯示為整數
                        }
                    }

                    color = Color.BLUE // 雷達圖線條顏色
                    fillColor = Color.CYAN // 填充顏色
                    setDrawFilled(true) // 是否填充
                    lineWidth = 2f // 設置線條寬度
                    valueTextSize = 25f//區塊數值的文字大小30f=30sp
                }

                val radarData = RadarData(dataSet)
                legend.textSize = 14f//圖例的文字大小
                data = radarData
                invalidate() // 刷新圖表
            }
        },
        modifier = Modifier.size(300.dp) // 設置雷達圖大小
    )
}

//圓餅圖
@Composable
fun PieChartView(size: Int = 240, achievement_rate: Float = 0.0f) {
    AndroidView(
        factory = { context ->
            PieChart(context).apply {
                description.isEnabled = false// 隱藏描述
                //description.text = "達成率" // 設置描述文字
                setDrawEntryLabels(false) // 隱藏區塊標籤
                //isDrawHoleEnabled = false  // 關閉中間的空心效果
                val entries = listOf(
                    PieEntry(100 - round(achievement_rate), "未達成"),
                    PieEntry(round(achievement_rate), "已達成"),
                    //PieEntry(30f, "反應力")
                )

                val dataSet = PieDataSet(entries, "").apply {
                    //圓餅圖顏色
                    colors = listOf(Color.GRAY, Color.GREEN)//, Color.GREEN
                    //圓餅間隔
                    //sliceSpace = 5f
                    valueTextSize = (size / 12).toFloat()//區塊數值的文字大小30f=30sp
                    valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            return value.toInt().toString() + "%"  // 移除小數點，顯示為整數
                        }
                    }
                }
                data = PieData(dataSet)

                // 設置 Legend 標籤置底居中
                legend.apply {
                    verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM // 垂直置底
                    horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER // 水平置中
                    orientation = Legend.LegendOrientation.HORIZONTAL // 橫向排列
                    setDrawInside(false) // 在圖表外部繪製
                    textSize = (size / 12).toFloat() // 標籤文字大小
                }

                setEntryLabelTextSize((size / 12).toFloat())//區塊標籤的文字大小
                //legend.textSize = (size / 12).toFloat()//圖例的文字大小
                //setEntryLabelColor(Color.BLACK)//區塊標籤的文字顏色
                invalidate()  // 刷新圖表
            }
        },
        modifier = Modifier.size(size.dp)
    )
}

@Composable
fun titleBar(
    color: androidx.compose.ui.graphics.Color,
    title: String,
    icon: Painter,
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter,
    ) {
        Row(
            modifier = Modifier.padding(top = 5.dp, start = 5.dp, end = 5.dp, bottom = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.width(5.dp))
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
                        .width(200.dp)
                        .padding(5.dp, 5.dp, 5.dp, 5.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(5.dp))
                    Icon(
                        painter = icon,
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp),
                        tint = color
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        AutoSizedText(
                            text = title,
                            size = 25,
                            color = colorResource(id = R.color.black),
                        )
                    }
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun score(
    score: Float = 0.0f,
    isScoreInt: Boolean = false,
    differenceFromLastPeriod: Float = 0.0f,
    trend: String = "上升",
    feedback: String,
    feedback2: List<String>,
    color: androidx.compose.ui.graphics.Color,
    size: Int = 240,
    needFeedback: Boolean = true
) {
    Column(
        modifier = Modifier.padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    )
    {
        Row(
            modifier = Modifier
                .height(size.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Box(
                modifier = Modifier
                    .size((size * 5 / 6).dp)
                    .clip(CircleShape)
                    .border(
                        width = 10.dp,
                        color = colorResource(id = R.color.btnAiVitalityDetection),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            )
            {
                AutoSizedText(
                    text = if (isScoreInt) {
                        String.format("%.0f", score)
                    } else {
                        score.toString()
                    },
                    size = size / 4,
                    color = colorResource(id = R.color.black)
                )
            }
            Icon(
                painter = painterResource(id = if (trend.equals("下降") || trend.equals("上升")) R.drawable.ic_back_arr else R.drawable.minus),
                contentDescription = "up or down",
                modifier = Modifier
                    .size((size / 5).dp)
                    .padding(5.dp, 5.dp, 5.dp, 5.dp)
                    .graphicsLayer(
                        rotationZ = if (trend.equals("下降")) 270f else if (trend.equals(
                                "上升"
                            )
                        ) 90f else 0f
                    ),
                tint = colorResource(
                    id = if (trend.equals("下降")) R.color.red else if (trend.equals("上升")) R.color.green else R.color.gray
                )
            )
            AutoSizedText(
                text = if (isScoreInt) {
                    String.format("%.0f", differenceFromLastPeriod)
                } else {
                    differenceFromLastPeriod.toString()
                },
                size = (size / 6),
                color = colorResource(id = R.color.black)
            )
        }
        if (needFeedback) {
            feedBack(
                feedBack = feedback2,
                feedBackSize = feedback2.size,
                size = size,
                color = color
            )
        }
    }
}

@Composable
fun scoreTwo(
    scoreAD8: Float = 0.0f,
    scoreLone: Float = 0.0f,
    isScoreInt: Boolean = false,
    differenceFromLastPeriod: Float = 0.0f,
    feedbackAD8: String? = null,
    feedbackLone: String? = null,
    color: androidx.compose.ui.graphics.Color,
    size: Int = 240,
) {
    Box(
        modifier = Modifier
            //.height(650.dp)
            .width(300.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color),
        contentAlignment = Alignment.Center
    )
    {
        Column(modifier = Modifier.fillMaxSize().padding(10.dp)) {
            //AD8
            Row(modifier = Modifier.fillMaxWidth().padding(5.dp)) {
                Box(
                    modifier = Modifier
                        .height(50.dp)
                        .width(220.dp),
                    contentAlignment = Alignment.Center
                )
                {
                    AutoSizedText(text = "AD8認知功能評估")
                }
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .border(3.dp, colorResource(id = R.color.btnMoodScaleColor)),
                    contentAlignment = Alignment.Center
                )
                {
                    AutoSizedText(text = String.format("%.0f", scoreAD8))
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically // 垂直居中對齊
            )
            {
                Icon(
                    painter = painterResource(id = R.drawable.teacher),
                    contentDescription = "teacher",
                    modifier = Modifier.height(80.dp),
                    //.padding(end = 8.dp, top = 4.dp, bottom = 4.dp)
                    tint = androidx.compose.ui.graphics.Color.Unspecified
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(color)
                        .border(3.dp, colorResource(id = R.color.btnMoodScaleColor),RoundedCornerShape(8.dp))
                        .padding(12.dp)

                )
                {
                    FixedSizeText(
                        text =
                        if (!feedbackAD8.isNullOrEmpty()) {
                            feedbackAD8
                        } else {
                            "無評語"
                        },
                        size = (size / 6).dp,
                        color = colorResource(id = R.color.black)
                    )
                }
            }
            Divider(
                color = androidx.compose.ui.graphics.Color.Black,
                thickness = 3.dp, // 分隔線的厚度
                modifier = Modifier.padding(vertical = 8.dp) // 分隔線的間距
            )
            //lone
            Row(modifier = Modifier.fillMaxWidth().padding(5.dp)) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .border(3.dp, colorResource(id = R.color.btnMoodScaleColor)),
                    contentAlignment = Alignment.Center
                )
                {
                    AutoSizedText(text = String.format("%.0f", scoreLone))
                }
                Box(
                    modifier = Modifier
                        .height(50.dp)
                        .width(220.dp),
                    contentAlignment = Alignment.Center
                )
                {
                    AutoSizedText(text = "寂寞量表")
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically // 垂直居中對齊
            )
            {
                Box(
                    modifier = Modifier
                        .weight(1f, fill = true)
                        .padding(5.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(color)
                        .border(3.dp, colorResource(id = R.color.btnMoodScaleColor),RoundedCornerShape(8.dp))
                        .padding(12.dp)

                )
                {
                    FixedSizeText(
                        text =
                        if (!feedbackLone.isNullOrEmpty()) {
                            feedbackLone
                        } else {
                            "無評語字數測試字數測試字數測試字數測試字數測試字數測試字數測試字數測試字數測試字數測試字數測試字數測試字數測試字數測試"
                        },
                        size = (size / 6).dp,
                        color = colorResource(id = R.color.black)
                    )
                }
                Icon(
                    painter = painterResource(id = R.drawable.teacher),
                    contentDescription = "teacher",
                    modifier = Modifier.height(80.dp).scale(-1f, 1f),
                    tint = androidx.compose.ui.graphics.Color.Unspecified
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}


//跑馬燈
@Composable
fun marqueeText(text: String, size: Int, marqueeSize: Float) {
    var offset by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            offset -= 1f
            if (offset < -marqueeSize) { // 調整 -300f 根據字串長度
                offset = marqueeSize
            }
            delay(8L) // 控制速度
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RectangleShape)
            .offset(x = offset.dp)
    ) {
        AutoSizedText(
            text = text,
            size = size,
        )
    }
}


@Composable
fun feedBack(
    feedBack: List<String>,
    feedBackSize: Int,
    size: Int = 240,
    color: androidx.compose.ui.graphics.Color,
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically // 垂直居中對齊
        )
        {
            Icon(
                painter = painterResource(id = R.drawable.teacher),
                contentDescription = "teacher",
                modifier = Modifier.height(80.dp),
                //.padding(end = 8.dp, top = 4.dp, bottom = 4.dp)
                tint = androidx.compose.ui.graphics.Color.Unspecified
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(color)
                    .padding(12.dp)
            )
            {
                FixedSizeText(
                    text =
                    if (!feedBack.isNullOrEmpty()) {
                        feedBack[0].replace("[", "").replace("]", "");
                    } else {
                        "無評語"
                    },
                    size = (size / 6).dp,
                    color = colorResource(id = R.color.black)
                )
            }
        }
        if (feedBackSize >= 2) {
            Row {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(color)
                        .padding(12.dp)
                )
                {
                    FixedSizeText(
                        text =
                        if (!feedBack.isNullOrEmpty()) {
                            feedBack[1].replace("[", "").replace("]", "");
                        } else {
                            "無評語"
                        },
                        size = (size / 6).dp,
                        color = colorResource(id = R.color.black)
                    )
                }

            }
        }
        if (feedBackSize >= 3) {
            Row {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(color)
                        .padding(12.dp)
                )
                {
                    FixedSizeText(
                        text =
                        if (feedBack != null) {
                            feedBack[2].replace("[", "").replace("]", "");
                        } else {
                            "無評語"
                        },
                        size = (size / 6).dp,
                        color = colorResource(id = R.color.black)
                    )
                }
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun DefaultPreview() {
    MainPage()
}