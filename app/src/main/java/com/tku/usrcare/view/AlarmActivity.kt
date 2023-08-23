package com.tku.usrcare.view

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tku.usrcare.R
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.ui.theme.UsrcareTheme

class AlarmActivity : ComponentActivity() {
    private var ringtone: Ringtone? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtone = RingtoneManager.getRingtone(this, ringtoneUri)
        ringtone?.play()
        setContent {
            UsrcareTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Main()
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
fun Main() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.bgClock))
    ) {
        ExitButton()
    }
}
@Composable
fun ExitButton(){
    val context = LocalContext.current
    Button(onClick = { (context as? Activity)?.finish() },modifier = Modifier.size(250.dp)) {
        Text(text = "關閉鬧鐘",fontSize = 50.sp)
    }
}


@Preview(showBackground = true)
@Composable
fun AlarmPreview() {
    UsrcareTheme {
        Main()
    }
}