package com.tku.usrcare.view.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.ramcosta.composedestinations.spec.DestinationStyle.Dialog.Default.properties

@Composable
fun missionCompleteDialog(
    onDismiss:()->Unit,
    onConfirm:()->Unit,
    showDialog:Boolean,
    content: String,
    icon: Painter,
    content2: String,
    //buttonText:String,
    color: Color,
    backgroundColor: Color,
) {
    if(showDialog) {
        AlertDialog(
            modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(5.dp, color,shape = RoundedCornerShape(16.dp)),
            backgroundColor = backgroundColor,
            onDismissRequest = { onDismiss()},
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    FixedSizeText(
                        text = content,
                        size = 70.dp
                    )
                    Icon(
                        painter = icon,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 8.dp, top = 4.dp, bottom = 4.dp)
                            .size(50.dp),
                        tint = Color.Unspecified
                    )
                    FixedSizeText(
                        text = content2,
                        size = 70.dp
                    )
                }
            },
            buttons = {
                /*Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = { onConfirm();onDismiss()},
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = color,
                            contentColor = Color.White
                        ), modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        FixedSizeText(buttonText, size = 60.dp, color = Color.White)
                    }
                }*/
            },
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = true)
        )
    }
}