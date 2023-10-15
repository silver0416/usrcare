package com.tku.usrcare.view

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tku.usrcare.R
import com.tku.usrcare.view.ui.theme.UsrcareTheme


class PermissionsRequestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val packageName = packageName
        val applicationInfo = applicationInfo

        super.onCreate(savedInstanceState)
        checkNotificationsPermission(intent, this)
        setContent {
            BackHandler(true) {

            }
            UsrcareTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = colorResource(id = R.color.white)
                ) {
                    NotificationRequestPage(packageName, applicationInfo)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checkNotificationsPermission(intent, this)
    }
}

@Composable
fun NotificationRequestPage(packageName: String, applicationInfo: ApplicationInfo) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_notification),
            contentDescription = null,
            modifier = Modifier
                .size(120.dp)
                .padding(16.dp)
        )
        Text(
            textAlign = TextAlign.Center,
            text = stringResource(id = R.string.notification_request),
            style = TextStyle(
                fontSize = with(
                    LocalDensity.current
                ) { 28.dp.toSp() }, color = colorResource(id = R.color.black)
            ),
            modifier = Modifier.padding(16.dp)
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .wrapContentHeight(),
            onClick = { launchNotificationSetting(packageName, applicationInfo, context) },
            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.MainButtonColor)),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(
                text = stringResource(id = R.string.go_open_notification),
                style = TextStyle(
                    fontSize = with(
                        LocalDensity.current
                    ) { 28.dp.toSp() }, color = colorResource(id = R.color.MainButtonTextColor)
                )
            )
        }
    }
}

fun launchNotificationSetting(
    packageName: String,
    applicationInfo: ApplicationInfo,
    context: Context
) {
    val intent = Intent()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
    } else {
        // 舊版本
        intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
        intent.putExtra("app_package", packageName)
        intent.putExtra("app_uid", applicationInfo.uid)
    }
    context.startActivity(intent)
}

private fun checkNotificationsPermission(intent: Intent,context: Context){
    // 檢查權限
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val areNotificationsEnabled = notificationManager.areNotificationsEnabled()
    if (areNotificationsEnabled) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel =
                notificationManager.getNotificationChannel(context.getString(R.string.clock_reminder))
            val isChannelEnabled = channel?.importance != NotificationManager.IMPORTANCE_NONE
            if (isChannelEnabled) {
                intent.setClass(context, MainActivity::class.java)
                context.startActivity(intent)
            }
        }
        else{
            intent.setClass(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewPermissionsRequestActivity() {
    UsrcareTheme {
        NotificationRequestPage("", ApplicationInfo())
    }
}