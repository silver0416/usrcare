package com.tku.usrcare.view.component


import android.app.Activity
import android.app.AlertDialog
import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.tku.usrcare.R

@Composable
fun ApiFaildAlertDialogCompose(errorMessage: String) {
    Log.d("ApiFaildAlertDialogCompose", "ApiFaildAlertDialogCompose: $errorMessage")
    val openDialog = remember {
        mutableStateOf(true)
    }
    if (openDialog.value) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text(stringResource(id = R.string.error_title)) },
            text = { Text("${stringResource(id = R.string.error_message)}\n${stringResource(id = R.string.error_reason)}:$errorMessage") },
            confirmButton = {
                androidx.compose.material3.Button(
                    onClick = {
                        openDialog.value = false
                    }
                ) {
                    Text(stringResource(id = R.string.confirm))
                }
            }
        )
    }
}


fun ApiFaildAlertDialog(errorMessage: String , activity: Activity) {
    AlertDialog.Builder(activity)
        .setTitle("錯誤")
        .setMessage("伺服器錯誤，請聯絡開發人員\n錯誤代碼:$errorMessage")
        .setPositiveButton("確定") { _, _ ->
        }
        .create()
        .show()
}