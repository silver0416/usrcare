package com.tku.usrcare.view.ui.clock

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.tku.usrcare.view.Screen
import com.tku.usrcare.view.component.TitleBox
import com.tku.usrcare.view.ui.theme.UsrcareTheme


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
                    color = colorResource(id = R.color.black),
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate(Screen.Activity.route) },
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
                    color = colorResource(id = R.color.black),
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate(Screen.Drink.route) },
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
                    color = colorResource(id = R.color.black),
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate(Screen.Sleep.route) },
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
                    color = colorResource(id = R.color.black),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainPreview() {
    UsrcareTheme {
        Box {
            TitleBox(
                color = colorResource(id = R.color.btnClockColor),
                title = stringResource(id = R.string.clock_reminder),
                icon = painterResource(id = R.drawable.ic_clocknotice)
            )
            CenterButtons(navController = rememberNavController())
        }
    }
}

