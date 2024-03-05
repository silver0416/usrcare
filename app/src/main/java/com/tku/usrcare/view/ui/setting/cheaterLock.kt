package com.tku.usrcare.view.ui.setting

import android.app.Activity
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.tku.usrcare.R
import com.tku.usrcare.repository.SessionManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheaterLock(activity: Activity, showDialog: MutableState<Boolean>) {
    data class BonusCode(val code: String, var times: Int)

    val codes = listOf(
        BonusCode("USRCARE", 1),
        BonusCode("2942", -1),
    )
    val startCheater = remember {
        mutableStateOf(false)
    }


    val context = LocalContext.current
    val sessionManager = SessionManager(context)
    val enteredCode = remember { mutableStateOf("") }

    if (startCheater.value) {
        Cheater(activity = activity)
    }

    val enteredCodePassed = remember {
        mutableStateOf(false)
    }

    val buttonColorState = remember {
        mutableStateOf("gray")
    }

    fun checkCode(code: String, finalCheck: Boolean = false): Boolean {
        for (bonusCode in codes) {
            if (code == bonusCode.code) {
                buttonColorState.value = "main"
                if (bonusCode.times == -1) {
                    buttonColorState.value = "green"
                    if (finalCheck) {
                        sessionManager.saveCheatAccess(true)
                        startCheater.value = true
                    }
                } else {
                    if (finalCheck) {
                        startCheater.value = true
                    }
                }
                return true
            } else {
                buttonColorState.value = "gray"
            }
        }
        return false
    }

    AlertDialog(onDismissRequest = {
        showDialog.value = false
    }, confirmButton = {
        Button(
            onClick = {
                if (enteredCodePassed.value) {
                    checkCode(enteredCode.value, true)
                }
            },
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = when (buttonColorState.value) {
                    "gray" -> {
                        colorResource(id = R.color.gray)
                    }

                    "green" -> {
                        colorResource(id = R.color.green)
                    }

                    else -> {
                        colorResource(id = R.color.MainButtonColor)
                    }
                }
            ),
        ) {
            Text(text = "輸入")
        }
    },

        dismissButton = {
            TextButton(onClick = { showDialog.value = false }) {
                Text(text = "取消")
            }
        }, title = {
            Text(
                text = "請輸入獎勵代碼"
            )
        }, text = {
            OutlinedTextField(
                value = enteredCode.value,
                onValueChange = { if (it.length <= 8) enteredCode.value = it },
                singleLine = true,
                trailingIcon = {
                    if (checkCode(enteredCode.value)) {
                        enteredCodePassed.value = true
                        Text(text = "✅")
                    } else {
                        if (enteredCode.value.isNotEmpty()) {
                            enteredCodePassed.value = false
                            Text(text = "❌")
                        }
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = when (buttonColorState.value) {
                        "gray" -> {
                            colorResource(id = R.color.gray)
                        }

                        "green" -> {
                            colorResource(id = R.color.green)
                        }

                        else -> {
                            colorResource(id = R.color.MainButtonColor)
                        }
                    },
                    unfocusedIndicatorColor = colorResource(id = R.color.gray).copy(alpha = 0.5f),
                    cursorColor = colorResource(id = R.color.gray),
                ),
                textStyle = TextStyle(
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    fontSize = 30.sp
                ),
            )
        })
}