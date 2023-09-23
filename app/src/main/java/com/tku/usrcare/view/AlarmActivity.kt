package com.tku.usrcare.view

import android.app.Activity
import android.content.Intent
import android.media.Ringtone
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tku.usrcare.R
import com.tku.usrcare.repository.AlarmService
import com.tku.usrcare.view.ui.theme.UsrcareTheme


class AlarmActivity : ComponentActivity() {
    private var ringtone: Ringtone? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        val title = intent.getStringExtra("title")
        val detail = intent.getStringExtra("detail")
        setContent {
            MaterialTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    AlarmMain(title, detail)
                }
            }
        }
    }


    private fun stopAlarm() {
        ringtone?.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        //停止鈴聲
        stopAlarm()
    }

}

@Composable
fun AlarmMain(title : String?, detail : String?) {
    BackHandler {
        // 不做任何事情
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.bgClock))
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AlarmTitle(title)
            Spacer(modifier = Modifier.size(1.dp))
            AlarmDetail(detail)
            Spacer(modifier = Modifier.size(40.dp))
            ExitButton()
        }
    }
}

@Composable
fun AlarmTitle(title : String?){
    Text(text = title.toString(), fontSize = 50.sp)
}

@Composable
fun AlarmDetail(title : String?){
    Text(text = title.toString(), fontSize = 30.sp)
}

@Composable
fun ExitButton() {
    val context = LocalContext.current
    Button(onClick = {
        val serviceIntent = Intent(context, AlarmService::class.java)
        context.stopService(serviceIntent)
        (context as? Activity)?.finish()
    }, modifier = Modifier.size(250.dp)) {
        Text(text = "關閉鬧鐘", fontSize = with(LocalDensity.current) { 50.dp.toSp() })
    }
}


@Preview(showBackground = true)
@Composable
fun AlarmPreview() {
    UsrcareTheme {
        AlarmMain("測試", "測試副標題")
    }
}