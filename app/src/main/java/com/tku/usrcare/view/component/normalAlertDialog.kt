package com.tku.usrcare.view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.ramcosta.composedestinations.spec.DestinationStyle.Dialog.Default.properties
import com.tku.usrcare.R

@Composable
fun normalAlertDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    showDialog: Boolean,
    title: String,
    content: String,
    buttonText: String,
    color: Color,
    backgroundColor: Color,
) {
    if (showDialog) {
        AlertDialog(
            modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(5.dp, color, shape = RoundedCornerShape(16.dp)),
            backgroundColor = backgroundColor,
            //.background(color = colorResource(id = R.color.black),shape = RoundedCornerShape(16.dp)),
            onDismissRequest = { onDismiss() },
            title = { FixedSizeText(text = title, size = 80.dp, fontWeight = FontWeight.Bold) },
            text = {
                Column() {
                    FixedSizeText(
                        text = content,
                        size = 70.dp
                    )
                }
            },
            buttons = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = { onConfirm();onDismiss() },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = color,
                            contentColor = Color.White
                        ), modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        FixedSizeText(buttonText, size = 60.dp, color = Color.White)
                    }
                }
            },
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        )
    }
}

@Composable
fun twoButtonDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    showDialog: Boolean,
    title: String,
    content: String,
    buttonYes: String,
    buttonNo: String,
    color: Color,
    backgroundColor: Color,
) {
    if (showDialog) {
        AlertDialog(
            modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(5.dp, color, shape = RoundedCornerShape(16.dp)),
            backgroundColor = backgroundColor,
            onDismissRequest = { onDismiss() },
            title = { FixedSizeText(text = title, size = 80.dp, fontWeight = FontWeight.Bold) },
            text = {
                Column() {
                    FixedSizeText(
                        text = content,
                        size = 70.dp
                    )
                }
            },
            buttons = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    //verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(//取消按鈕
                        onClick = { onDismiss() },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = color,
                            contentColor = Color.White
                        ), modifier = Modifier.padding(3.dp)
                    ) {
                        FixedSizeText(buttonNo, size = 60.dp, color = Color.White)
                    }
                    Button(//確認按鈕
                        onClick = {
                            onConfirm()
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = color,
                            contentColor = Color.White
                        ), modifier = Modifier.padding(3.dp)
                    ) {
                        FixedSizeText(buttonYes, size = 60.dp, color = Color.White)
                    }
                }
            },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        )
    }
}