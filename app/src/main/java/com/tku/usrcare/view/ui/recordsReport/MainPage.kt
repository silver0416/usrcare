package com.tku.usrcare.view.ui.recordsReport

import TopBar
import android.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import android.util.Log
import android.webkit.WebView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.anastr.speedometer.SpeedView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.charts.RadarChart
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
import com.tku.usrcare.R
import com.tku.usrcare.api.ApiUSR
import com.tku.usrcare.model.AD8
import com.tku.usrcare.model.LonelinessScale
import com.tku.usrcare.model.checkin
import com.tku.usrcare.model.exercise
import com.tku.usrcare.model.healthReport
import com.tku.usrcare.model.mental_record
import com.tku.usrcare.model.pet_companion
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.component.AutoSizedText
import com.tku.usrcare.view.component.FixedSizeText
import com.tku.usrcare.view.component.TitleBox

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
                    mood_trend=""),
                exercise = exercise(
                    average_exercise_score = 0.0f,
                    exercise_level="",
                    feedback="",
                ),
                mental_record = mental_record(
                    AD8= AD8(
                        average_score=0,
                        feedback="",
                    ),
                    LonelinessScale= LonelinessScale(
                        average_score=0,
                        feedback="",
                        mood_level="",
                    ),
                ),
                LonelinessScale = listOf(""),
                pet_companion = pet_companion(
                    total_steps=0,
                    average_daily_steps=0,
                    goal_achievement_dates= listOf("12-31"),
                    goal_achievement_days=0,
                    goal_achievement_rate=0.0f,
                    goal_steps=0,
                    step_difference_from_last_period=0,
                    step_level="",
                    feedback="",
                ),
                reportPeriod = "daily",
                reportType = "summary"
            )
        )
    }
    ApiUSR.getHealthReport(
        SessionManager(context).getUserToken().toString(),
        onSuccess = {
            try {
                healthReport = it
                Log.d("MainPage", "healthReport_MainPage: $healthReport")
            }
            catch (e: Exception) {
                Log.d("MainPage", "healthReport_MainPage: $e")
            }
        },
        onError = {
            Log.d("MainPage", "healthReport_MainPage: Error")
        },
        onInternetError = {
            Log.d("MainPage", "healthReport_MainPage: InternetError")
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
        Items(
            stringResource(id = R.string.daily_task),
            painterResource(id = R.drawable.ic_dailytask),
            colorResource(id = R.color.btnDailyTaskColor),
            false
        ),
        Items(
            stringResource(id = R.string.brain_game),
            painterResource(id = R.drawable.ic_game),
            colorResource(id = R.color.btnBrainGameColor),
            false
        )
        ,
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
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        TitleBox(
            color = colorResource(id = R.color.btnDailyTaskColor),
            title = stringResource(id = R.string.daily_task),
            icon = painterResource(id = R.drawable.ic_dailytask),
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
                        score(score =healthReport.checkin.average_mood_score,differenceFromLastPeriod = healthReport.checkin.mood_difference_from_last_period,trend = healthReport.checkin.mood_trend,feedback = healthReport.checkin.feedback.toString(),color = colorResource(id = R.color.bgSignSignHappy))
                    }
                    "寵物陪伴" -> {
                        //圓餅圖
                        PieChartView()
                        //長條圖
                        BarChart()
                    }
                    "心情量表" -> {
                        lineChart()
                    }
                    "愛來運動" -> {
                        gasGraph(score=healthReport.exercise.average_exercise_score,text=healthReport.exercise.feedback,color = colorResource(id = R.color.bgSports))
                    }
                    "動腦小遊戲" -> {
                        RadarChart()
                    }
                }
                FixedSizeText(
                    text = "評語",
                    size = 50.dp,
                    color = colorResource(id = R.color.black),)
                /*item {
                    titleBar(//簽簽樂
                        color = colorResource(id = R.color.btnSignsignhappyColor),
                        title = stringResource(id = R.string.sign_sign_happy),
                        icon = painterResource(id = R.drawable.ic_signsignhappy),
                    )
                    gasGraph(progress = 0.5f, maxValue = 100f)
                    FixedSizeText(
                        text = "文字長度測試。文字長度測試。文字長度測試。文字長度測試。文字長度測試。文字長度測試。文字長度測試。文字長度測試。文字長度測試。文字長度測試。文字長度測試。文字長度測試。文字長度測試。文字長度測試。文字長度測試。文字長度測試。文字長度測試。文字長度測試。文字長度測試。文字長度測試。文字長度測試。文字長度測試。文字長度測試。文字長度測試。文字長度測試。文字長度測試。文字長度測試。文字長度測試。文字長度測試。文字長度測試。文字長度測試。文字長度測試。文字長度測試。文字長度測試。文字長度測試。文字長度測試。文字長度測試。文字長度測試。文字長度測試。文字長度測試。文字長度測試。",
                        size = 50.dp,
                        color = colorResource(id = R.color.black),)
                }
                item {
                    titleBar(//每日任務
                        color = colorResource(id = R.color.btnDailyTaskColor),
                        title = stringResource(id = R.string.daily_task),
                        icon = painterResource(id = R.drawable.ic_dailytask),
                    )
                    lineChart()
                }
                item {
                    titleBar(//動腦小遊戲
                        color = colorResource(id = R.color.btnBrainGameColor),
                        title = stringResource(id = R.string.brain_game),
                        icon = painterResource(id = R.drawable.ic_game),
                    )
                    BarChart()
                }
                item {
                    titleBar(//寵物陪伴
                        color = colorResource(id = R.color.btnPetcompanyColor),
                        title = stringResource(id = R.string.pet_company),
                        icon = painterResource(id = R.drawable.ic_petcompany),
                    )
                    RadarChart()
                }
                item {
                    titleBar(//愛來運動
                        color = colorResource(id = R.color.btnAiVitalityDetection),
                        title = stringResource(id = R.string.AI_vitality_detection),
                        icon = painterResource(id = R.drawable.ic_aivitalitydetection),
                    )
                    PieChartView()
                }
                /*item {
                    titleBar(//鬧鐘小提醒
                        color = colorResource(id = R.color.btnAiVitalityDetection),
                        title = stringResource(id = R.string.AI_vitality_detection),
                        icon = painterResource(id = R.drawable.ic_aivitalitydetection),
                    )
                    PieChartView()
                }*/
                item {
                    titleBar(//心情量表
                        color = colorResource(id = R.color.btnMoodScaleColor),
                        title = stringResource(id = R.string.mood_scale),
                        icon = painterResource(id = R.drawable.ic_moodscale),
                    )
                    //PieChartView()
                }*/
            }
        }
    }


}
@Composable
fun gasGraph(score: Float = 0.0f,text:String="",color: androidx.compose.ui.graphics.Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,)
    {
        SpeedView(
            modifier = Modifier.size(250.dp),
            speed = score,
            //startDegree = 180,
            //endDegree = 360,
            unit = "分",
            speedText = {},  // 隱藏速度文字
            unitText = {},  // 隱藏單位文字
            minSpeed = 0f,
            maxSpeed = 10f,
            unitUnderSpeed = false,
            centerContent= {
                Box(Modifier.padding(top=90.dp))
                {
                    AutoSizedText(text = score.toString(),size=50,color= colorResource(id = R.color.black))
                }
            },
        )
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color))
        {
            FixedSizeText(text ="評語:$text",size=50.dp,color= colorResource(id = R.color.black))
        }
    }

}

