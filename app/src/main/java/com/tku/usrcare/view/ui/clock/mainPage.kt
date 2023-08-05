package com.tku.usrcare.view.ui.clock

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tku.usrcare.R
import com.tku.usrcare.model.ClockData
import com.tku.usrcare.view.Screen
import com.tku.usrcare.view.findActivity
import com.tku.usrcare.view.ui.theme.UsrcareTheme

@Composable
fun TitleBox() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val activity = context.findActivity()
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        Card(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
                .shadow(15.dp, RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(colorResource(id = R.color.btnClockColor))
        ) {
            Row(
                modifier = Modifier
                    .width(320.dp)
                    .padding(3.dp, 10.dp, 10.dp, 10.dp),
                horizontalArrangement = Arrangement.Center,
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
                Spacer(Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = R.drawable.ic_clocknotice),
                    contentDescription = null,
                    modifier = Modifier
                        .size(42.dp),
                    tint = colorResource(id = R.color.white)
                )
                Spacer(modifier = Modifier.width(7.dp))
                Text(
                    text = stringResource(R.string.clock_reminder),
                    fontSize = 28.sp,
                    color = colorResource(id = R.color.white),
                )
                Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun CenterButtons(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { navController.navigate(Screen.Drug.route) },
                modifier = Modifier
                    .width(282.dp)
                    .height(93.dp)
                    .border(
                        4.dp,
                        colorResource(id = R.color.bgClockCard),
                        RoundedCornerShape(16.dp)
                    ),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.white))
            ) {
                Text(
                    text = stringResource(R.string.drug_reminder),
                    fontSize = 40.sp,
                    color = colorResource(id = R.color.txClockColor),
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* Handle click */ },
                modifier = Modifier
                    .width(282.dp)
                    .height(93.dp)
                    .border(
                        4.dp,
                        colorResource(id = R.color.bgClockCard),
                        RoundedCornerShape(16.dp)
                    ),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.white))
            ) {
                Text(
                    stringResource(R.string.activity_reminder),
                    fontSize = 40.sp,
                    color = colorResource(id = R.color.txClockColor),
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* Handle click */ },
                modifier = Modifier
                    .width(282.dp)
                    .height(93.dp)
                    .border(
                        4.dp,
                        colorResource(id = R.color.bgClockCard),
                        RoundedCornerShape(16.dp)
                    ),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.white))
            ) {
                Text(
                    stringResource(R.string.drink_water_reminder),
                    fontSize = 40.sp,
                    color = colorResource(id = R.color.txClockColor),
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* Handle click */ },
                modifier = Modifier
                    .width(282.dp)
                    .height(93.dp)
                    .border(
                        4.dp,
                        colorResource(id = R.color.bgClockCard),
                        RoundedCornerShape(16.dp)
                    ),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.white))
            ) {
                Text(
                    stringResource(R.string.rest_reminder), fontSize = 40.sp,
                    color = colorResource(id = R.color.txClockColor),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun MainPreview() {
    UsrcareTheme {
        TitleBox()
        CenterButtons(navController = rememberNavController())
    }
}

