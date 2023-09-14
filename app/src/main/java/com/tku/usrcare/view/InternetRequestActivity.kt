package com.tku.usrcare.view

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
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
import androidx.core.content.ContextCompat.getSystemService
import com.tku.usrcare.R
import com.tku.usrcare.view.ui.theme.UsrcareTheme


class InternetRequestActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkInternetExist(this)
        setContent {
            BackHandler(true) {

            }
            UsrcareTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = colorResource(id = R.color.white)
                ) {
                    InternetRequestPage()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onResume() {
        super.onResume()
        checkInternetExist(this)
    }
}

@Composable
fun InternetRequestPage() {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_disconnect),
            contentDescription = null,
            modifier = Modifier
                .size(120.dp)
                .padding(16.dp)
        )
        Text(
            textAlign = TextAlign.Center,
            text = stringResource(id = R.string.internet_request),
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
            onClick = { launchInternetSetting(context) },
            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.MainButtonColor)),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(
                text = stringResource(id = R.string.go_open_internet),
                style = TextStyle(
                    fontSize = with(
                        LocalDensity.current
                    ) { 28.dp.toSp() }, color = colorResource(id = R.color.MainButtonTextColor)
                )
            )
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp, 16.dp, 0.dp)
                .wrapContentHeight(),
            onClick = { checkInternetExist(context) },
            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.SecondaryButtonColor)),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(3.dp, colorResource(id = R.color.SecondaryButtonTextColor))
        ) {
            Text(
                text = stringResource(id = R.string.retry),
                style = TextStyle(
                    fontSize = with(
                        LocalDensity.current
                    ) { 28.dp.toSp() }, color = colorResource(id = R.color.SecondaryButtonTextColor)
                )
            )
        }
    }
}

fun launchInternetSetting(
    context: Context
) {
    val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
    context.startActivity(intent)
}


private fun checkInternetExist(context: Context) {
    val connectivityManager = getSystemService(context, ConnectivityManager::class.java) as ConnectivityManager
    val network = connectivityManager.activeNetwork
    val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
    val intent = Intent()

    // 檢查網路是否可用
    val isInternetAvailable = network != null &&
            networkCapabilities != null &&
            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

    if (isInternetAvailable) {
        // 如果網路可用，則啟動 MainActivity
        intent.setClass(context, MainActivity::class.java)
        context.startActivity(intent)
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewInternetRequestActivity() {
    UsrcareTheme {
        InternetRequestPage()
    }
}