//折線圖
@Composable
fun lineChart() {
    // 使用 AndroidView 嵌入 MPAndroidChart 的 LineChart
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
fun BarChart() {
    // 使用 AndroidView 嵌入 MPAndroidChart 的 BarChart
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

            // Y 軸設定
            axisRight.isEnabled = false // 隱藏右側 Y 軸

            // 填入數據
            val entries = listOf(
                BarEntry(0f, 2f),
                BarEntry(1f, 4f),
                BarEntry(2f, 6f),
                BarEntry(3f, 8f),
                BarEntry(4f, 3f)
            )

            val dataSet = BarDataSet(entries, "Sample Data").apply {
                color = Color.BLUE
                valueTextColor = Color.BLACK
                valueTextSize = 16f
            }

            val barData = BarData(dataSet)
            data = barData
            invalidate() // 刷新圖表
        }
    }, modifier = Modifier.size(250.dp))
}

//雷達圖
@Composable
fun RadarChart() {
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
                    valueFormatter = IndexAxisValueFormatter(listOf("反應力", "持續力", "記憶力")) // 標籤
                }

                // Y軸設置 (對應雷達圖的半徑方向)
                yAxis.apply {
                    setDrawLabels(false) // 不顯示數值標籤
                    axisMinimum = 0f // 設置最小值
                    axisMaximum = 100f // 設置最大值
                }

                // 範例數據
                val entries = listOf(
                    RadarEntry(60f),  // 屬性 1
                    RadarEntry(80f),  // 屬性 2
                    RadarEntry(50f)   // 屬性 3
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
fun PieChartView() {
    AndroidView(
        factory = { context ->
            PieChart(context).apply {
                description.isEnabled = false
                isDrawHoleEnabled = false  // 關閉中間的空心效果
                val entries = listOf(
                    PieEntry(40f, "持續力"),
                    PieEntry(30f, "記憶力"),
                    PieEntry(30f, "反應力")
                )
                val dataSet = PieDataSet(entries, "").apply {
                    //圓餅圖顏色
                    colors = listOf(Color.RED, Color.YELLOW, Color.GREEN)
                    sliceSpace = 5f
                    valueTextSize = 25f//區塊數值的文字大小30f=30sp
                }
                data = PieData(dataSet)
                setEntryLabelTextSize(25f)//區塊標籤的文字大小
                legend.textSize = 14f//圖例的文字大小
                setEntryLabelColor(Color.BLACK)//區塊標籤的文字顏色
                invalidate()  // 刷新圖表
            }
        },
        modifier = Modifier.size(250.dp)
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
    score: Float=0.0f,
    differenceFromLastPeriod:Float=0.0f,
    trend:String="上升",
    feedback:String="",
    color: androidx.compose.ui.graphics.Color)
{
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    )
    {
        Row(
            modifier = Modifier
                .height(250.dp)
                .fillMaxWidth(),
            horizontalArrangement  = Arrangement.Center,verticalAlignment = Alignment.CenterVertically)
        {
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .clip(CircleShape)
                    .border(
                        width = 10.dp,
                        color = colorResource(id = R.color.btnAiVitalityDetection),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center)
            {
                AutoSizedText(text =score.toString(),size=70,color= colorResource(id = R.color.black))
            }
            Icon(
                painter =painterResource(id = if(trend.equals("下降")||trend.equals("上升")) R.drawable.ic_back_arr else R.drawable.minus),
                contentDescription = "up or down",
                modifier = Modifier
                    .size(50.dp)
                    .padding(5.dp, 5.dp, 5.dp, 5.dp)
                    .graphicsLayer(
                        rotationZ = if (trend.equals("下降")) 270f else if (trend.equals(
                                "上升"
                            )
                        ) 90f else 0f
                    ),
                tint = colorResource(id = if(trend.equals("下降")) R.color.red else if(trend.equals("上升"))R.color.green else R.color.gray)
            )
            AutoSizedText(text =differenceFromLastPeriod.toString(),size=30,color= colorResource(id = R.color.black))
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color))
        {
            FixedSizeText(text ="評語:"+feedback.replace("[", "").replace("]", ""),size=50.dp,color= colorResource(id = R.color.black))
        }
    }
}

@Composable
@Preview(showBackground = true)
fun DefaultPreview() {
    MainPage()
}