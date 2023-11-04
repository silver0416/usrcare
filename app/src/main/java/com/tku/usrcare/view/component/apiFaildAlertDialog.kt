package com.tku.usrcare.view.component


import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.tku.usrcare.R
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.LoginActivity

@Composable
fun ApiFaildAlertDialogCompose(context: Context, errorMessage: String) {
    Log.d("ApiFaildAlertDialogCompose", "ApiFaildAlertDialogCompose: $errorMessage")
    val openDialog = remember {
        mutableStateOf(true)
    }
    val sessionManager = SessionManager(context)
    if (errorMessage == "403") {
        sessionManager.clearAll(context)
    }
    if (openDialog.value) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = {
                when (errorMessage) {
                    "403" -> {
                        val intent = Intent()
                        intent.setClass(context, LoginActivity::class.java)
                        context.startActivity(intent)
                    }

                    else -> {
                        openDialog.value = false
                    }
                }
            },
            title = { Text(stringResource(id = R.string.error_title)) },
            text = {
                Text(
                    when (errorMessage) {
                        "403" -> stringResource(id = R.string.logged_out)
                        else -> "${stringResource(id = R.string.error_message)}\n${stringResource(id = R.string.error_reason)}:$errorMessage"
                    }
                )
            },
            confirmButton = {
                androidx.compose.material3.Button(
                    onClick = {
                        when (errorMessage) {
                            "403" -> {
                                val intent = Intent()
                                intent.setClass(context, LoginActivity::class.java)
                                context.startActivity(intent)
                            }

                            else -> {
                                openDialog.value = false
                            }
                        }
                    }
                ) {
                    Text(stringResource(id = R.string.confirm))
                }
            }
        )
    }
}


fun ApiFaildAlertDialog(errorMessage: String, activity: Activity) {
    AlertDialog.Builder(activity)
        .setTitle("錯誤")
        .setMessage(
            when (errorMessage) {
                "403" -> "您已被登出，請重新登入"
                else -> "伺服器錯誤，請聯絡開發人員\n錯誤代碼:$errorMessage"
            }
        )
        .setPositiveButton("確定") { _, _ ->
        }
        .setOnDismissListener {
            when (errorMessage) {
                "403" -> {
                    val intent = Intent()
                    intent.setClass(activity, LoginActivity::class.java)
                    activity.startActivity(intent)
                }
            }
        }
        .create()
        .show()
}